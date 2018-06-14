package control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import entity.Exam;
import entity.ExamDetailsMessage;
import entity.Question;
import entity.QuestionInExam;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class StudentControl extends UserControl implements Initializable {
	private static Scene homeSc = null;
	private static Scene gradeSc = null; 
	private static ArrayList<Question> questioninexecutedexam;
	public static Time solutionTime;
	public static int remainTime;
	public static Timer timer;
	private int index = -1;
	private static Boolean copyFlag = false ; 
	public static String timeToString;
	public static HashMap<String, Integer> examAnswers;// saves the question id and the answers
	/********************* Variable declaration *************************/
	// *********for HomePage***********//
	@FXML
	private Label userNameLabel;
	@FXML
	private Label authorLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private TextField newMsgTextField;
	@FXML
	private ToggleGroup answers;
	// move to user
	private Calendar currentCalendar = Calendar.getInstance();
	private Date currentTime = currentCalendar.getTime();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
	private static String executedID;
	FXMLLoader loader = new FXMLLoader();

	// *********for student see his grades AND can order exam***********//
	@FXML
	private TableView<ExamDetailsMessage> examGradesTable;
	@FXML
	private TableColumn<ExamDetailsMessage, String> examCodeColumn;
	@FXML
	private TableColumn<ExamDetailsMessage, String> courseCodeColumn;
	@FXML
	private TableColumn<ExamDetailsMessage, String> gradeColumn;
	@FXML
	private TableColumn<ExamDetailsMessage, String> dateColumn;
	@FXML
	private TableColumn<ExamDetailsMessage, String> executedIDCol;

	@FXML
	private ComboBox<String> examCodeCombo;

	ObservableList<ExamDetailsMessage> detailsList = FXCollections.observableArrayList();
	ObservableList<String> executeExamList = FXCollections.observableArrayList();

	// *******for student execute or download exam*********//
	@FXML
	private TextField codeTextField;
	@FXML
	private CheckBox correctExamCodeCB;
	@FXML
	private TextField userIDTextField;
	@FXML
	private Button finishButton;
	// ******************** student perform exam ************//
	@FXML
	private RadioButton correctRadioButton2;
	@FXML
	private RadioButton correctRadioButton1;
	@FXML
	private RadioButton correctRadioButton4;
	@FXML
	private RadioButton correctRadioButton3;
	@FXML
	private TextField answer3;
	@FXML
	private TextField answer2;
	@FXML
	private TextField answer4;
	@FXML
	private TextField answer1;
	@FXML
	private Label pageLabel;
	@FXML
	private TextField questionContent;
	@FXML
	private Label courseName;
	@FXML
	private Button nextBTN;
	@FXML
	private Button prevBTN;
	@FXML
	private TextField timerTextField;
	@FXML
	private ImageView imageDraging;
	@FXML
	private ImageView wordLogo;
	@FXML
	private ImageView uploadImage;
	@FXML
	private Button uploadManualExamButton;
	// ******************** Showing exam copy  ************//
@FXML
private Label studentAnswer ; 	
@FXML
private Label selectedAnswer ; 	

	/************************ Class Methods *************************/
	public void initialize(URL url, ResourceBundle rb) {
		// connect(this);
		switch (pageLabel.getText()) {
		case ("Perform exam"): {
			correctRadioButton1.setVisible(true);
			correctRadioButton2.setVisible(true);
			correctRadioButton3.setVisible(true);
			correctRadioButton4.setVisible(true);
			answer1.setStyle("-fx-background-color: white;");
			answer2.setStyle("-fx-background-color: white;");
			answer3.setStyle("-fx-background-color: white;");
			answer4.setStyle("-fx-background-color: white;");
			answer1.setVisible(true);
			answer2.setVisible(true);
			answer3.setVisible(true);
			answer4.setVisible(true);
			
			nextQuestion(null);
			prevBTN.setVisible(false);
			if(copyFlag==false) {
			examAnswers = new HashMap<String, Integer>();
			// timerTextField.setText("123");
			// s=solutionTime.toString();
			// timerTextField.setText(s);
			remainTime = solutionTime.getHours() * 3600 + solutionTime.getMinutes() * 60 + solutionTime.getSeconds();//reamain is the time in seconds
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
			// courseName.setText(questioninexecutedexam.get(0).getId().substring(0, 2));
			break;
		}
		case ("My Grades sheet "): {
			getGradesFromServer();
			break;
		}
		default:
			return;
		}
	}

	{

	}

	private static final int setInterval() {
		if (remainTime == 1)
			timer.cancel();
		return --remainTime;
	}

	public static Time intToTime(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int sec = seconds % 60;
		Time t = new Time(hours, minutes, sec);
		return t;
	}

	/********************* general Functions *************************/
	public void setStudentAuthor_Date_name() {// *** move to userControl rename userDetails
		userNameLabel.setText(Globals.getFullName());
		dateLabel.setText(dateFormat.format(currentTime));// Setting Current Date
		authorLabel.setText("Student");
	}

	public void logoutPressed(ActionEvent e) throws Exception, SQLException { // *** move to userControl
		connect(this);
		messageToServer[0] = "logoutProcess";
		messageToServer[1] = Globals.getuserName();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
		closeScreen(e);
	}

	// the problem is with the fact that we create a new scene each time and we need
	// to prevent it in that way
	// ***
	public void closeScreen(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	// ***
	private void openScreen(String screen) {// open a window of screen
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

	// ***
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

	// ***
	@FXML
	public void goToHomePressed(ActionEvent e) throws Exception {
		closeScreen(e);
		openScreen("NewDesignHomeScreenStudent");
	}

	/********************* Student Home Screen listeners *************************/
	public void myGradesPressed(ActionEvent e) {
		try {
			closeScreen(e);
			openScreen("MyGradesScreen");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void orderExamCopyPressed(ActionEvent e) {
		try {
			closeScreen(e);
			openScreen("OrderExamCopyScreen");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void excecuteMorCExamPressed(ActionEvent e) {
		try {
			closeScreen(e);
			openScreen("ManualAndComputerizeExamScreen");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/********************* Student see his grades or get copy *************************/
	public void getGradesFromServer() {
		connect(this);
		messageToServer[0] = "getExamsByUserName";
		messageToServer[1] = Globals.getuserName();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	/*********************
	 * Student Order Copy
	 * 
	 * @throws IOException
	 *************************/
	public void orderExamPressed(ActionEvent e) {
		/*
		 * messageToServer[0] = "getExamsCopyByUserName"; messageToServer[1] =
		 * examCodeCombo.getValue(); messageToServer[2] = null;
		 * chat.handleMessageFromClientUI(messageToServer);// send the message to server
		 */
		connect(this);  
		messageToServer[0] = "getStudentAnswers";
		messageToServer[1] = examCodeCombo.getValue(); // sending executed exam id 
		messageToServer[2] = Globals.getUser().getUsername(); // sending the user name 
		chat.handleMessageFromClientUI(messageToServer);
	}

	

	/***************************
	 * Student excecute exam
	 * 
	 * @throws SQLException
	 * @throws IOException
	 ****************************/
	public void excecuteExam(ActionEvent e) throws IOException, SQLException {// click on the button "execute exam"
		if (codeTextField.getText().equals("") || userIDTextField.getText().equals((""))) {
			openScreen("ErrorMessage", "Error in executed exam id");
			return;
		} else if (!userIDTextField.getText().equals((Globals.getUser().getUserID()))) {
			openScreen("ErrorMessage", "Your ID is incorrect"); // if user ID isn't correct
			return;
		}
		// everything fine
		executedID = codeTextField.getText();
		connect(this); // connecting to server
		messageToServer[0] = "checkExecutedExam";
		messageToServer[1] = executedID;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}
	public void downloadExamPressed() {

	}
	/************************* checking message ***********************************/
	// for all windows
	@SuppressWarnings("unchecked")
	@Override
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();
			final Object[] msgFromServer = (Object[]) message;
			switch (msgFromServer[0].toString()) {
			case "logoutProcess": {
				openScreen("LoginGui");
				break;
			}
			case "getExamsByUserName": {
				showGradesOnTable((ArrayList<ExamDetailsMessage>) msgFromServer[1]);
				break;
			}
			case "checkExecutedExam": {
				checkExecutedExam((Object[]) msgFromServer);
				break;
			}
			case "addTime": {
				addTimeToExam(msgFromServer);
				break;
			}
			case "showingCopy" : 
				showingCopy((ArrayList<Question>)msgFromServer[1],(HashMap<String, Integer>)msgFromServer[2]);
				break;
			}

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/********************** Handling message from server ***********************/
	@SuppressWarnings("unchecked")
	private void checkExecutedExam(Object[] message) {
			ArrayList<Question> questioninexam = (ArrayList<Question>) message[1];
			Exam exam=(Exam) message[2];
			solutionTime = Time.valueOf(exam.getSolutionTime());
			questioninexecutedexam =  questioninexam;
			if (exam.getType().equals("manual")) {
				Platform.runLater(()->openScreen("ManualExam"));
			} else {
				Platform.runLater(()->openScreen("ComputerizedExam"));
			}
	}
	public void showingCopy(ArrayList<Question> ques ,HashMap<String, Integer>ans) {

		examAnswers = ans;
		questioninexecutedexam =  ques;
		copyFlag = true ; 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				openScreen("ComputerizedExam");  
			}
		});
	}



	@SuppressWarnings("deprecation")
	public void addTimeToExam(Object[] message) {
		int timeToAdd;
		Time timeFromMessage = (Time) message[2];
		if (executedID.equals((String) message[1])) {// if the student perform the relevant exam
			timeToAdd = timeFromMessage.getHours() * 3600 + timeFromMessage.getMinutes() * 60
					+ timeFromMessage.getSeconds();// reamin time is he time in secods
			remainTime += timeToAdd;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showGradesOnTable(ArrayList<ExamDetailsMessage> detailsFromS) {

		for (ExamDetailsMessage edM : detailsFromS) {
			detailsList.add(edM);
			executeExamList.add(edM.getExcecutedExamID());
		}

		if (examGradesTable != null && examGradesTable.getColumns() != null)
			examGradesTable.getColumns().clear();
		if (examCodeCombo != null && examCodeCombo.getItems() != null)
			examCodeCombo.getItems().clear();
		examCodeCombo.setItems(executeExamList);
		examGradesTable.setItems(detailsList);
		examCodeColumn.setCellValueFactory(new PropertyValueFactory("examID"));
		dateColumn.setCellValueFactory(new PropertyValueFactory("examDate"));
		gradeColumn.setCellValueFactory(new PropertyValueFactory("examGrade"));
		courseCodeColumn.setCellValueFactory(new PropertyValueFactory("examCourse"));
		executedIDCol.setCellValueFactory(new PropertyValueFactory<>("excecutedExamID"));
		examGradesTable.getColumns().addAll(examCodeColumn, courseCodeColumn, gradeColumn, dateColumn, executedIDCol);
		/*
		 * also need to take from detailsFromS the exam_id's and insert them to
		 * observeable list into the relevante combobox .
		 */
	}

	/************************ Student performing exam *************/
	@FXML
	private void nextQuestion(ActionEvent e) {
		if (index >= 0 && copyFlag == false )
			addAnswerToHashMap();
		index++;
		setQuestion();
		if (index + 1 == questioninexecutedexam.size()) {
			nextBTN.setVisible(false);
		}
		prevBTN.setVisible(true);

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
		
		if(copyFlag==true) {
		Boolean correctAns = false ; 
		String stdSelected = examAnswers.get(questioninexecutedexam.get(index).getId()).toString() ;
		String qustionAnswer = questioninexecutedexam.get(index).getCorrectAnswer() ;
		answer1.setStyle("-fx-background-color: white;");
		answer2.setStyle("-fx-background-color: white;");
		answer3.setStyle("-fx-background-color: white;");
		answer4.setStyle("-fx-background-color: white;");
		studentAnswer.setVisible(true) ; 	
		selectedAnswer.setVisible(true);
		switch(qustionAnswer) {
		case "1": 
			if(stdSelected.equals("1")) {
				answer1.setStyle("-fx-background-color: green;");
				correctAns = true ; 
			}
			correctRadioButton1.setSelected(true);
			break;
		case "2":
			if(stdSelected.equals("2")) {
				answer2.setStyle("-fx-background-color: green;");
				correctAns = true ; 
			}
			correctRadioButton2.setSelected(true);
			break;
		case "3":
			if(stdSelected.equals("3")) {
				answer3.setStyle("-fx-background-color: green;");
				correctAns = true ; 
			}
			correctRadioButton3.setSelected(true);
			break;
		case "4":
			if(stdSelected.equals("4")) {
				answer4.setStyle("-fx-background-color: green;");
				correctAns = true ; 
			}
			correctRadioButton4.setSelected(true);
			break;
		}
		
		switch(stdSelected) {
		case "1": 
			if(correctAns == false) 
				answer1.setStyle("-fx-background-color: red;");
			break;
		case "2":
			if(correctAns == false) 
				answer2.setStyle("-fx-background-color: red;");
			break;
		case "3":
			if(correctAns == false) 
				answer3.setStyle("-fx-background-color: red;");
			break;
		case "4":
			if(correctAns == false) 
				answer4.setStyle("-fx-background-color: red;");
			break;
		}
		selectedAnswer.setText(examAnswers.get(questioninexecutedexam.get(index).getId()).toString());
		}
	}

	@FXML
	private void previousQuestion(ActionEvent e) {
		if(copyFlag == false)
		addAnswerToHashMap();
		index--;
		setQuestion();
		if (index == 0) {
			prevBTN.setVisible(false);
		}
		nextBTN.setVisible(true);
	}

	@FXML
	private void finishExam(ActionEvent e) {
		if(copyFlag == false) {
		addAnswerToHashMap();
		String details[] = new String[2];
		details[0] = executedID;
		details[1] = Globals.getUser().getUsername();
		connect(this);
		messageToServer[0] = "finishExam";
		messageToServer[1] = details;
		messageToServer[2] = examAnswers;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
		try {
			closeScreen(e);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		openScreen("NewDesignHomeScreenStudent");
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
	public void dragOver(DragEvent e) {
		if (e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.ANY);
		}
	}
	
	@FXML
	public void dropFileToImage(DragEvent e) {
		List<File> file = e.getDragboard().getFiles();
		boolean wordFile = file.get(0).getAbsolutePath().contains(".docx");
		if (wordFile) {
			wordLogo.setVisible(true);
			uploadManualExamButton.setDisable(false);
		}
		else {
			wordLogo.setVisible(false);
			uploadManualExamButton.setDisable(true);
		}
	}
	
	@FXML
	public void uploadFileToServer(ActionEvent e) {
		
	}
		
}
