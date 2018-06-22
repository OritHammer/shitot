package control;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import entity.Course;
import entity.Exam;
import entity.ExecutedExam;
import entity.Question;
import entity.QuestionInExam;
import entity.StudentPerformExam;
import entity.TeachingProfessionals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

public class TeacherSeeExamsControl extends TeacherControl implements Initializable {

	@FXML
	private TableView<ExecutedExam> ExamsForTeacher;
	@FXML
	private TableColumn<ExecutedExam, Time> executionTime;
	@FXML
	private TableColumn<ExecutedExam, Integer> numOfStudentStarted;
	@FXML
	private TableColumn<ExecutedExam, Integer> numOfStudentFinished;
	@FXML
	private TableColumn<ExecutedExam, String> timeAllocated;
	@FXML
	private TableColumn<ExecutedExam, String> executedExamID;
	@FXML
	private TableColumn<ExecutedExam, Integer> numOfStudentDidntFinished;
	@FXML
	private Button showStudentsButton;
	@FXML
	private TableColumn<ExecutedExam, String> date;
	@FXML
	private TextField medianTxtFiled;
	@FXML
	private TextField avgTxtFiled;
	@FXML
	private Label pageLabel;

	static private ArrayList<ExecutedExam> executedExam;
	static private ExecutedExam choosenExecutedExamToReport;
	@FXML
	static protected BarChart<?, ?> barChart;
	@SuppressWarnings("rawtypes")
	static protected XYChart.Series histogram = null;

	public void initialize(URL url, ResourceBundle rb) {
		if (pageLabel == null) {
			executedExam = new ArrayList<ExecutedExam>();
			// this condition is to initialize the screen that the director
			// see executed exam
			if (getMyUser().getRole().equals("director")) {
				messageToServer[1] = null;
				showStudentsButton.setVisible(true);
			} else {
				messageToServer[1] = getMyUser().getUsername();
			}
			messageToServer[4] = getMyUser().getUsername();
			setUnVisible();
			connect(this); // connecting to server
			messageToServer[0] = "getAllExecutedExams";
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
		} else if (pageLabel.getText().equals("StatisticReportTeacher")) {
			medianTxtFiled.setText(" " + choosenExecutedExamToReport.getMedian());
			avgTxtFiled.setText(" " + choosenExecutedExamToReport.getAverage());
			ShowHistogramInBarChart();
		}
	}

	/* check the content message from server */
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
				executedExam = (ArrayList<ExecutedExam>) msg[1];
				Platform.runLater(() -> {
					switch (msg[0].toString()) {

					/***************************************
					 * General "get" items from serer to client
					 ************************************/

					case ("getAllExecutedExams"): /*   */
					{
						// executedExam = (ArrayList<ExecutedExam>) msg[1];
						ObservableList<ExecutedExam> observablelistExamsForTeacher = FXCollections
								.observableArrayList(executedExam);
						executedExamID.setCellValueFactory(new PropertyValueFactory<>("executedExamID"));
						date.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("date"));
						executionTime.setCellValueFactory(
								new PropertyValueFactory<ExecutedExam, Time>("actuallySolutionTime"));
						timeAllocated.setCellValueFactory(new PropertyValueFactory<>("SolutionTime"));
						numOfStudentStarted.setCellValueFactory(
								new PropertyValueFactory<ExecutedExam, Integer>("numOfStudentStarted"));
						numOfStudentFinished.setCellValueFactory(
								new PropertyValueFactory<ExecutedExam, Integer>("numOfStudentFinished"));
						numOfStudentDidntFinished.setCellValueFactory(
								new PropertyValueFactory<ExecutedExam, Integer>("numOfStudentDidntFinished"));
						ExamsForTeacher.setItems(observablelistExamsForTeacher);
						break;
					}

					default: {
						System.out.println("Error in input");
					}
					}
				});
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void openScreen(ActionEvent e, String screen) throws IOException {

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("/boundary/" + screen + ".fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		tableViewScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		// This line gets the Stage information
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();
	}

	/**
	 * void showStatistic(ActionEvent e) The function show an specific executedExam
	 * statistic Report to teacher and manager
	 * 
	 * @author Orit Hammer
	 */
	public void showStatistic(ActionEvent e) throws IOException {
		choosenExecutedExamToReport = ExamsForTeacher.getSelectionModel().getSelectedItem();
		if (choosenExecutedExamToReport == null) {
			errorMsg("Please choose an exam");
			return;
		}

		openScreen(e, "StatisticReportTeacher");

		for (ExecutedExam ex : executedExam)
			if (ex.getExecutedExamID().equals(choosenExecutedExamToReport.getExecutedExamID())) {
				choosenExecutedExamToReport = ex;
				openScreen("boundary", "StatisticReportTeacher");
			}

	}

	/**
	 * void ShowHistogramInBarChart() The function show an specific executedExam
	 * statistic in histogram Report to teacher and manager
	 * 
	 * @author Orit Hammer
	 */
	@SuppressWarnings("rawtypes")
	public void ShowHistogramInBarChart() throws NullPointerException {
		// set values in the bar chart
		int[] sumGradeRanges = choosenExecutedExamToReport.getGradeRang();
		for (int i = 0, j = 9; i < 90; i += 10, j += 10) {
			histogram.getData().add(new XYChart.Data(i + "-" + j, sumGradeRanges[i / 10]));
		}
		histogram.getData().add(new XYChart.Data("90-100", sumGradeRanges[9]));
		barChart.getData().add(histogram);
	}

	public void showStudentsInThisExam(ActionEvent e) throws IOException {
		tempExamId = ExamsForTeacher.getSelectionModel().getSelectedItem().getExecutedExamID();
		openCheckExamScreen(e);
	}
}
