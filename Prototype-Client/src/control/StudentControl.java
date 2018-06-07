package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import entity.ExamDetailsMessage;
import entity.Question;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentControl extends UserControl implements Initializable {
	private static Scene homeSc = null;
	private static Scene gradeSc = null;
	private static ArrayList<Question> questioninexecutedexam;
	private int index=0;

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
	// move to user
	private Calendar currentCalendar = Calendar.getInstance();
	private Date currentTime = currentCalendar.getTime();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
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
	private ComboBox<String> examCodeCombo;

	ObservableList<ExamDetailsMessage> detailsList = FXCollections.observableArrayList();

	// *******for student execute or download exam*********//
	@FXML
	private TextField codeTextField;
	@FXML
	private CheckBox correctExamCodeCB;
	@FXML
	private TextField userIDTextField;
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
	private Button nextBTN;
	/************************ Class Methods *************************/
	public void initialize(URL url, ResourceBundle rb) {
		// connect(this);
		if (pageLabel.getText().equals("Perform exam")) {
			nextQuestion(null);
		}

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

	/********************* Student see his grades *************************/
	public void getGradesFromServer() {
		connect(this);
		messageToServer[0] = "getExamsByUserName";
		messageToServer[1] = Globals.getuserName();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	public void refreshTable(ActionEvent e) {
		getGradesFromServer();
	}

	/*********************
	 * Student Order Copy
	 * 
	 * @throws IOException
	 *************************/
	public void orderExamPressed(ActionEvent e) {
		messageToServer[0] = "getExamsCopyByUserName";
		messageToServer[1] = examCodeCombo.getValue();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	public void downloadExamPressed() {

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
		String executedID = codeTextField.getText();
		connect(this); // connecting to server
		messageToServer[0] = "checkExecutedExam";
		messageToServer[1] = executedID;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

	/************************* checking message ***********************************/
	// for all windows
	@SuppressWarnings("unchecked")
	@Override
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();
			Object[] msgFromServer = (Object[]) message;
			switch (msgFromServer[0].toString()) {
			case "logoutProcess": {
				openScreen("LoginGui");
				break;
			}
			case "getExamsByUserName": {
				showGradesOnTable((ArrayList<ExamDetailsMessage>) msgFromServer[1]);
			}
			case "checkExecutedExam": {
				checkExecutedExam((Object[]) msgFromServer[1]);
			}
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
	private void checkExecutedExam(Object[] msgFromServer) {
		if (msgFromServer == null) {
			// openScreen("ErrorMessage","Exam Locked or not defined");
			return;
		}
		String type = (String) msgFromServer[1];
		if (type.equals("manual")) {
			// We Need To Build This Functionality !!!!!!!
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					questioninexecutedexam = (ArrayList<Question>) msgFromServer[0];
					openScreen("ComputerizedExam");
				}
			});
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showGradesOnTable(ArrayList<ExamDetailsMessage> detailsFromS) {
		for (ExamDetailsMessage edM : detailsFromS) {
			detailsList.add(edM);
		}

		examGradesTable.setItems(detailsList);
		examCodeColumn.setCellValueFactory(new PropertyValueFactory("examID"));
		dateColumn.setCellValueFactory(new PropertyValueFactory("examDate"));
		gradeColumn.setCellValueFactory(new PropertyValueFactory("examGrade"));
		courseCodeColumn.setCellValueFactory(new PropertyValueFactory("examCourse"));
		examGradesTable.getColumns().removeAll();
		examGradesTable.getColumns().addAll(examCodeColumn, courseCodeColumn, gradeColumn, dateColumn);
	}

	/************************ Student performing exam *************/
	@FXML
	private void nextQuestion(ActionEvent e) {
		questionContent.setText(questioninexecutedexam.get(index).getQuestionContent());
		answer1.setText(questioninexecutedexam.get(index).getAnswer1());
		answer2.setText(questioninexecutedexam.get(index).getAnswer2());
		answer3.setText(questioninexecutedexam.get(index).getAnswer3());
		answer4.setText(questioninexecutedexam.get(index).getAnswer4());
		index++;
		if(index==questioninexecutedexam.size()) {
			nextBTN.setVisible(false);
		}
	}

}
