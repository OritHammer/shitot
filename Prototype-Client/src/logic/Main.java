package logic;



import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	private ArrayList<Question> questions;
	public void start(Stage primaryStage) {
		try {
		questions=new ArrayList<Question>(); //init array of questions
		Parent root = FXMLLoader.load(getClass().getResource("/gui/updateQ.fxml"));
		Scene scene = new Scene(root);
//		scene.getStylesheets().add(getClass().getResource("/gui/updateQ.css").toExternalForm());
		primaryStage.setTitle("Update question");
		primaryStage.setScene(scene);
		primaryStage.show();	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
