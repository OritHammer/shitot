package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.sun.javafx.fxml.LoadListener;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;

import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import studentControllers.S_HomeScreenController;

public class UserControl implements Initializable {
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
	private Label errorMsg;
	@FXML
	private ImageView errorImg;
	@FXML
	private Label errorMsg1;
	@FXML
	private Label errorMsg2;
	@FXML
	private Label errorMsg3;
	@FXML
	private Button LoginBtn;

	@FXML
	private ImageView LoginButton;
	@FXML
	public Text userText;///user name
	@FXML
	private Label userText1;
	@FXML
	private javafx.scene.control.Button closeButton;

	private Parent home_page_parent;
	private Scene home_page_scene;
	static Thread th;
	protected String userNameFromDB;
	
	public void closeButtonAction(ActionEvent e) throws IOException {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	} 
	protected ComboBox<String> subjectsComboBox;
	protected ComboBox<String> coursesComboBox;
	protected ChatClient chat;

	protected Object[] messageToServer = new Object[3];
	/* connections variables */
	static String ip;// server ip

	final public static int DEFAULT_PORT = 5555;

	/* this method connected between client and server */
	public void connect(UserControl user) {
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
		// this.ip = "77.138.70.98";
		this.ip = "localhost";
		// this.ip = sc.nextLine();
		sc.close(); 
		errorMsg.setVisible(false);
		errorImg.setVisible(false);
		LoginBtn.setDefaultButton(true);
	}
 
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();// close the connection 
			Object[] msg = (Object[]) message;
			User user = (User) msg[1];
			if (user==null) {
				Platform.runLater(()->	{
					errorMsg.setVisible(true);
					errorImg.setVisible(true);
				});
			
				return;
			}
			if (msg[0].toString().equals("checkUserDetails")) {
				if (user != null) {
					switch (user.getRole().toLowerCase()) {
					case "teacher": {
						// Platform.exit();

						System.out.println("teacher screen request");
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									FXMLLoader loader=new FXMLLoader();
									loader.setLocation(getClass().getResource("/boundary/HomeScreenTeacher.fxml"));
									home_page_parent = loader.load();
									TeacherControl tController=loader.getController();
									String userName=user.getFullname().toLowerCase();
									tController.setUserText(userName);/*send the name to the controller*/
									Globals.setFullName(user.getFullname());
									Globals.setuserName(user.getUsername());
									home_page_scene = new Scene(home_page_parent);
									Main.getStage().setTitle("HomeScreenTeacher");
									Main.getStage().setScene(home_page_scene);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						});
						break;
					}
					case "student": {
						System.out.println("student screen request");
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {

									FXMLLoader loader=new FXMLLoader();
									loader.setLocation(getClass().getResource("/studentBoundary/NewDesignHomeScreenStudent.fxml"));
									home_page_parent = loader.load();
									StudentControl sController=loader.getController();
									Globals.setUser(user); 
									Globals.setFullName(user.getFullname());
									Globals.setuserName(user.getUsername());
									sController.setStudentAuthor_Date_name();/*send the name to the controller*/
									home_page_scene = new Scene(home_page_parent);
									//	sController.setHomePScene(home_page_scene);
									Main.getStage().setTitle("HomeScreenStudent");
									Main.getStage().setScene(home_page_scene);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		 					}
						});
						break;
					}
					case "director": {
						System.out.println("director screen request");
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									FXMLLoader loader=new FXMLLoader();
									loader.setLocation(getClass().getResource("/directorBoundary/HomeScreenDirector.fxml"));
									
									home_page_parent = loader.load();
									Globals.setFullName(user.getFullname());
									Globals.setuserName(user.getUsername());
									DirectorControl dController=loader.getController();
									String userName=user.getFullname().toLowerCase();/*get the name of the user*/
									dController.setUserText(userName);/*send the name to the controller*/
									home_page_scene = new Scene(home_page_parent);
									Main.getStage().setTitle("HomeScreenDirector");
									Main.getStage().setScene(home_page_scene);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
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
		/*errorMsg.setVisible(false);
		errorMsg1.setVisible(false);
		errorMsg2.setVisible(false);
		errorMsg3.setVisible(false);*/
		  connect(this);
		  if (userName.getText().equals("") && password.getText().equals(""))
		  {
			  errorMsg.setVisible(true);
		  	errorImg.setVisible(true);
		  }
		  else if(userName.getText().equals(""))
		  {
			  errorMsg.setVisible(true);
			errorImg.setVisible(true);
		  }
			else if(password.getText().equals(""))
			{
				errorMsg.setVisible(true);
				errorImg.setVisible(true);
			}
				else {
		   messageToServer[0] = "checkUserDetails";
		   messageToServer[1] = userName.getText();
		   messageToServer[2] = password.getText();
		   chat.handleMessageFromClientUI(messageToServer);
		  }
		 }
	
	public void setUserText(String userNameFromDB) {/*set the user name text in the "hello user" text*/
		this.userNameFromDB=userNameFromDB;
		Globals.setFullName(userNameFromDB);
		if(this instanceof DirectorControl) {
			 userText1.setText(userNameFromDB);
		return;
		}
		 userText.setText(userNameFromDB);
	} 
	//functions relevant for all users 
	public void loadCourses(String typeList,String subject) throws IOException {
		/* ask for the courses name */
		if (subject == null)
			return;
		String[] subjectSubString = subject.split("-");
		connect(this); // connecting to server
		messageToServer[0] = "getCourses";
		messageToServer[1] = subjectSubString[0].trim();
		if(typeList=="All")
			messageToServer[2] = null;
		else messageToServer[2] = Globals.getuserName();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

}
