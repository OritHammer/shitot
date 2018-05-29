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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StudentControl  extends UserControl implements Initializable{
/********************* Variable declaration *************************/
	//myDetails Win
	@FXML
	private Label userNameLabel ;
	@FXML 
	private Label authorLabel ;
	@FXML
	private Label dateLabel ;
	//MyGrades win	
	@FXML 
	private ComboBox<String> choosingSubject;
	@FXML
	private TableView<ExamCopy> examsTable;
	//OrderExam Win
	@FXML 
	private ComboBox<String> orderChoosingSubject; 
	@FXML 
	private ComboBox<String> chooseExam; 
	//ManualExam Win
	@FXML
	private TextField manualExamCodeField; 
	@FXML 
	private Label errorMsg ;
	//DoComputerizeExam 
	@FXML 
	private TextField examCodeField; 
	@FXML 
	private Label errorMsg2 ;
	@FXML
	private Tab myDetails;
	
	
	
	/************* Class Useful variables *************************/
	String subjectChoosen ;
	
	String[] messageToServer = new String[3];
	
	
	/***************Class Methods*******************************/
	
	//MyDetails Win
	 public void initialize(URL url, ResourceBundle rb) {
		/*
		userNameLabel.setText(sName); // setting student name
		authorLabel.setText("Student"); // setting authorize
		//Calendar cal = Calendar.getInstance();
		//Date todayDate = cal.getTime();
		Date todayDate = new Date();
		dateLabel.setText(todayDate.toString());
		//DateFormat dateForm = new SimpleDateFormat("DD/MM/YY");
	//	String formatDateString = dateForm.format(todayDate);	
	 */
	}
	/****************************************   MyGradesWindow   *******************************************************/
	/**************************Action Listeners***********************/
	public void initSubjects(ActionEvent e) {
		//ask for relevant subject from the server 
		messageToServer[0] = "getSubjects";
		messageToServer[1] = null;
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server	
	}
	
	public void chooseSubjectPressed(ActionEvent e) throws IOException  {
		choosingSubject.getSelectionModel().clearSelection();
		//clearing previous results  
		for(int i=0;i<examsTable.getItems().size();i++)
			examsTable.getItems().clear();
		//get the chosen subject by the user
		subjectChoosen = choosingSubject.getValue();
		//ask for executed exams for the chosen subject from the server
		messageToServer[0] = "getExecutedExams";
		messageToServer[1] = subjectChoosen;
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server	
	}
/******************** LoadingDate methods to GUI ********************/
	public void showExecutedExams(ArrayList<ExamCopy> executedExamsList) {
		try {
			//setting executed exam data on the table
			ObservableList<ExamCopy> observableList = FXCollections.observableArrayList(executedExamsList);
			examsTable.setItems(observableList);
		} catch (Exception e) {
		}	
	}	
	/****************************************   ManualExamWindow   *******************************************************/
	/**************************Action Listeners***********************/
	public void downloadPressed(ActionEvent e) {
		if(!manualExamCodeField.getText().isEmpty()) {
		messageToServer[0] ="checkExamCode";
		messageToServer[1] = manualExamCodeField.getText();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server	
		}
		else errorMsg.setVisible(true);
	}
	/******************** LoadingData methods to GUI ********************/
	//no data should appear , server should send the client an exam 
	
	/****************************************   DoComputerizeExam Window   *******************************************************/
	/**************************Action Listeners***********************/
	public void openExamPressed(ActionEvent e) {
		if(!examCodeField.getText().isEmpty()) {
		messageToServer[0] ="checkExamCode";
		messageToServer[1] = examCodeField.getText();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server	
		}
		else errorMsg2.setVisible(true);
	}
	
	
	
	/*************************** Handaling messages from the server *********************************/
	public void checkMessage( ) {
		
		
		
		
	}
}
