package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;

import entity.Course;
import entity.ExecutedExam;
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
import javafx.scene.chart.XYChart.Series;
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
	private TableColumn<RequestForChangingTimeAllocated, String> StatusColumn;
	@FXML
	private Button showDetailsButton;

	// FAML Adding Time Requests window
	@FXML
	private TextField txtFATRexecutedExamId;
	@FXML
	private TextField txtFATRTeachName;
	@FXML
	private TextField txtStatusInApproveReq;
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
	@FXML
	private Label chooseCourseLabel;
	@FXML
	private Label chooseSubjectLabel;
	@FXML
	private Label chooseUserNameLabel;
	@FXML
	private Button btnRefresh;

	private static String requestId;
	@SuppressWarnings("rawtypes")
	private XYChart.Series histogram = null;
	private float[] sumGradeRanges;
	private ArrayList<ExecutedExam> GradeList;
	private ArrayList<Integer> studentGradeList;
	private Boolean refreshPressed = false;
	
	// FXML System information
	@FXML
	private JFXButton btnQuestionStock;
	@FXML
	private JFXButton btnExamStock;

	/******************************************************************
	 * homePageButtons
	 **********************************************************/
	public void initialize(URL url, ResourceBundle rb) {
		messageToServer[4] = getMyUser().getUsername();
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
			initWindow();
			refreshPress();
		} else if (pageLabel.getText().contentEquals("")) {// system information

		}

	}

	public void initWindow() {
		// init the labels and comboBox
		if (histogram == null)
			histogram = new XYChart.Series<>();// initialize histogram
		else
			histogram.getData().clear();
		if (sumGradeRanges == null)
			sumGradeRanges = new float[10];
		for (int i = 0; i < 10; i++) {
			sumGradeRanges[i] = 0;
		}
	}

	// open window of buttons in home page
	public void openTimeRequestTable(ActionEvent e) {
		Platform.runLater(() -> {
			final Node source = (Node) e.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			// stage.close();
			stage.close();
			openScreen("directorBoundary", "TimeRequestTable");
		});
	}

	public void openStatisticReport(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding homePage window
		openScreen("directorBoundary", "statisticReportDirector");
	}

	public void openSystemInformation(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding homePage window
		openScreen("directorBoundary", "systemInformationDirector");
	}

	/* cancel button was pressed */
	public void backButtonPressed(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
		openScreen("directorBoundary", "HomeScreenDirector");
	}

	// ***********check the message that arrived from server**************//
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			final Object[] msg = (Object[]) message;
			if (messagesRead.contains((int) msg[5])) {
				return;
			}
			messagesRead.add((int) msg[5]);
			if (msg[4].equals(getMyUser().getUsername())) {
				chat.closeConnection();// close the connection
				/*
				 * if (refreshPressed) refreshPressed = false; else {
				 */

				Platform.runLater(() -> {
					try {
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
							if (chooseUserComboBox.getSelectionModel() != null)
								chooseUserComboBox.getSelectionModel().clearSelection();
							chooseUserComboBox.setItems(observableList);
							break;
						}
						case "getSubjects": {
							ObservableList<String> observableList = FXCollections.observableArrayList();
							for (TeachingProfessionals tp : (ArrayList<TeachingProfessionals>) msg[1]) {
								observableList.add(tp.getTp_id() + " - " + tp.getName());
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
						}
						case "getReportByCourse":
						case "getReportByTeacher": {
							if (GradeList != null)
								GradeList.clear();
							GradeList = (ArrayList<ExecutedExam>) msg[1];
							float average = 0;
							int sumStudent = 0;
							for (ExecutedExam executedExam : GradeList) {
								average +=executedExam.getAverage();
							}
							if (GradeList.size() > 0)
								averageTextField.setText(" " + (average / (float)GradeList.size()));
							Collections.sort(GradeList);
							if (GradeList.size() % 2 == 0)
								medianTextField.setText(" " + GradeList.get((GradeList.size() / 2) - 1).getMedian());
							else
								medianTextField.setText(" " + GradeList.get(GradeList.size() / 2).getMedian());
							ShowHistogramInBarChart();
							break;
						}
						case "getReportByStudent": {
							if (studentGradeList != null)
								studentGradeList.clear();
							studentGradeList = (ArrayList<Integer>) msg[1];
							Collections.sort(studentGradeList);

							float sum = 0;
							for (Integer grade : studentGradeList) {
								sum += grade;
								if (grade >= 0 && grade <= 9)// grade between0to54
									sumGradeRanges[0]++;
								else if (grade >= 10 && grade <= 19)// grade between55to64
									sumGradeRanges[1]++;
								else if (grade >= 20 && grade <= 29)// grade between 65to74
									sumGradeRanges[2]++;
								else if (grade >= 30 && grade <= 39)// grade between 75to84
									sumGradeRanges[3]++;
								else if (grade >= 40 && grade <= 49)// grade between 85to94
									sumGradeRanges[4]++;
								else if (grade >= 50 && grade <= 59)// grade between 95to100
									sumGradeRanges[5]++;
								else if (grade >= 60 && grade <= 69)// grade between0to54
									sumGradeRanges[6]++;
								else if (grade >= 70 && grade <= 79)// grade between0to54
									sumGradeRanges[70]++;
								else if (grade >= 80 && grade <= 89)// grade between0to54
									sumGradeRanges[8]++;
								else if (grade >= 90 && grade <= 100)// grade between0to54
									sumGradeRanges[9]++;
							}
							if (studentGradeList.size() % 2 == 0)
								medianTextField.setText(" " + studentGradeList.get((studentGradeList.size() / 2) - 1));
							else
								medianTextField.setText(" " + studentGradeList.get(studentGradeList.size() / 2));
							if (studentGradeList.size() > 0)
								averageTextField.setText(" " + sum / studentGradeList.size());
							ShowHistogramInBarChart();
						}

						}
					} catch (IndexOutOfBoundsException e) {
						errorMsg("There is no exams");
					}
				});
			}
		} catch (NullPointerException e) {
			errorMsg("There is no request to confirm .");
		} catch (IOException e) {

			e.printStackTrace();
		} /*
			 * catch (ArrayIndexOutOfBoundsException e) { errorMsg("There is no exams"); }
			 */
	}

	/*******************************************************
	 * listeners on TimeRequestTable
	 ***********************************************************/
	public void showDetailsButtonPressed(ActionEvent e) {
		requestId = (requestsTable.getSelectionModel().getSelectedItems().get(0).getRequestID());
		((Node) e.getSource()).getScene().getWindow().hide(); // hiding homePage window
		openScreen("directorBoundary", "addingTimeRequest");
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
			StatusColumn.setCellValueFactory(new PropertyValueFactory<>("menagerApprove"));
			timeAddedColumn.setCellValueFactory(new PropertyValueFactory<>("timeAdded"));
			// requestsTable.getColumns().clear();
			requestsTable.getColumns().addAll(examIDColumn, teacherNameColumn, timeAddedColumn, StatusColumn);
		});
	}

	// initialize the field in details of request
	public void initAddingTimeRequestDetails(RequestForChangingTimeAllocated request) {
		try {
			txtFATRTeachName.setText(request.getTeacherName());

			txtFATRTimeAdded.setText(request.getTimeAdded());
			txtFATRreasonAddingTime.setText(request.getReason());
			txtFATRrequestId.setText(request.getRequestID());
			txtStatusInApproveReq.setText(request.getMenagerApprove());
			txtFATRexecutedExamId.setText(request.getIDexecutedExam());
			if (!request.getMenagerApprove().equals("waiting")) {
				btnATRApprove.setDisable(true);
				btnATRreject.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************************************************
	 * listeners on addingTimeRequest
	 ***********************************************************/
	@FXML
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
	 *********** listeners on statistic Report**************
	 *******************************************************/
	@FXML
	public void showListForChooseObject(ActionEvent e) throws Exception {// display the list of student/courses/teachers
		reportByChoose = reportByComboBox.getValue();
		if (reportByChoose != null) {
			reportByComboBox.setDisable(true);
			connect(this);
			switch (reportByChoose) {
			case "Student": {
				chooseUserNameLabel.setVisible(true);
				chooseUserComboBox.setDisable(false);
				messageToServer[0] = "getStudentsList";
				messageToServer[1] = null;
				messageToServer[2] = null;
				break;
			}
			case "Teacher": {
				chooseUserNameLabel.setVisible(true);
				chooseUserComboBox.setDisable(false);// hide user comboBox
				messageToServer[0] = "getTeachersList";
				messageToServer[1] = null;
				messageToServer[2] = null;
				break;
			}
			case "Course": {
				chooseUserComboBox.setVisible(false);// hide user comboBox
				chooseSubjectLabel.setVisible(true);// show subject label
				subjectsComboBox.setDisable(false);// disable user comboBox
				messageToServer[0] = "getSubjects";
				messageToServer[1] = null;
				messageToServer[2] = null;
				break;
			}
			}
			chat.handleMessageFromClientUI(messageToServer);
		}
	}

	@FXML
	public void loadCourses(ActionEvent e) throws IOException {// load list of courses to combobox
		String subject = subjectsComboBox.getValue(); // get the subject code
		if (subject != null) {
			chooseCourseLabel.setVisible(true);
			coursesComboBox.setDisable(false);
			subjectsComboBox.setDisable(true);
			loadCourses("All", subject);
		}
	}

	@FXML
	public void getReportUser(ActionEvent e) {// load Statistic details of user on window
		String userName = chooseUserComboBox.getValue();
		if (userName != null) {
			connect(this);
			chooseUserComboBox.setDisable(true);
			if (reportByChoose.equals("Teacher"))
				messageToServer[0] = "getReportByTeacher";
			else
				messageToServer[0] = "getReportByStudent";
			messageToServer[1] = userName;
			messageToServer[2] = null;
			chat.handleMessageFromClientUI(messageToServer);
		}
	}

	@FXML
	public void getReportByCourseCode(ActionEvent e) {// load Statistic details of course on window
		String courseName = coursesComboBox.getValue();
		if (courseName != null) {
			connect(this);
			coursesComboBox.setDisable(true);
			messageToServer[0] = "getReportByCourse";
			String[] coursesSubString = courseName.split("-");
			messageToServer[1] = coursesSubString[0].trim();
			messageToServer[2] = null;
			chat.handleMessageFromClientUI(messageToServer);
		}
	}

	public void sumRangGrades(ExecutedExam eExam) throws IndexOutOfBoundsException {
		for (int i = 0; i < sumGradeRanges.length; i++)
			sumGradeRanges[i] += eExam.getGradeRang()[i];
	}

	public void ShowHistogramInBarChart() throws NullPointerException {
		// set values in the bar chart
		for (int i = 0, j = 9; i < sumGradeRanges.length * 10 - 10; i += 10, j += 10) {
			histogram.getData().add(new XYChart.Data(i + "-" + j, sumGradeRanges[i / 10]));
		}
		histogram.getData().add(new XYChart.Data("90-100", sumGradeRanges[9]));
		/*
		 * histogram.getData().add(new XYChart.Data("0-54", sumGradeRanges[0]));
		 * histogram.getData().add(new XYChart.Data("55-65", sumGradeRanges[1]));
		 * histogram.getData().add(new XYChart.Data("65-75", sumGradeRanges[2]));
		 * histogram.getData().add(new XYChart.Data("75-84", sumGradeRanges[3]));
		 * histogram.getData().add(new XYChart.Data("85-94", sumGradeRanges[4]));
		 * histogram.getData().add(new XYChart.Data("95-100", sumGradeRanges[5]));
		 */
		barChart.getData().add(histogram);
	}

	public void clearData() {
		barChart.getData().clear();
		histogram.getData().clear();
		medianTextField.clear();
		averageTextField.clear();
	}

	public void refreshPress() {

		chooseUserComboBox.getSelectionModel().clearSelection();
		reportByComboBox.getSelectionModel().clearSelection();
		subjectsComboBox.getSelectionModel().clearSelection();
		coursesComboBox.getSelectionModel().clearSelection();
		if (barChart.getData() != null)
			clearData();
		reportByComboBox.setDisable(false);
		chooseCourseLabel.setVisible(false);
		chooseSubjectLabel.setVisible(false);
		chooseUserNameLabel.setVisible(false);
		chooseUserComboBox.setVisible(true);
		chooseUserComboBox.setDisable(true);
		subjectsComboBox.setDisable(true);
		coursesComboBox.setDisable(true);
		medianTextField.clear();
		averageTextField.clear();
		initWindow();
		refreshPressed = false;
	}

	@FXML
	public void refreshPressListener(ActionEvent e) {

		refreshPressed = true;
		refreshPress();
	}

	/*******************************************************
	 *********** system information functions **************
	 *******************************************************/
	@FXML
	public void openQuestionStock(ActionEvent e) {
		Platform.runLater(() -> {
			final Node source = (Node) e.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			// stage.close();
			stage.close();
			openScreen("directorBoundary", "QuestionStock");
		});
	}

	@FXML
	public void GetQuestionStock(ActionEvent e) {
		TeacherControl tcontroller = new TeacherControl();
		Platform.runLater(() -> {
			try {
				tcontroller.openUpdateQuestionScreen(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	@FXML
	void GetExamsStock(ActionEvent e) {
		TeacherControl tcontroller = new TeacherControl();
		Platform.runLater(() -> {
			try {
				tcontroller.openUpdateExamScreen(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	public void getExecutedExamsStock(ActionEvent e) {
		TeacherSeeExamsControl tcontroller = new TeacherSeeExamsControl();
		Platform.runLater(() -> {
			try {
				tcontroller.openTeacherSeeExamScreen(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	public void getStudentsCopiesStock(ActionEvent e) {
		TeacherControl sController = new TeacherControl();
		//sController.myGradesPressed(null);
	}
}