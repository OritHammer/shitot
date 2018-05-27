package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
		
	public void start(Stage primaryStage) {
		try {
			/*display update question UI*/
			//Parent root = FXMLLoader.load(getClass().getResource("/boundary/updateQuestion.fxml"));
			//Parent root = FXMLLoader.load(getClass().getResource("/boundary/LoginGui.fxml"));
			Parent root = FXMLLoader.load(getClass().getResource("/boundary/HomeScreenTeacher.fxml"));

			Scene scene = new Scene(root);
			primaryStage.setTitle("Login");
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
