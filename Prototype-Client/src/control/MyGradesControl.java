package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import entity.ExamDetailsMessage;
import entity.Question;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MyGradesControl extends StudentControl implements Initializable {
	// *********for student see his grades AND can order exam***********//
		@FXML
		private TableView<ExamDetailsMessage> examGradesTable;
		@FXML
		private TableColumn<ExamDetailsMessage, String> examCodeColumn;
		@FXML
		private TableColumn<ExamDetailsMessage, String> courseCodeColumn;
		@FXML
		private TableColumn<ExamDetailsMessage, String> gradeColumn;
		@FXML
		private TableColumn<ExamDetailsMessage, String> dateColumn;
		@FXML
		private TableColumn<ExamDetailsMessage, String> executedIDCol;


	public void initialize(URL url, ResourceBundle rb) {
		connect(this);
		messageToServer[0] = "getExamsByUserName";
		messageToServer[1] = getMyUser().getUsername();
		messageToServer[2] = null;
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
	}
	public void showingCopy(ArrayList<Question> ques, HashMap<String, Integer> ans) {
		if (ans.isEmpty())
		{
			Alert emptyStdExam = new Alert(AlertType.INFORMATION, "Your exam has no answers please pay attention"
							,ButtonType.NO);
			emptyStdExam.showAndWait() ; 
		}
		examAnswers = ans;
		questioninexecutedexam = ques;
		copyFlag = true;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (justFlag == true) {
					((Stage) ((Node) tempEvent.getSource()).getScene().getWindow()).hide();
					Stage primaryStage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					Pane root;
					try {
						root = loader
								.load(getClass().getResource("/studentBoundary/ComputerizedExam.fxml").openStream());
						Scene scene = new Scene(root);
						primaryStage.setScene(scene);
						primaryStage.show();
					} catch (IOException e) {

						e.printStackTrace();
					}
				} else {
					openScreen("studentBoundary", "ComputerizedExam");
				}
			}
		});
	}
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final Object[] msgFromServer = (Object[]) message;
		Platform.runLater(() -> {
			switch (msgFromServer[0].toString()) {
			case "getExamsByUserName": {
				showGradesOnTable((ArrayList<ExamDetailsMessage>) msgFromServer[1]);
				break;
			}
			case "showingCopy": {
				showingCopy((ArrayList<Question>) msgFromServer[1],
						(HashMap<String, Integer>) msgFromServer[2]);
				break;
			}
			}
		});
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showGradesOnTable(ArrayList<ExamDetailsMessage> detailsFromS) {

		for (ExamDetailsMessage edM : detailsFromS) {
			detailsList.add(edM);
			executeExamList.add(edM.getExcecutedExamID());
		}

		if (examGradesTable != null && examGradesTable.getColumns() != null)
			examGradesTable.getColumns().clear();
		if (examCodeCombo != null && examCodeCombo.getItems() != null)
			examCodeCombo.getItems().clear();
		examCodeCombo.setItems(executeExamList);
		examGradesTable.setItems(detailsList);
		examCodeColumn.setCellValueFactory(new PropertyValueFactory("examID"));
		dateColumn.setCellValueFactory(new PropertyValueFactory("examDate"));
		gradeColumn.setCellValueFactory(new PropertyValueFactory("examGrade"));
		courseCodeColumn.setCellValueFactory(new PropertyValueFactory("examCourse"));
		executedIDCol.setCellValueFactory(new PropertyValueFactory<>("excecutedExamID"));
		examGradesTable.getColumns().addAll(examCodeColumn, courseCodeColumn, gradeColumn, dateColumn, executedIDCol);
		/*
		 * also need to take from detailsFromS the exam_id's and insert them to
		 * observeable list into the relevante combobox .
		 */
	}
	/*********************
	 * Student Order Copy
	 * 
	 * @throws IOException
	 *************************/
	public void orderExamPressed(ActionEvent e) {
		/*
		 * messageToServer[0] = "getExamsCopyByUserName"; messageToServer[1] =
		 * examCodeCombo.getValue(); messageToServer[2] = null;
		 * chat.handleMessageFromClientUI(messageToServer);// send the message to server
		 */
		if (examCodeCombo.getValue() == null) {
			errorMsg("Please select exam first");
			return;
		}
		connect(this);
		messageToServer[0] = "getStudentAnswers";
		messageToServer[1] = examCodeCombo.getValue(); // sending executed exam id
		messageToServer[2] = getMyUser().getUsername(); // sending the user name
		chat.handleMessageFromClientUI(messageToServer);
	}

}
