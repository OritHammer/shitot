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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class S_HomeScreenController extends UserControl implements Initializable {
	/********************* GUI Variable declaration *************************/
	@FXML
	private  Label userNameLabel;
	@FXML
	private  Label authorLabel;
	@FXML
	private  Label dateLabel;
	@FXML
	private TextField newMsgTextField;

	private Object[] messageToServer = new Object[3];
	private static Boolean connectionFlag = false;
	private Calendar currentCalendar = Calendar.getInstance();
	private Date currentTime = currentCalendar.getTime();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
	/*************** Class Methods *******************************/
	public void initialize(URL url, ResourceBundle rb) {
		// make sure that after the first time there be no more connections
		setStudentAuthorAndDate();
		if (!connectionFlag) {
		connect(this);
		}
	}

	public void setStudentAuthorAndDate() {
		dateLabel.setText(dateFormat.format(currentTime));// Setting Current Date
		authorLabel.setText("Student");
	}

	public void logoutPressed(ActionEvent e) {
		messageToServer[0] = "logoutProcess";
		messageToServer[1] = null;
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}

	public void myGradesPressed(ActionEvent e) {
		connectionFlag = true ;
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
		openScreen("MyGradesScreen");
	}

	public void orderExamCopyPressed(ActionEvent e) {
		connectionFlag = true ;
		openScreen("OrderExamCopyScreen");
	}

	public void manualExamPressed(ActionEvent e) {
		connectionFlag = true ;
		openScreen("ManualExamScreen");
	}

	public void computrizeExamPressed(ActionEvent e) {
		connectionFlag = true ;
	}

	private void openScreen(String screen) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/studentBoundary/" + screen + ".fxml"));
			Scene scene = new Scene(root);
			Stage stage = Main.getStage();
			// stage.setTitle("Create question");
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
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
