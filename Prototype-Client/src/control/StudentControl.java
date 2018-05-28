package control;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StudentControl  extends UserControl implements Initializable{
/********************* Variable declaration *************************/
	@FXML
	private Label userNameLabel ;
	@FXML 
	private Label authorLabel ;
	@FXML
	private Label dateLabel ;
	@FXML
	private Label newUpdateLable ;
	
		
	@FXML 
	private ComboBox<String> choosingSubject;
	@FXML 
	private ComboBox<String> orderChoosingSubject; 
	@FXML 
	private ComboBox<String> chooseExam; 
	
	@FXML 
	private TextField manualExamCodeField; 
	@FXML 
	private TextField examCodeField; 
	
	
	@FXML
	private TableView<String> examsTable;
	/************* Class Useful variables *************************/
	String subjectChoosen ;
	String[] messageToServer = new String[3];
	
	
	/***************Class Methods*******************************/
	@FXML public void  showDetails(String sName) {
		userNameLabel.setText(sName); // setting student name
		authorLabel.setText("Student"); // setting authorize
		//Calendar cal = Calendar.getInstance();
		//Date todayDate = cal.getTime();
		Date todayDate = new Date();
		dateLabel.setText(todayDate.toString());
		//DateFormat dateForm = new SimpleDateFormat("DD/MM/YY");
	//	String formatDateString = dateForm.format(todayDate);	
	}
	/******************************MyGradesWindow*********************************/
	/**************************Action Listeners***********************/
	public void showGradesOpened(ActionEvent e) throws IOException{
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
	
	
	
}
