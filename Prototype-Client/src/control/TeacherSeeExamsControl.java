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
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
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
    private TableColumn<ExecutedExam, String> date;
    
    
    public void initialize(URL url, ResourceBundle rb) {
    	if(getMyUser().getRole().equals("director")) {
			messageToServer[1] = null;
    	}
    	else {
			messageToServer[1] = getMyUser().getUsername();
    	}
		messageToServer[4] = getMyUser().getUsername();
		setUnVisible();
			connect(this); // connecting to server
			messageToServer[0] = "getAllExecutedExams";
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
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

				Platform.runLater(() -> {
					switch (msg[0].toString()) {

					/***************************************
					 * General "get" items from serer to client
					 ************************************/

					case ("getAllExecutedExams"): /*   */
					{
						ObservableList<ExecutedExam> observablelistExamsForTeacher = FXCollections
								.observableArrayList((ArrayList<ExecutedExam>) msg[1]);
						executedExamID.setCellValueFactory(new PropertyValueFactory<>("executedExamID"));
						date.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("date"));
						executionTime.setCellValueFactory(new PropertyValueFactory<ExecutedExam, Time>("actuallySolutionTime"));
						timeAllocated.setCellValueFactory(new PropertyValueFactory<>("SolutionTime"));
						numOfStudentStarted.setCellValueFactory(new PropertyValueFactory<ExecutedExam,Integer>("numOfStudentStarted"));
						numOfStudentFinished.setCellValueFactory(new PropertyValueFactory<ExecutedExam, Integer>("numOfStudentFinished"));
						numOfStudentDidntFinished.setCellValueFactory(new PropertyValueFactory<ExecutedExam, Integer>("numOfStudentDidntFinished"));
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
	}

