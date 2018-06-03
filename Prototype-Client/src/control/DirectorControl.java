package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DirectorControl extends UserControl implements Initializable {

	/// HOME TAB

	
	//FAML Adding Time Requests TAB
	@FXML 
	private ComboBox<String> cmbATRChooseExecutedExam;
	@FXML 
	private TextField txtFATRCourseName;
	@FXML 
	private TextField txtFATRTeachName;
	@FXML 
	private TextField txtFATRTimeAdded;
	@FXML 
	private TextField txtFATRreasonAddingTime;
	@FXML
	private Button btnATRApprove;
	@FXML
	private Button btnATRreject;
	//FXML Statistic Reports
	
	
	
	//FXML System information
	
	public void initialize(URL url, ResourceBundle rb) {
		//userText.setText(userNameFromDB);
	}
	
	
	
}