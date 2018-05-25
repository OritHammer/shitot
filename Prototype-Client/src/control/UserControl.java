
package control;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class UserControl implements Initializable
{
	@FXML
	private TextField userName;
	@FXML
	private PasswordField password;
	@FXML
	private Label userNameError;
	@FXML
	private Label passwordError;
	@FXML
	private Label loginError;
	@FXML
	private Label noUser;
	@FXML
	private Button LoginBtn;
	
	@FXML
	private ImageView LoginButton;
	
	protected ChatClient chat;
	
	String[] messageToServer = new String[3];
	/* connections variables */
	protected String ip ;// server ip
	
	final public static int DEFAULT_PORT = 5555;
  
  /* this method connected between client and server */
	public void connect(UserControl user){
		try {
			chat = new ChatClient(ip, DEFAULT_PORT, user);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}
	

	public void initialize(URL arg0, ResourceBundle arg1) {
		Scanner sc = new Scanner(System.in);
		System.out.println("enter server ip");
		this.ip = sc.nextLine();
		sc.close();
		connect(this);
	}
	
	
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();// close the connection
			Object[] msg = (Object[]) message;
			if (msg[0].toString() == "checkUserDetails") {
				ArrayList<String> userDetails = (ArrayList<String>) msg[1];
				if (userDetails == null) {
					noUser.setDisable(false);
				} else {
					switch (userDetails.get(2).toLowerCase()) {
					case "teacher": {
						Parent teacherWin = FXMLLoader.load(getClass().getResource("/boundary/TeacherWindow.fxml"));
						Scene teacherGUI = new Scene(teacherWin);
						Main.window.setScene(teacherGUI);
						break;
					}
					case "student": {

						break;
					}
					case "director": {

						break;
					}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void loginPressed(ActionEvent e) throws IOException {

		if (userName.getText() == null || password.getText() == null)
			loginError.setDisable(false);
		else if (userName.getText().length() < 4)
			userNameError.setDisable(false);
		else if (password.getText().length() < 4)
			passwordError.setDisable(false);
		else {
			messageToServer[0] = "checkUserDetails";
			messageToServer[1] = userName.getText();
			messageToServer[2] = password.getText();
			chat.handleMessageFromClientUI(messageToServer);
		}

	}

}
