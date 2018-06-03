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
import javafx.stage.Stage;

public class ErrorControl implements Initializable  {
	private String backwardScreen;
	public void closeTheWindow(ActionEvent e)throws IOException{
		((Stage) ((Node)(e.getSource())).getScene().getWindow()).close();
		try {
		   Parent root = FXMLLoader.load(getClass().getResource("/boundary/"+backwardScreen+".fxml"));
           Scene scene = new Scene(root);
           Stage stage=Main.getStage();
           stage.setTitle("Create question");
           stage.setScene(scene);  
           stage.show();
         }catch(Exception exception) {
       	  System.out.println("Error in opening the page");
         }	
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
