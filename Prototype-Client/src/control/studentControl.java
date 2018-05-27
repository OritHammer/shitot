package control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class studentControl  extends UserControl implements Initializable{

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
	
	
	
	
	
	/***************Class Methods*******************************/
	public void  windowUp(String sName) {
		userNameLabel.setText(sName); // setting student name
		authorLabel.setText("Student"); // setting authorize
		//Calendar cal = Calendar.getInstance();
		//Date todayDate = cal.getTime();
		Date todayDate = new Date();
		dateLabel.setText(todayDate.toString());
		//DateFormat dateForm = new SimpleDateFormat("DD/MM/YY");
	//	String formatDateString = dateForm.format(todayDate);
	
		
	}
	
	
	
}
