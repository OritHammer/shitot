package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorControl implements Initializable  {
	
	private Scene backwardScreen;
	@FXML
	private Text errorText;
	
	public void closeTheWindow(ActionEvent e)throws IOException{
		((Stage) ((Node)(e.getSource())).getScene().getWindow()).close();
		try {
           Stage stage=Main.getStage();
           stage.setTitle("Create question");
           stage.setScene(backwardScreen);  
           stage.show();
         }catch(Exception exception) {
       	  System.out.println("Error in opening the page");
         }	
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	public Scene getBackwardScreen() {
		return backwardScreen;
	}
	public void setBackwardScreen(Scene backwardScreen) {
		this.backwardScreen = backwardScreen;
	}
	public void setErrorMessage(String errorMessage) {
		errorText.setText(errorMessage);
	}

}
