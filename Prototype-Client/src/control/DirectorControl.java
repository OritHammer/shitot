package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.application.Platform;

import entity.Course;

import entity.RequestForChangingTimeAllocated;
import entity.TeachingProfessionals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sun.nio.cs.HistoricallyNamedCharset;


public class DirectorControl extends UserControl implements Initializable {
	ObservableList<RequestForChangingTimeAllocated> addingTimeRequestsObservable = FXCollections.observableArrayList();
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
	// FAML Time Requests Table window
	@FXML
	private TableView<RequestForChangingTimeAllocated> requestsTable;
	@FXML
	private TableColumn<RequestForChangingTimeAllocated, String> examIDColumn;
	@FXML
	private TableColumn<RequestForChangingTimeAllocated, String> teacherNameColumn;
	@FXML
	private TableColumn<RequestForChangingTimeAllocated, String> timeAddedColumn;
	@FXML
	private Button showDetailsButton;

	// FAML Adding Time Requests window
	@FXML
	private TextField txtFATRexecutedExamId;
	@FXML
	private TextField txtFATRTeachName;
	@FXML
	private TextField txtFATRrequestId;
	@FXML
	private TextField txtFATRTimeAdded;
	@FXML
	private TextField txtFATRreasonAddingTime;
	@FXML
	private Button btnATRApprove;
	@FXML
	private Button btnATRreject;

	// FXML Statistic Reports
	@FXML
	private ComboBox<String> reportByComboBox;
	@FXML
	private ComboBox<String> chooseUserComboBox;
	@FXML
	private ComboBox<String> subjectsComboBox;
	@FXML
	private ComboBox<String> coursesComboBox;
	private String reportByChoose;

	@FXML
	private BarChart<?, ?> barChart;
	@FXML
	private TextField averageTextField;
	@FXML
	private TextField medianTextField;

	private static String requestId;
	
	private XYChart.Series histogram =null;
	
	// FXML System information

	/******************************************************************
	 * homePageButtons
	 **********************************************************/
	public void initialize(URL url, ResourceBundle rb) {
		if (pageLabel.getText().equals("Home screen"))
			userText1.setText(getMyUser().getFullname());
		else if (pageLabel.getText().contentEquals("requests")) {
			connect(this);
			messageToServer[0] = "getTimeRequestList";
			messageToServer[1] = null;
			messageToServer[2] = null;
			chat.handleMessageFromClientUI(messageToServer);// send the message to server
		} else if (pageLabel.getText().contentEquals("Adding time requests")) {
			connect(this);
			messageToServer[0] = "getTimeRequestDetails";
			messageToServer[1] = requestId;
			messageToServer[2] = null;
			chat.handleMessageFromClientUI(messageToServer);// send the message to server
		} else if (pageLabel.getText().contentEquals("Statistic report")) {
			ObservableList<String> observableList = FXCollections.observableArrayList();
			observableList.add("Student");
			observableList.add("Teacher");
			observableList.add("Course");
			reportByComboBox.setItems(observableList);
			chooseUserComboBox.setVisible(false);
			subjectsComboBox.setVisible(false);
			coursesComboBox.setVisible(false);
			histogram =new XYChart.Series<>();//initialize histogram
		} else if (pageLabel.getText().contentEquals("")) {// system information

		}

	}

	public void openTimeRequestTable(ActionEvent e) {
		Platform.runLater(() -> {
			final Node source = (Node) e.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			// stage.close();
			stage.close();
			openScreen("TimeRequestTable");
		});
	}

	public void openStatisticReport(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding homePage window
		openScreen("statisticReportDirector");
	}

	public void openSystemInformation(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding homePage window
		openScreen("systemInformationDirector");
	}

	private void openScreen(String screen) {// open the windows after login

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/directorBoundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			if (screen.equals("ErrorMessage")) {
				ErrorControl dController = loader.getController();
				dController.setBackwardScreen(stage.getScene());/* send the name to the controller */
				dController.setErrorMessage("ERROR");// send a the error to the alert we made
			}
			stage.setTitle(screen);
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}

	}

	private void openScreen(String screen, String message) {// for error message
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			ErrorControl tController = loader.getController();
			tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
			tController.setErrorMessage(message);// send a the error to the alert we made
			stage.setTitle("ErrorMessage");
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}
	}

	/* cancel button was pressed */
	public void backButtonPressed(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
		openScreen("HomeScreenDirector");
	}

	// ***********check the message that arrived from server**************//
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();// close the connection

			final Object[] msg = (Object[]) message;
			Platform.runLater(() -> {

				switch (msg[0].toString()) {
				case ("getTimeRequestList"): { /* get the subjects list from server */
					initAddingTimeRequests((ArrayList<RequestForChangingTimeAllocated>) msg[1]);

					break;
				}
				case ("getTimeRequestDetails"): { /* get the subjects list from server */
					initAddingTimeRequestDetails((RequestForChangingTimeAllocated) msg[1]);
					break;
				}
				case "getStudentsList":
				case "getTeachersList": {
					ObservableList<String> observableList = FXCollections.observableArrayList();
					for (String item : (ArrayList<String>) msg[1])
						observableList.add(item);
					chooseUserComboBox.setVisible(true);
					subjectsComboBox.setVisible(false);
					coursesComboBox.setVisible(false);
					if (chooseUserComboBox.getSelectionModel() != null)
						chooseUserComboBox.getSelectionModel().clearSelection();
					chooseUserComboBox.setItems(observableList);
					break;
				}
				case "getSubjects": {
					ObservableList<String> observableList = FXCollections.observableArrayList();
					for (TeachingProfessionals tp : (ArrayList<TeachingProfessionals>) msg[1]) {
						observableList.add(tp.getTp_id() + " - " + tp.getName());
						chooseUserComboBox.setVisible(false);
						subjectsComboBox.setVisible(true);
						coursesComboBox.getSelectionModel().clearSelection();
						subjectsComboBox.setItems(observableList);
					}
					break;
				}
				case "getCourses": {
					ObservableList<String> observableList = FXCollections.observableArrayList();
					for (Course c : (ArrayList<Course>) msg[1]) {
						observableList.add(c.getCourseID() + " - " + c.getName());
					}
					coursesComboBox.setVisible(true);
					coursesComboBox.setItems(observableList);
					break;
				}case "getReportByTeacher":{ 
					int sumGrades=0;
					int sumStudent=0;
					break;
				}
				case "getReportByCourse":{
					
					break;
				}case "getReportByStudent":{
					ArrayList<Integer> studentGradeList=(ArrayList<Integer>)msg[1];
					Collections.sort(studentGradeList);
					medianTextField.setText(" "+studentGradeList.get(studentGradeList.size()/2));
					int sum=0;
					int range0to54=0;
					int range55to64=0;
					int range65to74=0;
					int range75to84=0;
					int range85to94=0;
					int range95to100=0;
					for(Integer grade: studentGradeList) {
						sum+=grade;
						if (grade<55)//grade between0to54 
							range0to54++;
						else if(grade>54&&grade<65)//grade between55to64
							range55to64++;
						else if (grade>64&&grade<75)//grade between 65to74
							range65to74++;
						else if(grade>74&&grade<85)//grade between 75to84
							range75to84++;
						else if(grade>84&&grade<95)//grade between 85to94
							range85to94++;
						else if(grade>94)//grade between 95to100
							range95to100++;
					}
					averageTextField.setText(" "+sum/studentGradeList.size());
					//clear data from bar chart
					//histogram.getData().clear();
					//set values in the bar chart 
					histogram.getData().add(new XYChart.Data("0-54", range0to54));
					histogram.getData().add(new XYChart.Data("55-65", range55to64));
					histogram.getData().add(new XYChart.Data("65-75", range65to74));
					histogram.getData().add(new XYChart.Data("75-84", range75to84));
					histogram.getData().add(new XYChart.Data("85-94", range85to94));
					histogram.getData().add(new XYChart.Data("95-100", range95to100));
					Platform.runLater(() -> {barChart.getData().addAll(histogram);});
				}
 
				} 
			});
		} catch (NullPointerException e) {
			openScreen("ErrorMessage", "There is no request to confirm .");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/*******************************************************
	 * listeners on TimeRequestTable
	 ***********************************************************/
	public void showDetailsButtonPressed(ActionEvent e) {
		requestId = (requestsTable.getSelectionModel().getSelectedItems().get(0).getRequestID());
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding homePage window
		openScreen("addingTimeRequest");
	}

	@SuppressWarnings("unchecked") // showing list of request that waiting to answer
	public void initAddingTimeRequests(ArrayList<RequestForChangingTimeAllocated> requestsList)
			throws NullPointerException {
		Platform.runLater(() -> {
			for (RequestForChangingTimeAllocated i : requestsList) {
				addingTimeRequestsObservable.add(i);
			}
			if (requestsTable != null && requestsTable.getColumns() != null)
				requestsTable.getColumns().clear();
			requestsTable.setItems(addingTimeRequestsObservable);
			// display the id in the table view
			examIDColumn.setCellValueFactory(new PropertyValueFactory<>("IDexecutedExam"));

			teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));

			timeAddedColumn.setCellValueFactory(new PropertyValueFactory<>("timeAdded"));
			// requestsTable.getColumns().clear();
			requestsTable.getColumns().addAll(examIDColumn, teacherNameColumn, timeAddedColumn);
		});
	}

	// initialize the field in details of request
	public void initAddingTimeRequestDetails(RequestForChangingTimeAllocated request) {
		txtFATRTeachName.setText(request.getTeacherName());
		txtFATRTimeAdded.setText(request.getTimeAdded());
		txtFATRreasonAddingTime.setText(request.getReason());
		txtFATRrequestId.setText(request.getRequestID());
		txtFATRexecutedExamId.setText(request.getIDexecutedExam());
	}

	/*******************************************************
	 * listeners on addingTimeRequest
	 ***********************************************************/
	public void answerRequest(ActionEvent e) {
		String requestID = txtFATRrequestId.getText();
		connect(this);
		if (e.getSource() == btnATRApprove)// press on approved button
			messageToServer[0] = "SetStatusToApproved";
		else if (e.getSource() == btnATRreject)// press on reject button
			messageToServer[0] = "SetStatusToReject";
		messageToServer[1] = requestID;
		messageToServer[2] = null;
		this.openTimeRequestTable(e);
		chat.handleMessageFromClientUI(messageToServer);
	}

	/*******************************************************
	 * listeners on statistic Report
	 ***********************************************************/
	public void showListForChooseObject(ActionEvent e) {// display the list of student/courses/teachers
		reportByChoose = reportByComboBox.getValue();
		connect(this);
		switch (reportByChoose) {
		case "Student": {
			messageToServer[0] = "getStudentsList";
			messageToServer[1] = null;
			messageToServer[2] = null;
			break;
		}
		case "Teacher": {
			messageToServer[0] = "getTeachersList";
			messageToServer[1] = null;
			messageToServer[2] = null;
			break;
		}
		case "Course": {
			messageToServer[0] = "getSubjects";
			messageToServer[1] = null;
			messageToServer[2] = null;
			break;
		}
		}
		chat.handleMessageFromClientUI(messageToServer);
	}

	public void loadCourses(ActionEvent e) throws IOException {// load list of courses to combobox
		String subject = subjectsComboBox.getValue(); // get the subject code
		loadCourses("All", subject);
	}

	
	public void getReportUser(ActionEvent e) {// load Statistic details of user on window
		connect(this);
		String userName = chooseUserComboBox.getValue();
		if (reportByChoose == "Teacher")
			messageToServer[0] = "getReportByTeacher";
		else
			messageToServer[0] = "getReportByStudent";
		messageToServer[1] = userName;
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);
	} 
	public void getReportByCourseCode(ActionEvent e) {// load Statistic details of course on window
		connect(this);
		String courseName = coursesComboBox.getValue();
		 messageToServer[0]="getgetReportByCourse";
		 messageToServer[1]=courseName;
		 messageToServer[2] = null;
		 chat.handleMessageFromClientUI(messageToServer);
	}
}