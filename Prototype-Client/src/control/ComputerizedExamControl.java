package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import entity.ExamDetailsMessage;
import entity.Question;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import ocsf.client.AbstractClient;

public class ComputerizedExamControl extends AbstractClient implements Initializable {
	public static HashMap<String, Integer> examAnswers;// saves the question id and the answers
	public static int remainTime;
	private int index = -1;
	public static String timeToString;
	private static ArrayList<Question> questioninexecutedexam = new ArrayList<Question>();
	public static Time solutionTime = new Time(02, 30, 00);
	protected Object[] messageToServer = new Object[3];
	private static String executedID;
	public static Timer timer;
	@FXML
	private TextField questionContent;

	@FXML
	private Button prevBTN;

	@FXML
	private TextField answer3;

	@FXML
	private ToggleGroup answers;

	@FXML
	private TextField answer2;

	@FXML
	private TextField answer4;

	@FXML
	private TextField timerTextField;

	@FXML
	private TextField answer1;

	@FXML
	private RadioButton correctRadioButton2;

	@FXML
	private Button finishButton;

	@FXML
	private RadioButton correctRadioButton1;

	@FXML
	private RadioButton correctRadioButton4;

	@FXML
	private RadioButton correctRadioButton3;

	@FXML
	private Label courseName;

	@FXML
	private Button nextBTN;

	@FXML
	private Label pageLabel;

	public ComputerizedExamControl(String host, int port) {
		super(host, port);
	}

	public ComputerizedExamControl() throws IOException {
		super("localhost", 5555);
		openConnection();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Question q = new Question();
		q.setAnswer1("1");
		q.setAnswer2("kaki");
		q.setAnswer3("pipi");
		q.setAnswer4("lo po");
		q.setCorrectAnswer("3");
		q.setId("010001");
		q.setTeacherName("k");
		q.setQuestionContent("who are we?");
		questioninexecutedexam.add(q);
		correctRadioButton1.setVisible(true);
		correctRadioButton2.setVisible(true);
		correctRadioButton3.setVisible(true);
		correctRadioButton4.setVisible(true);
		answer1.setVisible(true);
		answer2.setVisible(true);
		answer3.setVisible(true);
		answer4.setVisible(true);
		examAnswers = new HashMap<String, Integer>();
		nextQuestion(null);
		prevBTN.setVisible(false);
		// timerTextField.setText("123");
		// s=solutionTime.toString();
		// timerTextField.setText(s);
		remainTime = solutionTime.getHours() * 3600 + solutionTime.getMinutes() * 60 + solutionTime.getSeconds();// reamin

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int sec = setInterval();
				if (remainTime == 1) {
					Platform.runLater(() -> openScreen("ErrorMessage", "Time is over"));
					questionContent.setText("time is over click Finish");
					correctRadioButton1.setVisible(false);
					correctRadioButton2.setVisible(false);
					correctRadioButton3.setVisible(false);
					correctRadioButton4.setVisible(false);
					answer1.setVisible(false);
					answer2.setVisible(false);
					answer3.setVisible(false);
					answer4.setVisible(false);
				}
				timeToString = intToTime(sec).toString();
				timerTextField.setText(timeToString);
			}
		}, 1000, 1000);
	}

	public static Time intToTime(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int sec = seconds % 60;
		Time t = new Time(hours, minutes, sec);
		return t;
	}

	private static final int setInterval() {
		if (remainTime == 1)
			timer.cancel();
		return --remainTime;
	}

	protected void handleMessageFromServer(Object msg) {
		final Object[] msgFromServer = (Object[]) msg;
		switch (msgFromServer[0].toString()) {

		case "addTime": {
			addTimeToExam(msgFromServer);
		}
		}
	}

	public void addTimeToExam(Object[] message) {
		int timeToAdd;
		Time timeFromMessage = (Time) message[2];
		if (executedID.equals((String) message[1])) {// if the student perform the relevant exam
			timeToAdd = timeFromMessage.getHours() * 3600 + timeFromMessage.getMinutes() * 60
					+ timeFromMessage.getSeconds();// reamin time is he time in secods
			remainTime += timeToAdd;
		}
	}

	@FXML
	void previousQuestion(ActionEvent event) {
		addAnswerToHashMap();
		index--;
		setQuestion();
		if (index == 0) {
			prevBTN.setVisible(false);
		}
		nextBTN.setVisible(true);
	}

	@FXML
	void nextQuestion(ActionEvent event) {
		if (index >= 0)
			addAnswerToHashMap();
		index++;
		setQuestion();
		if (index + 1 == questioninexecutedexam.size()) {
			nextBTN.setVisible(false);
		}
		prevBTN.setVisible(true);

	}

	public void addAnswerToHashMap() {
		int selectedAnswer = 0;
		if (index <= -1)
			return;
		String q_id = questioninexecutedexam.get(index).getId();

		if (correctRadioButton1.isSelected()) {
			selectedAnswer = 1;
		} else if (correctRadioButton2.isSelected()) {
			selectedAnswer = 2;
		} else if (correctRadioButton3.isSelected()) {
			selectedAnswer = 3;
		} else if (correctRadioButton4.isSelected()) {
			selectedAnswer = 4;
		}
		if (examAnswers.containsKey(q_id)) {
			examAnswers.replace(q_id, selectedAnswer);
		} else {
			examAnswers.put(q_id, selectedAnswer);
		}

	}

	@FXML
	void finishExam(ActionEvent e) {
		addAnswerToHashMap();
		String details[] = new String[2];
		details[0] = executedID;
		details[1] = Globals.getUser().getUsername();

		messageToServer[0] = "finishExam";
		messageToServer[1] = details;
		messageToServer[2] = examAnswers;
		try {
			sendToServer(messageToServer);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} // send the message to server
		try {
			closeScreen(e);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		openScreen("NewDesignHomeScreenStudent");
	}

	private void openScreen(String screen) {
		try {
			FXMLLoader loader = new FXMLLoader();
			if (screen.equals("LoginGui")) {
				loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			} else
				loader.setLocation(getClass().getResource("/studentBoundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			if (screen.equals("NewDesignHomeScreenStudent")) {
				StudentControl sControl = loader.getController();
				sControl.setStudentAuthor_Date_name();
			}
			if (screen.equals("ErrorMessage")) {
				ErrorControl tController = loader.getController();
				tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
				tController.setErrorMessage("ERROR");// send a the error to the alert we made
			}
			// stage.setTitle(screen);
			stage.setScene(scene);
			stage.show();

		} catch (Exception exception) {
			System.out.println("Error in opening the page");
			exception.printStackTrace();
		}
	}

	private void openScreen(String screen, String message) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			ErrorControl tController = loader.getController();
			tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
			tController.setErrorMessage(message);// send a the error to the alert we made
			stage.setTitle("Error message");
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}
	}

	public void closeScreen(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	private void setQuestion() {

		/*
		 * before setting questions need to get the answer the student selected if there
		 * is such answer , if null need to put on the correct answer of the current
		 * question as null .
		 * 
		 * need to use RadioButton selected = (RadioButton) group.getSelectedToggle();
		 * String selectedId = selected.getId(); int correctAns = need to get the number
		 * of the selected answer with switch case (chcking the value of "index" can be
		 * negative ? questioninexecutedexam.get(--index).setCorrectAnswer(correctAns);
		 */

		correctRadioButton1.setSelected(false);
		correctRadioButton2.setSelected(false);
		correctRadioButton3.setSelected(false);
		correctRadioButton4.setSelected(false);

		if (examAnswers.containsKey(questioninexecutedexam.get(index).getId())) {
			switch (examAnswers.get(questioninexecutedexam.get(index).getId())) {
			case 1:
				correctRadioButton1.setSelected(true);
				break;
			case 2:
				correctRadioButton2.setSelected(true);
				break;
			case 3:
				correctRadioButton3.setSelected(true);
				break;
			case 4:
				correctRadioButton4.setSelected(true);
				break;
			}
		}
		questionContent.setText(questioninexecutedexam.get(index).getQuestionContent());
		answer1.setText(questioninexecutedexam.get(index).getAnswer1());
		answer2.setText(questioninexecutedexam.get(index).getAnswer2());
		answer3.setText(questioninexecutedexam.get(index).getAnswer3());
		answer4.setText(questioninexecutedexam.get(index).getAnswer4());
	}

}
