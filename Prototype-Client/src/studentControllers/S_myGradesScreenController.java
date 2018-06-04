package studentControllers;

import control.Globals;
import control.Main;
import control.UserControl;
import entity.ExamCopy;//need to check if grades is based on exam copy or ex-exam


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
  
public class S_myGradesScreenController extends UserControl {
	/********************* GUI Variable declaration *************************/
	
	@FXML
	private TableView<ExamCopy> cexamGradesTable;
	@FXML
	private TableColumn<ExamCopy, String> examCodeColumn;
	@FXML
	private TableColumn<ExamCopy, String> courseCodeColumn;
	@FXML 
	private TableColumn<ExamCopy, Float> gradeColumn;

	private Object[] messageToServer = new Object[3];
	
	private static Scene homeSc ; 
 
	/*************** Class Methods *******************************/
	// both of those methods should be for all screens
	public void refreshTable(ActionEvent e) {
		getGradesFromServer();
	}

	public void goToHomePressed(ActionEvent e) throws Exception {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary Window
		//Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/studentBoundary/NewDesignHomeScreenStudent.fxml").openStream());
		Stage MainStage = Main.getStage();
		Scene scene = new Scene(root);
		MainStage.setScene(scene);
		MainStage.show();
		//primaryStage.setScene(scene);
		//primaryStage.show();
	}
	public void setHomePScene(Scene home) {
		homeSc = home; 
	}
	public void getGradesFromServer () {
		messageToServer[0] = "getExamsByUserName";
		messageToServer[1] = Globals.getuserName();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}
	public void checkMessage(Object message) {
		Object[] msgFromServer = (Object[])message ;
		try {
			switch((String)msgFromServer[0]) {
			case "getExamsByUserName":
			{
				
			}
			
			}

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	/**********************************************************HANDLEMESSAGE*********************************************/

	public void showGradesOnTable() {
		
	}
}
