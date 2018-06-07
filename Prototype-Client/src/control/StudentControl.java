package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import entity.ExamCopy;
import entity.ExamDetailsMessage;
import entity.TeachingProfessionals;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import studentControllers.S_myGradesScreenController;

public class StudentControl extends UserControl implements Initializable {
	private static Scene homeSc = null;
	private static Scene gradeSc = null;
	
	/********************* Variable declaration *************************/
	// *********for HomePage***********//
	@FXML
	private  Label userNameLabel;
	@FXML
	private  Label authorLabel;
	@FXML
	private  Label dateLabel;
	@FXML
	private TextField newMsgTextField;

	// *********for student do Exam***********//
	@FXML
	private TextField codeTextField;
	// *********for student see his grades***********//
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
	// *********for student ask for copy of his exam***********//

	// *******for student execute or download exam*********//
	private CheckBox correctExamCodeCB;

	// move to user
	private Calendar currentCalendar = Calendar.getInstance();
	private Date currentTime = currentCalendar.getTime();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
	FXMLLoader loader = new FXMLLoader();

	/*************** Class Methods *******************************/
	public void initialize(URL url, ResourceBundle rb) {
			//connect(this);
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
			if(screen.equals("LoginGui")) {
				loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));	
			}
			else loader.setLocation(getClass().getResource("/studentBoundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			if(screen.equals("NewDesignHomeScreenStudent") ) {
				StudentControl sControl = loader.getController();
				sControl.setStudentAuthor_Date_name();
			}
			if (screen.equals("ErrorMessage")) {
				ErrorControl tController = loader.getController();
				tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
				tController.setErrorMessage("ERROR");// send a the error to the alert we made
			}
			//stage.setTitle(screen);
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

	/********************* Student Order Copy 
	 * @throws IOException *************************/

	// for all windows
	@SuppressWarnings("unchecked")
	@Override
	public void checkMessage(Object message)  {
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
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		examGradesTable.getColumns().addAll(examCodeColumn,courseCodeColumn,gradeColumn,dateColumn);
	}

	public void orderExamPressed(ActionEvent e) {
		messageToServer[0] = "getExamsCopyByUserName";
		messageToServer[1] = examCodeCombo.getValue();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	public void downloadExamPressed() {

	}
}
