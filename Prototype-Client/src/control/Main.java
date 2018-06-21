package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	private static Stage guiStage;

	public void start(Stage primaryStage) {
		try {
			/* display update question UI */
			// Parent root =

			Parent root = FXMLLoader.load(getClass().getResource("/boundary/LoginGui.fxml"));
			// Parent root =
			// FXMLLoader.load(getClass().getResource("/boundary/HomeScreenTeacher.fxml"));
			guiStage = primaryStage;
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
			guiStage.setTitle("Login");
			guiStage.setScene(scene);
			guiStage.getIcons().add(new Image("Owl.png"));
			guiStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		launch(args);
	}

	public static Stage getStage() {
		return guiStage;
	}

}