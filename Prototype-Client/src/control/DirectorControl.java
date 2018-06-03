package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DirectorControl extends UserControl implements Initializable {

	/// HOME TAB
	@FXML
	private Label userText1;
	@FXML
	private Label pageLabel;
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
	
	@FXML
	private Button AddingTime;
	@FXML
	private Button StatisticReport;
	@FXML
	private Button SystemInformation;
	@FXML
	private Button backButton;
	
	
	
	//FXML Statistic Reports
	
	
	
	//FXML System information
	
	public void initialize(URL url, ResourceBundle rb) {
		 if(pageLabel.getText().equals("Home screen"));
			  userText1.setText(Globals.userName);
	}
	public void openAddingTimeRequest(ActionEvent e) {
		openScreen("addingTimeRequestDirector");
	}
	public void openStatisticReport(ActionEvent e) {
		openScreen("statisticReportDirector");
	}
	public void openSystemInformation(ActionEvent e) {
		openScreen("systemInformationDirector");
	}
	private void openScreen(String screen) {
		try{
				FXMLLoader loader=new FXMLLoader();
				loader.setLocation(getClass().getResource("/directorBoundary/"+screen+".fxml"));
	            Scene scene = new Scene(loader.load());
	            Stage stage=Main.getStage();
	            if(screen.equals("ErrorMessage")) {
	    			ErrorControl tController=loader.getController();
	    			tController.setBackwardScreen(stage.getScene());/*send the name to the controller*/
	    			tController.setErrorMessage("ERROR");//send a the error to the alert we made
	            }
	            stage.setTitle("Create question");
	            stage.setScene(scene);  
	            stage.show();
	          }catch(Exception exception) {
	        	  System.out.println("Error in opening the page");
	          }		
	}
	private void openScreen(String screen,String message) {
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
	/*back button was pressed*/
/*	public void backButtonPressed(ActionEvent e) throws IOException, SQLException {
	    final Node source = (Node) e.getSource();
	    Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();	
	    openScreen("HomeScreenDirector");
	}
	*/
}