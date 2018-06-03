package studentControllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import control.Main;
import control.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class S_HomeScreenController extends UserControl implements Initializable {
	/*********************GUI Variable declaration *************************/
	@FXML
	private Label userNameLabel;
	@FXML
	private Label authorLabel;
	@FXML
	private Label dateLabel;
	
	private Object[] messageToServer=new Object[3];
	/*************** Class Methods *******************************/
	public void initialize(URL url, ResourceBundle rb) {
		// ask for relevant subject from the server
		connect(this);
	}
	public void setStudentAuthorAndDate() {
		Calendar currentCalendar = Calendar.getInstance();
		Date currentTime = currentCalendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
		dateLabel.setText(dateFormat.format(currentTime));// Setting Current Date
		authorLabel.setText("Student");
	}
	public void logoutPressed(ActionEvent e) {
		messageToServer[0]="logoutProcess";
		messageToServer[1] = null;
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);//send the message to server
	}
	public void myGradesPressed(ActionEvent e) {
		openScreen("MyGradesScreen");
	}  
public void orderExamCopyPressed(ActionEvent e) {
	openScreen("OrderExamCopyScreen");
	}
public void manualExamPressed(ActionEvent e) {
	
}
public void computrizeExamPressed(ActionEvent e) {
	
}

	
	
	private void openScreen(String screen) {
		try{ 
			   Parent root = FXMLLoader.load(getClass().getResource("/studentBoundary/"+screen+".fxml"));
	            Scene scene = new Scene(root);
	            Stage stage=Main.getStage();
	            //stage.setTitle("Create question");
	            stage.setScene(scene);  
	            stage.show();
	          }catch(Exception exception) {
	        	  System.out.println("Error in opening the page");
	          }		
}
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			
			
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		}
}
