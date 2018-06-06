package control;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import entity.ExamCopy;
import entity.TeachingProfessionals;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import studentControllers.S_myGradesScreenController;

public class StudentControl extends UserControl implements Initializable {
	
	private Parent homeParent = null ;
	private Parent gradesParent = null ;
	private Parent orderCopyParent = null ;
	private Parent excecuteExamParent = null ;
	private static Scene homeSc = null;
	private static Scene gradeSc = null;
	private static Scene orderCopySc = null;
	private static Scene excecuteSc = null;
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
	
	

	// *********for student do Exam***********//
	@FXML
	private TextField codeTextField;
	// *********for student see his grades***********//
	@FXML
	private TableView<ExamCopy> cexamGradesTable;
	@FXML
	private TableColumn<ExamCopy, String> examCodeColumn;
	@FXML
	private TableColumn<String[], String> courseCodeColumn;
	@FXML
	private TableColumn<String[], String> gradeColumn;
	@FXML
	private TableColumn<String[], String> dateColumn;

	ObservableList<String[]> detailsList = FXCollections.observableArrayList();
	// *********for student ask for copy of his exam***********//
	@FXML
	private ComboBox<String> examCmb;
	
	// move to user
	private Calendar currentCalendar = Calendar.getInstance();
	private Date currentTime = currentCalendar.getTime();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
	FXMLLoader loader = new FXMLLoader();

	/*************** Class Methods *******************************/
	public void initialize(URL url, ResourceBundle rb) {
		initParentsAndScenes();
	}

	/********************* general Functions *************************/
	public void setStudentAuthor_Date_name() {// *** move to userControl rename userDetails
		userNameLabel.setText(Globals.getFullName());
		dateLabel.setText(dateFormat.format(currentTime));// Setting Current Date
		authorLabel.setText("Student");
	}

	public void logoutPressed(ActionEvent e) { // *** move to userControl
		messageToServer[0] = "logoutProcess";
		messageToServer[1] = Globals.getuserName();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	// the problem is with the fact that we create a new scene each time and we need
	// to prevent it in that way
	private void openScreen(String screen) {// *** move to userControl ?
		/*try {
			Parent root = FXMLLoader.load(getClass().getResource("/studentBoundary/" + screen + ".fxml"));
			if (gradeSc == null) {
				gradeSc = new Scene(root);
			}
			S_myGradesScreenController myGradeC = loader.getController();
			// Scene scene2 = new Scene(root);
			Stage stage = Main.getStage();
			// myGradeC.setHomePScene(homeSc);
			stage.setScene(gradeSc);
			stage.show();

			// stage.setScene(scene);
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error in opening the page");
		}*/
		Stage stage = Main.getStage();
		switch(screen){
		case "home" :
		{
			stage.setScene(homeSc);
		    break;
		}
		case "grade" : {
			stage.setScene(gradeSc);
			break;
		}
		case "copy" : 
		{
			stage.setScene(orderCopySc);
			break;
		}
		case "excecute" :{
			stage.setScene(excecuteSc);
			break;
		}
		}
		stage.show();
	}

	// ***
	public void goToHomePressed(ActionEvent e) throws Exception {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
		// Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader
				.load(getClass().getResource("/studentBoundary/NewDesignHomeScreenStudent.fxml").openStream());
		Stage MainStage = Main.getStage();
		Scene scene = new Scene(root);
		MainStage.setScene(scene);
		MainStage.show();
		// primaryStage.setScene(scene);
		// primaryStage.show();
	}

	public void setHomePScene(Scene home) {
		homeSc = home;
	}
public void initParentsAndScenes () {
	//this method make sure we use the first scene prevent calling server 
	try {
	FXMLLoader loaderFX=new FXMLLoader();
	loaderFX.setLocation(getClass().getResource("/studentBoundary/HomeScreenTeacher.fxml"));
	homeParent = loaderFX.load() ; // setting to home parent his FXML 
	homeSc = new Scene(homeParent);
	loaderFX.setLocation(getClass().getResource("/studentBoundary/MyGradesScreen.fxml"));
	gradesParent = loaderFX.load();
	gradeSc = new Scene(gradesParent);
	loaderFX.setLocation(getClass().getResource("/studentBoundary/OrderExamCopyScreen.fxml"));
	orderCopyParent = loaderFX.load();
	orderCopySc = new Scene(orderCopyParent);
	loaderFX.setLocation(getClass().getResource("/studentBoundary/ManualAndComputerizeExamScreen.fxml"));
	excecuteExamParent = loaderFX.load();
	excecuteSc = new Scene(excecuteExamParent);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
	/********************* Student Home Screen listeners *************************/
	public void myGradesPressed(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
		// S_myGradesScreenController myGradeC = loader.getController();
		// myGradeC.getGradesFromServer();
		//openScreen("MyGradesScreen");
		openScreen("grade");
	}

	public void orderExamCopyPressed(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
		//openScreen("OrderExamCopyScreen");
		openScreen("copy");
	}

	public void excecuteMorCExamPressed(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
		//openScreen("ManualAndComputerizeExamScreen");
		openScreen("excecute");
	}

	/********************* Student see his grades *************************/
	public void getGradesFromServer() {
		connect(this);
		messageToServer[0] = "getExamsByUserName";
		messageToServer[1] = Globals.getuserName();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	// for all windows
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();
			Object[] msgFromServer = (Object[]) message;
			switch (msgFromServer[0].toString()) {
				case "logoutProcess": {
					// need to think how to close this scene and go back to main scene !
					break;
				}
				case "getExamsByUserName": {
					showGradesOnTable((ArrayList<String[]>) msgFromServer[1]);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void showGradesOnTable(ArrayList<String[]> detailsFromS) {		}
	public void orderExamPressed(ActionEvent e) {
		
		messageToServer[0] = "getExamsCopyByUserName";
		messageToServer[1] = examCmb.getValue();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	} 

}
