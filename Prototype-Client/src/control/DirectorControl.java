package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.TeachingProfessionals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DirectorControl extends UserControl implements Initializable {

	/// HOME TAB
	@FXML
	private Label userText1;
	@FXML
	private Label pageLabel;
	@FXML
	private Button AddingTime;
	@FXML
	private Button StatisticReport;
	@FXML
	private Button SystemInformation;
	@FXML
	private Button backButton;
	
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
/*********************************************************************homePageButtons**********************************************************/
	public void initialize(URL url, ResourceBundle rb) {
		 if(pageLabel.getText().equals("Home screen"))
			  userText1.setText(Globals.getuserName());
	}
	public void openAddingTimeRequest(ActionEvent e) {
		((Node)e.getSource()).getScene().getWindow().hide(); //hiding homePage window
		openScreen("addingTimeRequestDirector");
		messageToServer[0] = "getExecutedExamCodeList";
		messageToServer[1] = null;
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}
	public void openStatisticReport(ActionEvent e) {
		((Node)e.getSource()).getScene().getWindow().hide(); //hiding homePage window
		openScreen("statisticReportDirector");
	}
	public void openSystemInformation(ActionEvent e) {
		((Node)e.getSource()).getScene().getWindow().hide(); //hiding homePage window
		openScreen("systemInformationDirector");
	}
	private void openScreen(String screen) {//open the windows after login
		try{
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/directorBoundary/"+screen+".fxml").openStream());///////////*נצטרך לשנות לבונדרי רגיל או להתאים לארור מסג'*/////////////
			
			Scene scene = new Scene(root);			
			 if(screen.equals("ErrorMessage")) {
	    			ErrorControl tController=loader.getController();
	    			tController.setBackwardScreen(primaryStage.getScene());// send the name to the controller
	    			tController.setErrorMessage("ERROR"); //send a the error to the alert we made
	            }
	        primaryStage.setTitle(screen);
	   //     primaryStage.getIcons().add(new Image("/Prototype-Client/Copywriting-Master-Class-Owl-200x175.png"));
			primaryStage.setScene(scene);		
			primaryStage.show();
			
	
	          }catch(Exception exception) {
	        	  exception.printStackTrace();
	        	  System.out.println("Error in opening the page");
	          }		
	}
	private void openScreen(String screen,String message) {// for error message
		try{
				FXMLLoader loader=new FXMLLoader();
				loader.setLocation(getClass().getResource("/directorBoundary/"+screen+".fxml"));
	            Scene scene = new Scene(loader.load());
	            Stage stage=Main.getStage();
	    		ErrorControl tController=loader.getController();
	    		tController.setBackwardScreen(stage.getScene());/*send the name to the controller*/
	    		tController.setErrorMessage(message);//send a the error to the alert we made
	            stage.setTitle("Create question");
	            stage.setScene(scene);  
	            stage.show();
	          }catch(Exception exception) {
	        	  System.out.println("Error in opening the page");
	          }		
	}
	/*cancel button was pressed*/
	public void backButtonPressed(ActionEvent e) throws IOException, SQLException {
	    final Node source = (Node) e.getSource();
	    Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();	
	    openScreen("HomeScreenDirector");
	}
///////********************************************check the message that arrived from server***********************************************************/
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();// close the connection

			Object[] msg = (Object[]) message;
			/*	msg[1]=new ArrayList<String>();
				((ArrayList<String>)msg[1]).add("afdvdfv");
				((ArrayList<String>)msg[1]).add("bhhgk");            */
			switch (msg[0].toString()) {
			case ("getTimeRequestCodeList"): { /* get the subjects list from server */
				initExecutedExamList((ArrayList<String>) msg[1]);
				break;
			}
			case ("bkjb"):{
				
				break;
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	/*******************************************************listeners on addingTimeRequestDirector***********************************************************/
	/**/
	public void initExecutedExamList(ArrayList<String> ExecutedExamList){
		ObservableList<String> observableList = FXCollections.observableArrayList(ExecutedExamList);
		cmbATRChooseExecutedExam.setItems(observableList);
		
		//questionsComboBox.getItems().remove(questionsComboBox.getValue());//removing from combobox
	}
}