package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;


import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
  
public class UserControl implements Initializable {
	@FXML
	private TextField userName;
	//*******homeScreenLabel***********//
	@FXML
	private Label userNameLabel;
	@FXML
	private Label authorLabel;
	@FXML
	private Label dateLabel;
	//******login screen**********//
	@FXML
	private PasswordField password;
	@FXML
	private Label errorMsg;
	@FXML
	private ImageView errorImg;
	@FXML
	private ImageView errorImg1;
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
//class variables 
	//date and author variables
	private Calendar currentCalendar = Calendar.getInstance();
	private Date currentTime = currentCalendar.getTime();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
	//FMXL variables
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
	
	protected Object[] messageToServer = new Object[5];
	/* connections variables */
	static String ip;// server ip

	final public static int DEFAULT_PORT = 5555;

	private static User myUser = new User();
	
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
		ip = "localhost";
		// this.ip = sc.nextLine();
		sc.close(); 
		errorMsg.setVisible(false);
		errorImg.setVisible(false);
		errorImg1.setVisible(false);
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
					errorImg1.setVisible(true);
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
									getMyUser().setFullname(user.getFullname());
									getMyUser().setUsername(user.getUsername());
									
									home_page_scene = new Scene(home_page_parent);
									home_page_scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
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
									setMyUser(user); 
									getMyUser().setFullname(user.getFullname());
									getMyUser().setUsername(user.getUsername());
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
									getMyUser().setFullname(user.getFullname());
									getMyUser().setUsername(user.getUsername());
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
/*check if the details of user is in the system */
	public void loginPressed(ActionEvent e) throws IOException {
		  connect(this);
		  if (userName.getText().equals("") || password.getText().equals(""))
		  {
			errorMsg.setVisible(true);
		  	errorImg.setVisible(true);
			errorImg1.setVisible(true);
		  }
		  else {
		   messageToServer[0] = "checkUserDetails";
		   messageToServer[1] = userName.getText();
		   messageToServer[2] = password.getText();
		   messageToServer[4]=userName.getText();
		   chat.handleMessageFromClientUI(messageToServer);
		  }
		 }
	
	public void setUserText(String userNameFromDB) {/*set the user name text in the "hello user" text*/
		this.userNameFromDB=userNameFromDB;
		getMyUser().setFullname(userNameFromDB);
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
		else messageToServer[2] = getMyUser().getUsername();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

	public static User getMyUser() {
		return myUser;
	}

	public static void setMyUser(User myUser) {
		UserControl.myUser = myUser;
	}

	/****************functions To Use Of all users*******************************/
	public void openScreen(String boundary,String screen) {// open windows
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/"+boundary+"/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
	/*		if (screen.equals("ErrorMessage")) {
				ErrorControl dController = loader.getController();
				dController.setBackwardScreen(stage.getScene());// send the name to the controller 
				dController.setErrorMessage("ERROR");// send a the error to the alert we made
			} */
			stage.setTitle(screen);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in opening the page");
		}
	}

	public void errorMsg(String message) {// for error message
		new Alert(Alert.AlertType.ERROR, message).showAndWait();
	}
	public void closeScreen(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}
	public void logoutPressed(ActionEvent e) throws Exception, SQLException { // *** move to userControl
		connect(this);
		messageToServer[0] = "logoutProcess";
		messageToServer[1] = getMyUser().getUsername();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
		closeScreen(e);
	}
	public void setStudentAuthor_Date_name() {// *** move to userControl rename userDetails
		userNameLabel.setText(getMyUser().getFullname());
		dateLabel.setText(dateFormat.format(currentTime));// Setting Current Date
		authorLabel.setText(""+myUser.getRole());
	}

	
}
