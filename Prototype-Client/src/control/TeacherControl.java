package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import entity.Course;
import entity.Exam;
import entity.ExecutedExam;
import entity.Question;
import entity.QuestionInExam;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TeacherControl extends UserControl implements Initializable {

	private Boolean trueAnsFlag = false;//
	private Object[] messageToServer = new Object[3];
	ObservableList<QuestionInExam> questionInExamObservable = FXCollections.observableArrayList();

	/* fxml variables */
	@FXML
	private Text userText;
	@FXML
	private TextField teacherNameOnCreate;

	@FXML
	private Label pageLabel;

	@FXML
	private TextField answer1;
	@FXML
	private TextField answer2;
	@FXML
	private TextField answer3;
	@FXML
	private TextField answer4;

	@FXML
	private TextField pointsText;
	@FXML
	private TextField questionName;
	@FXML
	private TextField questionID;
	@FXML
	private TextField teacherName;
	@FXML
	private TextField remarksForStudent;
	@FXML
	private TextField remarksForTeacher;
	@FXML
	private TextField timeForExamHours;
	@FXML
	private TextField timeForExamMinute;
	@FXML
	private TextField reasonForChange;
	@FXML
	private TextField examCode;
	
	
	/* RadioButton of display the correct answer */
	@FXML
	private RadioButton correctAns1;
	@FXML
	private RadioButton correctAns2;
	@FXML
	private RadioButton correctAns3;
	@FXML
	private RadioButton correctAns4;

	@FXML
	private ToggleGroup group;

	@FXML
	private TableView<QuestionInExam> questionsInExamTableView;
	@FXML
	private TableColumn<QuestionInExam, String> questionNameTableView;
	@FXML
	private TableColumn<QuestionInExam, Float> questionPointsTableView;
	
	@FXML
    private TableView<ExecutedExam> executedExamTableView;
    @FXML
    private TableColumn<ExecutedExam, String> exam_idTableView;
    @FXML
    private TableColumn<ExecutedExam, String> executedExamIDTableView;
    @FXML
    private TableColumn<ExecutedExam, String> teacherNameTableView;
	
    @FXML
	private ComboBox<String> questionsComboBox;
	@FXML
	private ComboBox<String> subjectsComboBox;
	@FXML
	private ComboBox<String> coursesComboBox;
	@FXML
	private ComboBox<String> examComboBox;
	@FXML
	private ComboBox<String> typeComboBox;

	/* check the content message from server */
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();// close the connection

			Object[] msg = (Object[]) message;
			switch (msg[0].toString()) {
			case ("getSubjects"): /* get the subjects list from server */
			{
				showSubjects((ArrayList<TeachingProfessionals>) msg[1]);
				break;
			}
			case ("getExecutedExams"): /* get the subjects list from server */
			{
				showExecutedExam((ArrayList<ExecutedExam>) msg[1]);
				break;
			}
			case ("getCourses"): /* get the courses list from server */
			{
				showCourses((ArrayList<Course>) msg[1]);
				break;
			}
			case ("getQuestions"): /* get the questions list from server */
			{
				showQuestions((ArrayList<String>) msg[1]);
				break;
			}
			case ("getExams"): /* get the subjects list from server */
			{
				showExams((ArrayList<String>) msg[1]);
				break;
			}
			case ("getQuestionDetails"): /* get the subject list from server */
			{
				showQuestionDetails((Question) msg[1]);
				break;
			}
			default: {
				System.out.println("Error in input");
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	private void showExecutedExam(ArrayList<ExecutedExam> executedexam) {
		ObservableList<ExecutedExam> observablelist = FXCollections.observableArrayList(executedexam);
		executedExamTableView.setItems(observablelist);
		executedExamIDTableView.setCellValueFactory(new PropertyValueFactory<>("executedExamID"));
		teacherNameTableView.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
		exam_idTableView.setCellValueFactory(new PropertyValueFactory<>("exam_id"));
	}

	/* clear all the text fields and radio buttons */
	public void initialize(URL url, ResourceBundle rb) {
		if (pageLabel.getText().equals("Create exam")) {
			typeComboBox.setItems(FXCollections.observableArrayList("computerized", "manual"));
			timeForExamHours.setText("00");
			timeForExamMinute.setText("00");
		}
		if (pageLabel.getText().equals("Home screen")) {
			userText.setText(Globals.getFullName());
		}
		if (pageLabel.getText().equals("Create question") 
			|| pageLabel.getText().equals("Create exam")
			|| pageLabel.getText().equals("Update question") 
			|| pageLabel.getText().equals("Create exam code")
			|| pageLabel.getText().equals("Extend exam time")
			|| pageLabel.getText().equals("Lock exam")) {
			
			connect(this);
			if (pageLabel.getText().equals("Extend exam time")
				|| pageLabel.getText().equals("Lock exam")) {
				
				messageToServer[0] = "getExecutedExams";
				messageToServer[1] = Globals.getuserName();
				chat.handleMessageFromClientUI(messageToServer);// send the message to server
			}
			
			if (pageLabel.getText().equals("Create question")) {
				teacherNameOnCreate.setText(Globals.getuserName());
			}
			
			messageToServer[0] = "getSubjects";
			messageToServer[1] = Globals.getuserName();
			messageToServer[2] = null;
			chat.handleMessageFromClientUI(messageToServer);// send the message to server
		}
	}

	/***************** Opening screens action-events *****************/
	public void openExtendExamTimeScreen(ActionEvent e) {
		openScreen("ExtendExamTime");
	}

	public void openUpdateQuestionScreen(ActionEvent e) {
		openScreen("UpdateQuestion");
	}

	public void openExamCodeScreen(ActionEvent e) {
		openScreen("CreateExamCode");
	}

	public void openCreateExam(ActionEvent e) {
		openScreen("CreateExam");
	}

	public void openCreateQuestion(ActionEvent e) {
		openScreen("CreateQuestion");
	}

	/***************** Create exam functions *****************/
	public void lockSubject(ActionEvent e) {
		subjectsComboBox.setDisable(true);
	}

	public void toQuestionInExam(ActionEvent e) {
		if (pointsText.getText().equals("")) {
			openScreen("ErrorMessage", "Please fill the points area");
			return;
		}
		if (questionsComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please choose question");
			return;
		}
		QuestionInExam questioninexam = new QuestionInExam();// creating new questioninexam
		String[] questionDetails = questionsComboBox.getValue().split("-");
		questioninexam.setQuestionID(questionDetails[0]);
		questioninexam.setPoints(Integer.parseInt(pointsText.getText()));
		questionInExamObservable.add(questioninexam);
		questionsInExamTableView.setItems(questionInExamObservable);
		questionNameTableView.setCellValueFactory(new PropertyValueFactory<>("questionID"));// display the id in the
																							// table view
		questionPointsTableView.setCellValueFactory(new PropertyValueFactory<>("points"));// display the points in the
																							// table view
		pointsText.setText("");// entering the question to the list and put text "" in the points component
		questionsComboBox.getItems().remove(questionsComboBox.getValue());// removing the question from the combobox

		// questionsInExamTableView.setRowFactory(value);
	}

	public void removeFromTableView(ActionEvent e) {
		ObservableList<QuestionInExam> questiontoremove = questionsInExamTableView.getSelectionModel()
				.getSelectedItems();
		questiontoremove.forEach(questionInExamObservable::remove);
		// questionsComboBox.getItems().add(questiontoremove);//removing the question
		// from the combobox
	}

	@SuppressWarnings("static-access")
	public void createExam(ActionEvent e) {
		int sumOfPoints = 0;
		if (timeForExamHours.getText().equals("") || timeForExamMinute.getText().equals("")
				|| Integer.valueOf(timeForExamHours.getText()) < 0) {
			openScreen("ErrorMessage", "Please fill time for exam");
			return;
		}
		if (typeComboBox.getValue().equals(null)) {
			openScreen("ErrorMessage", "Please select the type of exam");
			return;
		}
		for (QuestionInExam q : questionInExamObservable) {
			sumOfPoints += q.getPoints();
		}
		if (sumOfPoints != 100) {
			openScreen("ErrorMessage", "Points are not match to 100");
			return;
		}
		Exam exam = new Exam();// creating a new exam;
		Time time = null;
		String courseID = questionInExamObservable.get(0).getQuestionID().substring(0, 2);// we want the course id
		String []subjectSubString=subjectsComboBox.getValue().split("-");
		exam.setE_id(subjectSubString[0].trim() + "" + courseID);// making the start of the id of the exam
		ArrayList<QuestionInExam> questioninexam = (ArrayList<QuestionInExam>) questionInExamObservable.stream()
				.collect(Collectors.toList());// making the observable a lis
		exam.setRemarksForStudent(remarksForStudent.getText());
		exam.setRemarksForTeacher(remarksForTeacher.getText());
		exam.setTeacherUserName(Globals.getuserName());
		time = time.valueOf(timeForExamHours.getText() + ":" + timeForExamMinute.getText() + ":00");// making a Time
																									// class format
		exam.setSolutionTime(time.toString());
		exam.setType(typeComboBox.getValue());
		messageToServer[0] = "setExam";
		messageToServer[1] = questioninexam;
		messageToServer[2] = exam;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
		try {
			chat.closeConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // close the connection
	}
	
	
	public void createExamCode(ActionEvent e) {
		ExecutedExam exam;
		String examID=examComboBox.getValue();
		String executedExamId = examCode.getText();
		if (subjectsComboBox.getValue()==null) {
			openScreen("ErrorMessage", "Please choose subject");
			return;
		}
		if (coursesComboBox.getValue()==null) {
			openScreen("ErrorMessage", "Please choose course");
			return;
		}
		if (examComboBox.getValue()==null) {
			openScreen("ErrorMessage", "Please choose exam");
			return;
		}
		if (executedExamId.length() != 4) {
			for (int i = 0 ; i < executedExamId.length() ; i++){
	            char ch = executedExamId.charAt(i);

	            if (!((ch>='a' && ch<='z')||(ch>='A' && ch<='Z')||(ch>='0' && ch<='9'))){
	                openScreen("ErrorMessage", "You must enter only letters and numbers.");
	                return;
	            }
	        }
			openScreen("ErrorMessage", "You must enter exactly 4 letters & number");
			return;
		}
		exam = new ExecutedExam();
		exam.setExecutedExamID(executedExamId);
		exam.setTeacherName(Globals.getuserName());
		exam.setExam_id(examID);
		messageToServer[0] = "setExamCode";
		messageToServer[1] = exam;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
		try {
			chat.closeConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // close the connection
	}

	/***************** Update question screen function *****************/
	/* send to server request for update correct answer */
	
	public void updateCorrectAnswer(ActionEvent e) throws IOException, SQLException {
		if (trueAnsFlag) {// if there is permission to update the question
			String qID = questionID.getText();
			RadioButton selected = (RadioButton) group.getSelectedToggle();
			String selectedId = selected.getId();
			messageToServer[0] = "updateCorrectAnswer";
			messageToServer[1] = qID;
			messageToServer[2] = selectedId;
			connect(this);
			chat.handleMessageFromClientUI(messageToServer); // send the request to the server
			chat.closeConnection();
		}
	}

	/***************** create question screen function *****************/
	/* initialized the update Question window */
	
	public void createQuestionClick(ActionEvent e) throws IOException {
		if (subjectsComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please choose subject");
			return;
		}
		Question question = new Question();
		ArrayList<String> answers = new ArrayList<String>();
		question.setTeacherName(Globals.getuserName());
		question.setQuestionContent(questionName.getText().trim());
		answers.add(answer1.getText().trim());
		answers.add(answer2.getText().trim());
		answers.add(answer3.getText().trim());
		answers.add(answer4.getText().trim());

		int correctAnswer = 0;
		if (correctAns1.isSelected()) {
			correctAnswer = 1;
		}
		if (correctAns2.isSelected()) {
			correctAnswer = 2;
		}
		if (correctAns3.isSelected()) {
			correctAnswer = 3;
		}
		if (correctAns4.isSelected()) {
			correctAnswer = 4;
		}

		if ((answers.get(0).equals("")) || ((answers.get(1).equals(""))) || (answers.get(2).equals(""))
				|| (answers.get(3).equals("")) || (correctAnswer == 0) || (question.getQuestionContent().equals(""))) {
			openScreen("ErrorMessage", "Not all fields are completely full");
		} else {
			question.setAnswers(answers);
			question.setTrueAnswer(correctAnswer);
			String subject = subjectsComboBox.getValue();
			String[] subjectSubString = subject.split("-");
			connect(this); // connecting to server
			messageToServer[0] = "SetQuestion";
			messageToServer[1] = subjectSubString[0].trim();
			messageToServer[2] = question;
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
			chat.closeConnection();// close the connection
		}
	}

	/*****************
	 * functions that all most of the screens uses
	 *****************/

	public void loadQuestions(ActionEvent e) throws IOException {
		/* ask for the qustions text */
		String subject = subjectsComboBox.getValue(); // get the subject code
		if (subject == null)
			return;
		String[] subjectSubString = subject.split("-");
		questionsComboBox.getSelectionModel().clearSelection();
		if (!pageLabel.getText().equals("Create exam"))
			clearForm();
		connect(this); // connecting to server
		messageToServer[0] = "getQuestions";
		messageToServer[1] = subjectSubString[0].trim();
		messageToServer[2] = Globals.getuserName();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

	public void askForQuestionDetails(ActionEvent e) throws IOException {

		String selectedQuestion = questionsComboBox.getValue(); // get the selected question
		if (selectedQuestion == null)
			return;
		String[] questionDetails = selectedQuestion.split("-");
		connect(this); // connecting to server
		messageToServer[0] = "getQuestionDetails";
		messageToServer[1] = questionDetails[1];
		messageToServer[2] = null;
		clearForm();
		chat.handleMessageFromClientUI(messageToServer);// ask for details of specific question

	}

	/* this method show the subjects list on the combobox */
	public void showSubjects(ArrayList<TeachingProfessionals> msg) {
		ObservableList<String> observableList = FXCollections.observableArrayList();
		for (TeachingProfessionals tp : msg) {
			observableList.add(tp.getTp_id() + " - " + tp.getName());
			subjectsComboBox.setItems(observableList);
		}
	}
	
	/* this method show the questions list on the combobox */
	public void showQuestions(ArrayList<String> questionsList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(questionsList);
		questionsComboBox.setItems(observableList);
	}

	public void clearForm() {
		answer1.clear();
		answer2.clear();
		answer3.clear();
		answer4.clear();
		questionID.clear();
		teacherName.clear();
		correctAns1.setSelected(false);
		correctAns2.setSelected(false);
		correctAns3.setSelected(false);
		correctAns4.setSelected(false);
	}

	private void openScreen(String screen) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			if (screen.equals("ErrorMessage")) {
				ErrorControl tController = loader.getController();
				tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
				tController.setErrorMessage("ERROR");// send a the error to the alert we made
			}
			stage.setTitle(screen);
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}
	}

	private void openScreen(String screen, String message) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			ErrorControl tController = loader.getController();
			tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
			tController.setErrorMessage(message);// send a the error to the alert we made
			stage.setTitle("Error message");
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}
	}

	/* this method show the question details to user */
	public void showQuestionDetails(Question q) {
		try {
			trueAnsFlag = true;// Permit to user change the correct answer
			questionID.setText(q.getId());
			teacherName.setText(q.getTeacherName());
			ArrayList<String> answers = q.getAnswers();
			answer1.setText(answers.get(0));
			answer2.setText(answers.get(1));
			answer3.setText(answers.get(2));
			answer4.setText(answers.get(3));

			/* set up the correct answer button */
			switch (q.getTrueAnswer()) {/* The number of the correct answers */
			case (1): {
				correctAns1.setSelected(true);
				break;
			}
			case (2): {
				correctAns2.setSelected(true);
				break;
			}
			case (3): {
				correctAns3.setSelected(true);
				break;
			}
			case (4): {
				correctAns4.setSelected(true);
				break;
			}
			}
		} catch (Exception e) {
		}
	}

	/* close button was pressed */
	public void closeScreen(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
		openScreen("HomeScreenTeacher");
	}

	/***************** create Exam and extend extend time code screen function *****************/
	public void loadCourses(ActionEvent e) throws IOException {
		/* ask for the courses name */
		String subject = subjectsComboBox.getValue(); // get the subject code
		if (subject == null)
			return;
		String[] subjectSubString = subject.split("-");
		coursesComboBox.getSelectionModel().clearSelection();
		connect(this); // connecting to server
		messageToServer[0] = "getCourses";
		messageToServer[1] = subjectSubString[0].trim();
		messageToServer[2] = Globals.getuserName();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}
	
	/* this method show the Courses list on the combobox */
	public void showCourses(ArrayList<Course> msg) {
		ObservableList<String> observableList = FXCollections.observableArrayList();
		for (Course c : msg) {
			observableList.add(c.getCourseID() + " - " + c.getName());
		}
		coursesComboBox.setItems(observableList);
	}
	
	public void loadExams(ActionEvent e) throws IOException {
		/* ask for the exams name */
		if(coursesComboBox.getValue()==null) {
			return;
		}
		String examIDStart;
		String []subjectSubString = subjectsComboBox.getValue().split("-");
		String []examSubString = coursesComboBox.getValue().split("-");
		examIDStart=subjectSubString[0].trim()+""+examSubString[0].trim();
		if (examIDStart.equals("") || examIDStart == null)
			return;		
		connect(this); // connecting to server
		messageToServer[0] = "getExams";
		messageToServer[1] = examIDStart;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}
	
	public void openLockExamScreen(ActionEvent e) {
		openScreen("LockExam");
	}
	
	public void lockExam(ActionEvent e) {
		ExecutedExam executedexam= executedExamTableView.getSelectionModel().getSelectedItem();
		if(executedexam==null) {
			openScreen("ErrorMessage","Please choose an exam");
			return;
		}
		connect(this); // connecting to server
		messageToServer[0] = "setExecutedExamLocked";
		messageToServer[1] = executedexam.getExecutedExamID();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}
	
	public void showExams(ArrayList<String> examList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(examList);
		examComboBox.setItems(observableList);
	}
	
	public void createExtendTimeRequest(ActionEvent e) throws IOException {
		if(timeForExamHours.getText().equals("")||timeForExamMinute.getText().equals("")) {
			openScreen("ErrorMessage","Please fill the time you want to extend by");
			return;
		}
		if(reasonForChange.getText().trim().equals("")) {
			openScreen("ErrorMessage","Please fill the reason for changing the time");
			return;
		}
		ExecutedExam executedexam= executedExamTableView.getSelectionModel().getSelectedItem();
		if(executedexam==null) {
			openScreen("ErrorMessage","Please choose an exam");
			return;
		}
		RequestForChangingTimeAllocated request = new RequestForChangingTimeAllocated();
		request.setIDexecutedExam(executedexam.getExecutedExamID());
		request.setReason(reasonForChange.getText());
		request.setMenagerApprove("waiting");
		request.setTeacherName(Globals.getuserName());
		request.setTimeAdded(timeForExamHours.getText()+""+timeForExamMinute.getText());
		connect(this); // connecting to server
		messageToServer[0] = "createChangingRequest";
		messageToServer[1] = request;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
		chat.closeConnection();
	}
}
