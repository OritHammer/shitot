package studentControllers;

import java.io.IOException;

import control.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
 
public class S_orderExamCopyController extends UserControl{
	/********************* GUI Variable declaration *************************/
	@FXML
	private ComboBox<String> examCmb;


	private Object[] messageToServer = new Object[3];
	
	/***************  Class Methods *******************************/
	
	public void chooseExamPressed(ActionEvent e) {
	
	}
	public void orderExamPressed(ActionEvent e) {
		
		messageToServer[0] = "getExamsCopyByUserName";
		messageToServer[1] = examCmb.getValue();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}
public void goToHomePressed(ActionEvent e) throws Exception {
	((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
	Stage primaryStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	Pane root = loader.load(getClass().getResource("/studentBoundary/NewDesignHomeScreenStudent.fxml").openStream());
	Scene scene = new Scene(root);
	primaryStage.setScene(scene);
	primaryStage.show();
	}
		
}
