package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import entity.Course;
import control.StudentControl;
import entity.Exam;
import entity.ExecutedExam;
import entity.Question;
import entity.QuestionInExam;
import entity.RequestForChangingTimeAllocated;
import entity.StudentPerformExam;
import entity.TeachingProfessionals;
import entity.User;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.FloatStringConverter;

public class TeacherControl extends UserControl implements Initializable {

	private static ObservableList<QuestionInExam> questionInExamObservable = FXCollections.observableArrayList();
	private ObservableList<String> coursesListToCreateQuestion = FXCollections.observableArrayList();
	private ObservableList<Question> questionObservableList;
	private Object[] messageToServer = new Object[5];
	private static boolean blockPassQuestionButton;

	private ActionEvent temp;
	private ObservableList<Exam> exams;
	private Question questionSelected;
	private Question oldQuestion;
	private static String tempExamId;
	private Exam examSelected;
	private Exam oldExam;
	private String text=null;
	/* fxml variables */
	@FXML
	private Text userText;
	@FXML
	private Text allertText;
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
	private ToggleGroup group1;

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
	private TableView<Question> questionTableView;
	@FXML
	private TableColumn<Question, String> qid;
	@FXML
	private TableColumn<Question, String> tname;
	@FXML
	private TableColumn<Question, String> qtext;
	@FXML
	private TableColumn<Question, String> a1;
	@FXML
	private TableColumn<Question, String> a2;
	@FXML
	private TableColumn<Question, String> a3;
	@FXML
	private TableColumn<Question, String> a4;
	@FXML
	private TableColumn<Question, String> correctAns;


	@FXML
	private TableView<Exam> examsTableView;
	@FXML
	private TableColumn<Exam, String> examITable;
	@FXML
	private TableColumn<Exam, String> teacherNameTable;
	@FXML
	private TableColumn<Exam, String> remarksForTeacherTable;
	@FXML
	private TableColumn<Exam, String> solutionTimeTable;
	@FXML
	private TableColumn<Exam, String> typeTable;
	@FXML
	private TableColumn<Exam, String> questionIDTable;
	@FXML
	private TableColumn<Exam, String> remarksForStudentTable;

	
	@FXML
	private TableView<StudentPerformExam> studnetInExamTableView;
	@FXML
	private TableColumn<StudentPerformExam, String> studentId;
	@FXML
	private TableColumn<StudentPerformExam, String> studentName;
	@FXML
	private TableColumn<StudentPerformExam, String> grade;
	
	@FXML
	private ComboBox<String> subjectsComboBox;
	@FXML
	private ComboBox<String> coursesComboBox;
	@FXML
	private ComboBox<String> examComboBox;
	@FXML
	private ComboBox<String> typeComboBox;
	@FXML
	private ComboBox<String> executedExamsComboBox;

	@FXML
	private Button passQuestionL;
	@FXML
	private Button passQuestionR;
	@FXML
	private Button backButton;
	@FXML
	private Button updateBtn;

	  @FXML
	  private ListView<String> courseInCreateQuestion;
	  
	  @FXML
	  private AnchorPane mainPane;
	    
	    public void loadExamCopy(MouseEvent event) {
	    	if(event.getClickCount() == 1)
	    	{
	    		connect(this);
	    		messageToServer[0] = "getStudentAnswers";
	    		messageToServer[1] = executedExamsComboBox.getValue(); // sending executed exam id
	    		messageToServer[2] = studnetInExamTableView.getSelectionModel().getSelectedItem().getUserName(); // sending the user name
	    		chat.handleMessageFromClientUI(messageToServer);
	    		StudentControl studentControl = new StudentControl();
	    		studentControl.tempEvent = event;
	    	}
	    }
	  
	/* check the content message from server */
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();// close the connection

			final Object[] msg = (Object[]) message;
			Platform.runLater(() -> {
				switch (msg[0].toString()) {

				/*************************************** General "get" items from serer to client ************************************/
				
				case "showingCopy":
				{
					StudentControl scontrol = new StudentControl();
					scontrol.justFlag = true;
					scontrol.showingCopy((ArrayList<Question>) msg[1], (HashMap<String, Integer>) msg[2]);
					break;
				}
				
				case ("getSubjects"): /* get the subjects list from server */
				{
					ObservableList<String> observableList = FXCollections.observableArrayList();
					for (TeachingProfessionals tp : (ArrayList<TeachingProfessionals>) msg[1]) {
						observableList.add(tp.getTp_id() + " - " + tp.getName());
					}
					subjectsComboBox.setItems(observableList);
					break;
				}

				case ("getCourses"): /* get the courses list from server */
				{
					ObservableList<String> observableList = FXCollections.observableArrayList();
					for (Course c : (ArrayList<Course>) msg[1]) {
						observableList.add(c.getCourseID() + " - " + c.getName());
					}
					coursesComboBox.setItems(observableList);
					break;
				}

				/************************************************************ All Question cases ************************************/

				case ("getQuestionsToTable"): /* get the questions list from server */
				{
					questionObservableList = FXCollections.observableArrayList((ArrayList<Question>) msg[1]);//kaki
					qid.setCellValueFactory(new PropertyValueFactory<>("id"));
					tname.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
					qtext.setCellValueFactory(new PropertyValueFactory<>("questionContent"));
					if (pageLabel.getText().equals("Update question")) {
						a1.setCellValueFactory(new PropertyValueFactory<>("answer1"));
						a2.setCellValueFactory(new PropertyValueFactory<>("answer2"));
						a3.setCellValueFactory(new PropertyValueFactory<>("answer3"));
						a4.setCellValueFactory(new PropertyValueFactory<>("answer4"));
						correctAns.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
						questionTableView.setEditable(true);
						ObservableList<String> numbers = FXCollections.observableArrayList("1", "2", "3", "4");
						qtext.setCellFactory(TextFieldTableCell.forTableColumn());
						a1.setCellFactory(TextFieldTableCell.forTableColumn());
						a2.setCellFactory(TextFieldTableCell.forTableColumn());
						a3.setCellFactory(TextFieldTableCell.forTableColumn());
						a4.setCellFactory(TextFieldTableCell.forTableColumn());
						correctAns.setCellFactory(TextFieldTableCell.forTableColumn());
						correctAns.setCellFactory(
								ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), numbers));
						questionObservableList = FXCollections.observableArrayList((ArrayList<Question>) msg[1]);
					}
					questionTableView.setItems(questionObservableList);
					questionTableView.getSortOrder().setAll(qid);
					break;
				}

				case ("updateQuestion"): /* the server return true/false if the question updated or not */
				{
					if ((boolean) msg[1] == true) {
						questionTableView.refresh();
					} else {
						questionObservableList.remove(questionObservableList.indexOf(questionSelected));
						questionObservableList.add(oldQuestion);
						Platform.runLater(() -> openScreen("ErrorMessage", "This question is in active exam."));
						questionTableView.getSortOrder().setAll(qid);
					}
					break;
				}

				case ("deleteQuestion"): /* the server return true/false if the question deleted or not */
				{
					if((boolean) msg[1])
					{
						if ((boolean) msg[1] == true) {
							int index = questionObservableList.indexOf(questionSelected);
							questionObservableList.remove(index);
							questionTableView.refresh();
						} else {
							Platform.runLater(() -> openScreen("ErrorMessage", "This question is in active exam."));
						}
					}
					else
					{
						Platform.runLater(() -> openScreen("ErrorMessage", "This question is in exam, first delete the exam"));
					}
					break;
				}

				/***************************************************** All Exam cases *********************************************/

				case ("getExams"): /* get the exams list from server */
				{
					if (pageLabel.getText().equals("Update exam")) {
						exams = FXCollections.observableArrayList((ArrayList<Exam>) msg[1]);
						questionIDTable.setCellValueFactory(new PropertyValueFactory<>("e_id"));
						teacherNameTable.setCellValueFactory(new PropertyValueFactory<>("teacherUserName"));
						solutionTimeTable.setCellValueFactory(new PropertyValueFactory<>("solutionTime"));
						remarksForTeacherTable.setCellValueFactory(new PropertyValueFactory<>("remarksForTeacher"));
						remarksForStudentTable.setCellValueFactory(new PropertyValueFactory<>("remarksForStudent"));
						typeTable.setCellValueFactory(new PropertyValueFactory<>("type"));
						ObservableList<String> type = FXCollections.observableArrayList("computerized", "manual");
						solutionTimeTable.setCellFactory(TextFieldTableCell.forTableColumn());
						remarksForTeacherTable.setCellFactory(TextFieldTableCell.forTableColumn());
						remarksForStudentTable.setCellFactory(TextFieldTableCell.forTableColumn());
						typeTable.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), type));
						examsTableView.setItems(exams);
					} else {
						ObservableList<String> observableList = FXCollections.observableArrayList();
						ArrayList<Exam> exams = (ArrayList<Exam>) msg[1];
						for (Exam e : exams) {
							observableList.add(e.getE_id());
						}
						examComboBox.setItems(observableList);

					}
					break;
				}

				case ("getExecutedExams"): /* get the executed exams list from server */
				{
					if (!pageLabel.getText().equals("Check exam")) {
					ObservableList<ExecutedExam> observablelist = FXCollections
							.observableArrayList((ArrayList<ExecutedExam>) msg[1]);
					executedExamTableView.setItems(observablelist);
					executedExamIDTableView.setCellValueFactory(new PropertyValueFactory<>("executedExamID"));
					teacherNameTableView.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
					exam_idTableView.setCellValueFactory(new PropertyValueFactory<>("exam_id"));
					break;
					}
					else
					{
						ObservableList<String> observablelistToExecutedExamComboBox = FXCollections.observableArrayList();
						ArrayList<ExecutedExam> exams = (ArrayList<ExecutedExam>) msg[1];
						for(ExecutedExam e : exams)
						{
							observablelistToExecutedExamComboBox.add(e.getExecutedExamID());
						}
						executedExamsComboBox.setItems(observablelistToExecutedExamComboBox);
					}
					break;
				}

				case ("getQuestionInExam"): /* get the question list of specific exam from server and check if the exam active or not*/
				{
					try {
						((ArrayList<QuestionInExam>) msg[1]).forEach(questionInExamObservable::add);
						final boolean flag1 = (boolean) msg[2];
						Platform.runLater(() -> {
							if (flag1 == false) {
								blockPassQuestionButton = true;
							} else {
								blockPassQuestionButton = false;
							}
							try {
								openScreen(temp,"UpdateQuestionInExam");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
					} catch (NullPointerException exception) {
						Platform.runLater(() -> openScreen("ErrorMessage", "exam does not have any question"));
						blockPassQuestionButton = false;

					}
					break;
				}

				case ("setExecutedExamLocked"): /* set executed exam lock */
					text="Exam locked successfully ✔";
				case ("setExamCode"): /* the server return true/false if the executed exam created or not */
				{
					if(!text.equals("Exam locked successfully ✔"))
					{
						text="Exam code created successfully ✔";
					}
					if ((boolean) msg[1] == true) {
						allertText.setFill(Color.GREEN);
						allertText.setText(text);
					} else {
						allertText.setFill(Color.RED);
						allertText.setText("There is already a code like that, please choose another code ❌");
					}
					break;
				}
				case ("updateExam"): /* the server return true/false if the exam updated or not */
				{
					if ((boolean) msg[1] == true) {
						examsTableView.refresh();
					} else {
						exams.remove(exams.indexOf(examSelected));
						exams.add(oldExam);
						Platform.runLater(() -> openScreen("ErrorMessage", "This exam is in active exam."));
						examsTableView.getSortOrder().setAll(questionIDTable);
					}
					break;
				}

				case ("deleteExam"): /*  the server return true/false if the exam deleted or not */
				{
					if ((boolean) msg[1] == true) {
						exams.remove(exams.indexOf(examSelected));
					} else {

						Platform.runLater(() -> openScreen("ErrorMessage", "This exam is in active exam."));
					}
					break;
				}
				
				case ("getStudenstInExam"): /*   */
				{
					ObservableList<StudentPerformExam> observablelistOfStudentInExam = FXCollections
							.observableArrayList((ArrayList<StudentPerformExam>) msg[1]);
					studentId.setCellValueFactory(new PropertyValueFactory<>("userId"));
					studentName.setCellValueFactory(new PropertyValueFactory<>("userFullname"));
					grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
					studnetInExamTableView.setItems(observablelistOfStudentInExam);
					break;
				}

				default: {
					System.out.println("Error in input");
				}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/* intialize function go first after loading fxml */
	public void initialize(URL url, ResourceBundle rb) {

		switch (pageLabel.getText()) {
		case ("Home screen"): {
			userText.setText(getMyUser().getUsername());
			break;
		}
		case ("Update question in exam"): {
			updateBtn.setDisable(true);
			if (blockPassQuestionButton) {
				passQuestionL.setDisable(true);
				passQuestionR.setDisable(true);
				questionsInExamTableView.setEditable(false);
				allertText.setFill(Color.RED);
				allertText.setText("You can't edit this exam cause its an active exam");
			}

			setToQuestionInExamTableView();
			connect(this); // connecting to server
			messageToServer[0] = "getQuestionsToTable";
			messageToServer[1] = tempExamId.substring(0, 4);
			messageToServer[2] = getMyUser().getUsername();
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
			break;
		}
		case ("Create question"):
		case ("Create exam"):
		case ("Update question"):
		case ("Create exam code"):
		case ("Extend exam time"):
		case ("Check exam"):
		case ("Lock exam"):
		case ("Update exam"): {

			connect(this);

			switch (pageLabel.getText()) {
			case ("Create exam"): {
				typeComboBox.setItems(FXCollections.observableArrayList("computerized", "manual"));
				break;
			}
			}
			messageToServer[0] = "getSubjects";
			messageToServer[1] = getMyUser().getUsername();
			messageToServer[2] = null;
			chat.handleMessageFromClientUI(messageToServer);// send the message to server
			break;

		}

		}

	}

	/*********************************************************** Opening screens action-events ***************************************/
	
	
	public void justTest(ActionEvent e) throws IOException
	{
		AnchorPane myPane = FXMLLoader.load(getClass().getResource("/boundary/CreateExamCode.fxml"));
		mainPane.getChildren().setAll(myPane);
	}
	
	public void justTest2(ActionEvent e) throws IOException
	{
		AnchorPane myPane = FXMLLoader.load(getClass().getResource("/boundary/CreateQuestion.fxml"));
		mainPane.getChildren().setAll(myPane);
	}
	
	public void openScreen(ActionEvent e, String screen) throws IOException{

		 Parent tableViewParent = FXMLLoader.load(getClass().getResource("/boundary/" + screen + ".fxml"));
	        Scene tableViewScene = new Scene(tableViewParent);
	        tableViewScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
	        //This line gets the Stage information
	        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
	        window.setScene(tableViewScene);
	        window.show();
	}
	
	/* open the screen ExtendExamTime */
	public void openExtendExamTimeScreen(ActionEvent e) throws IOException{
		openScreen(e,"ExtendExamTime");
	}

	/* open the screen UpdateQuestion */
	public void openUpdateQuestionScreen(ActionEvent e) throws IOException{
		openScreen(e,"UpdateQuestion");
	}
	


	/* open the screen CreateExamCode */
	public void openExamCodeScreen(ActionEvent e) throws IOException{
		openScreen(e,"CreateExamCode");
	}

	/* open the screen CreateExam */
	public void openCreateExam(ActionEvent e) throws IOException{
		openScreen(e,"CreateExam");
	}

	/* open the screen CreateQuestion */
	public void openCreateQuestion(ActionEvent e) throws IOException{
		openScreen(e,"CreateQuestion");
	}

	/* open the screen UpdateExam */
	public void openUpdateExamScreen(ActionEvent e) throws IOException{
		openScreen(e,"UpdateExam");
	}
	
	/* open the screen CheckExam */
	public void openCheckExamScreen(ActionEvent e) throws IOException{
		openScreen(e,"CheckExam");
	}



	/* opening the screen LockExam */
	public void openLockExamScreen(ActionEvent e) throws IOException {
		openScreen("LockExam");
	}

	/* open the screen ErrorMessage and sending an object */
	public void openScreen(String screen, Object message) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
			Stage stage = Main.getStage();
			ErrorControl tController = loader.getController();
			
			tController.setBackwardScreen(stage.getScene());/* send the name to the controller */
			tController.setErrorMessage((String) message);// send a the error to the alert we made
			stage.setTitle("Error message");
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}
	}
	
	/* open the screen ErrorMessage and sending an object */
	public void openScreen(String screen) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/boundary/" + screen + ".fxml"));
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
			Stage stage = Main.getStage();
			stage.setTitle("Error message");
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error in opening the page");
		}
	}

	/* close button was pressed */
	public void closeScreen(ActionEvent e) throws IOException, SQLException {
		final Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
		questionInExamObservable.clear();
		openScreen("HomeScreenTeacher");
	}

	/************************************************** update question screen *******************************************************/
	
	/* make a new question to save the oldest question before changing it */
	public Question createBackUpQuestion(Question questionSelected) {
		return new Question(questionSelected.getId(), questionSelected.getTeacherName(),
				questionSelected.getQuestionContent(), questionSelected.getAnswer1(), questionSelected.getAnswer2(),
				questionSelected.getAnswer3(), questionSelected.getAnswer4(), questionSelected.getCorrectAnswer());
	}

	/* request to load questions to table view */
	public void loadQuestions(ActionEvent e) throws IOException {
		/* ask for the qustions text */
		String subject = subjectsComboBox.getValue(); // get the subject code
		if (subject == null)
			return;
		String course = coursesComboBox.getValue();
		if(course == null)
			return;
		String[] subjectSubString = subject.split("-");
		String[] coursesSubString = course.split("-");
		connect(this); // connecting to server
		messageToServer[0] = "getQuestionsToTable";
		messageToServer[1] = subjectSubString[0].trim()+""+coursesSubString[0].trim();
		messageToServer[2] = getMyUser().getUsername();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

	/* change the question content on table view */
	public void changeQuestionContentOnTable(CellEditEvent<Question, String> edittedCell) {

		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		oldQuestion = createBackUpQuestion(questionSelected);
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getQuestionContent())) {
			questionSelected.setQuestionContent(edittedCell.getNewValue().toString());
			updateQuestion(questionSelected);
		}
	}

	/* change the answer 1 on table view */
	public void changeAnswer1OnTable(CellEditEvent<Question, String> edittedCell) {
		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		oldQuestion = createBackUpQuestion(questionSelected);
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getAnswer1())) {
			questionSelected.setAnswer1(edittedCell.getNewValue().toString());
			updateQuestion(questionSelected);
		}
	}

	/* change the answer 2 content on table view */
	public void changeAnswer2OnTable(CellEditEvent<Question, String> edittedCell) {
		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		oldQuestion = createBackUpQuestion(questionSelected);
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getAnswer2())) {
			questionSelected.setAnswer2(edittedCell.getNewValue().toString());
			updateQuestion(questionSelected);

		}
	}

	/* change the answer 3 content on table view */
	public void changeAnswer3OnTable(CellEditEvent<Question, String> edittedCell) {
		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		oldQuestion = createBackUpQuestion(questionSelected);
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getAnswer3())) {
			questionSelected.setAnswer3(edittedCell.getNewValue().toString());
			updateQuestion(questionSelected);

		}
	}

	/* change the answer 4 content on table view */
	public void changeAnswer4OnTable(CellEditEvent<Question, String> edittedCell) {
		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		oldQuestion = createBackUpQuestion(questionSelected);
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getAnswer4())) {
			questionSelected.setAnswer4(edittedCell.getNewValue().toString());
			updateQuestion(questionSelected);
		}
	}

	/* change the correct answer on table view */
	public void changeCorrectAnswerOnTable(CellEditEvent<Question, String> edittedCell) {
		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		oldQuestion = createBackUpQuestion(questionSelected);
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getCorrectAnswer())) {
			questionSelected.setCorrectAnswer(edittedCell.getNewValue().toString());
			updateQuestion(questionSelected);

		}
	}

	/* updating the question that has been selected */
	public void updateQuestion(Question questionSelected) {
		messageToServer[0] = "updateQuestion";
		messageToServer[1] = questionSelected;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer); // send the request to the server
	}

	/* deleting question from the tableview and from the database */
	public void deleteQuestion(ActionEvent e) {
		questionSelected = questionTableView.getSelectionModel().getSelectedItem();
		if(questionSelected == null)
		{
			openScreen("ErrorMessage", "Please select question");
			return;
		}
		messageToServer[0] = "deleteQuestion";
		messageToServer[1] = questionSelected;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer); // send the request to the server
	}

	/********************************************************** create question screen ***********************************************/
	
	/* create a new question */
	public void createQuestionClick(ActionEvent e) throws IOException {
		if (subjectsComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please choose subject");
			return;
		}
		if (courseInCreateQuestion.getItems().isEmpty()) {
			openScreen("ErrorMessage", "Please choose course");
			return;
		}
		Question question;

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

		if ((answer1.getText().equals("")) || ((answer2.getText().equals(""))) || (answer3.getText().equals(""))
				|| (answer4.getText().equals("")) || (correctAnswer == 0) || (questionName.getText().equals(""))) {
			openScreen("ErrorMessage", "Not all fields are completely full");
		} else {
			question = new Question(null, getMyUser().getUsername(), questionName.getText().trim(),
					answer1.getText().trim(), answer2.getText().trim(), answer3.getText().trim(),
					answer4.getText().trim(), String.valueOf(correctAnswer));
			String subject = subjectsComboBox.getValue();
			String[] subjectSubString = subject.split("-");
			ArrayList<String> courses = (ArrayList<String>) coursesListToCreateQuestion.stream()
					.collect(Collectors.toList());
			connect(this); // connecting to server
			messageToServer[0] = "SetQuestion";
			messageToServer[1] = subjectSubString[0].trim();
			messageToServer[2] = question;
			messageToServer[3] = courses;
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
			chat.closeConnection();// close the connection
		}
	}


    public void coursesToList(ActionEvent event) {
    	
    	try {
    	
    	String[] courseSubString = coursesComboBox.getValue().split("-");
			if (!coursesListToCreateQuestion.contains(courseSubString[0] + courseSubString[1])) {
				coursesListToCreateQuestion.add(courseSubString[0] + courseSubString[1]);
				courseInCreateQuestion.setItems(coursesListToCreateQuestion);
				subjectsComboBox.setDisable(true);
			}
    	}
    	catch (NullPointerException e) {
    		
    	}
    }

    public void removeCoursesFromList(ActionEvent event) {
    	if(courseInCreateQuestion.getSelectionModel().getSelectedItem() != null)
    	{
    		coursesListToCreateQuestion.remove(courseInCreateQuestion.getSelectionModel().getSelectedItem());
	    	coursesComboBox.getSelectionModel().clearSelection();
	    	if(courseInCreateQuestion.getItems().isEmpty())
	    		subjectsComboBox.setDisable(false);
    	}
    }
	/************************************** Update exam screen ***********************************************************************/
	
	/* requesting the exams from the database */
	public void loadExams(ActionEvent e) throws IOException {
		String examIDStart;
		String toSend;
		if(!pageLabel.getText().equals("Create exam code") && !pageLabel.getText().equals("Update exam"))
		{
			toSend = "getExecutedExams";
			messageToServer[2] = getMyUser().getUsername();
		}
		else
			toSend = "getExams";
		/* ask for the exams name */
		if (coursesComboBox.getValue() == null)
			return;
		String[] subjectSubString = subjectsComboBox.getValue().split("-");
		String[] examSubString = coursesComboBox.getValue().split("-");
		examIDStart = subjectSubString[0].trim() + "" + examSubString[0].trim();
		if (examIDStart.equals("") || examIDStart == null)
			return;
		connect(this); // connecting to server
		messageToServer[0] = toSend;
		messageToServer[1] = examIDStart;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

	public void loadStudenstInExam (ActionEvent e) throws IOException{
		connect(this); // connecting to server
		messageToServer[0] = "getStudenstInExam";
		messageToServer[1] = executedExamsComboBox.getValue();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}
	
	/* changing the remarks for teacher */
	public void changeRemarksForTeacherOnTable(CellEditEvent<Exam, String> edittedCell) throws IOException {
		examSelected = examsTableView.getSelectionModel().getSelectedItem();
		oldExam = new Exam();
		oldExam.setE_id(examSelected.getE_id());
		oldExam.setSolutionTime(examSelected.getSolutionTime());
		oldExam.setRemarksForTeacher(examSelected.getRemarksForTeacher());
		oldExam.setRemarksForStudent(examSelected.getRemarksForStudent());
		oldExam.setType(examSelected.getType());
		oldExam.setTeacherUserName(examSelected.getTeacherUserName());

		if (!edittedCell.getNewValue().toString().equals(examSelected.getRemarksForTeacher())) {
			examSelected.setRemarksForTeacher(edittedCell.getNewValue().toString());
			updateExam(examSelected);
		}
	}

	/* changing the remarks for student on the table view */
	public void changeRemarksForStudentOnTable(CellEditEvent<Exam, String> edittedCell) throws IOException {
		examSelected = examsTableView.getSelectionModel().getSelectedItem();
		oldExam = new Exam();
		oldExam.setE_id(examSelected.getE_id());
		oldExam.setSolutionTime(examSelected.getSolutionTime());
		oldExam.setRemarksForTeacher(examSelected.getRemarksForTeacher());
		oldExam.setRemarksForStudent(examSelected.getRemarksForStudent());
		oldExam.setType(examSelected.getType());
		oldExam.setTeacherUserName(examSelected.getTeacherUserName());
		if (!edittedCell.getNewValue().toString().equals(examSelected.getRemarksForStudent())) {
			examSelected.setRemarksForStudent(edittedCell.getNewValue().toString());
			updateExam(examSelected);
		}
	}

	/* changing the type of the exam */
	public void changeTypeOnTable(CellEditEvent<Exam, String> edittedCell) throws IOException {
		examSelected = examsTableView.getSelectionModel().getSelectedItem();
		oldExam = new Exam();
		oldExam.setE_id(examSelected.getE_id());
		oldExam.setSolutionTime(examSelected.getSolutionTime());
		oldExam.setRemarksForTeacher(examSelected.getRemarksForTeacher());
		oldExam.setRemarksForStudent(examSelected.getRemarksForStudent());
		oldExam.setType(examSelected.getType());
		oldExam.setTeacherUserName(examSelected.getTeacherUserName());
		if (!edittedCell.getNewValue().toString().equals(examSelected.getType())) {
			examSelected.setType(edittedCell.getNewValue().toString());
			updateExam(examSelected);
		}
	}
	
	/* get the question in a specific exam */
	public void viewQuestion(ActionEvent e) throws IOException {
		try {
			temp = e;
			Exam exam = examsTableView.getSelectionModel().getSelectedItem();
			connect(this); // connecting to server
			messageToServer[0] = "getQuestionInExam";
			messageToServer[1] = exam.getE_id();
			tempExamId = exam.getE_id();
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
		} catch (NullPointerException exception) {
			openScreen("ErrorMessage", "Please select exam");
		}
	}
	
	/* updating the exam in the database */
	public void updateExam(Exam examSelected) {
		messageToServer[0] = "updateExam";
		messageToServer[1] = examSelected;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer); // send the request to the server
	}
	
	/* removing the exam from the database */
	public void deleteExam(ActionEvent e) {
			examSelected = examsTableView.getSelectionModel().getSelectedItem();
			if(examSelected == null)
			{
				openScreen("ErrorMessage", "Please select exam");
				return;
			}
			messageToServer[0] = "deleteExam";
			messageToServer[1] = examSelected;
			connect(this);
			chat.handleMessageFromClientUI(messageToServer); // send the request to the server
	}
	
	
	/************************************** (Create + Update) questions in exam screens ***********************************************/
	
	/* creating exam */
	@SuppressWarnings("static-access")
	public void createExam(ActionEvent e) {
		int sumOfPoints = 0;
		if (timeForExamHours.getText().equals("") || timeForExamMinute.getText().equals("")
				|| Integer.valueOf(timeForExamHours.getText()) < 0) {
			openScreen("ErrorMessage", "Please fill time for exam");
			return;
		}
		if (typeComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please select the type of exam");
			return;
		}
		if ((Integer.parseInt(timeForExamHours.getText()) <= 0 && Integer.parseInt(timeForExamMinute.getText()) <= 0)
				|| (Integer.parseInt(timeForExamHours.getText()) > 99
						|| Integer.parseInt(timeForExamMinute.getText()) > 99)) {
			openScreen("ErrorMessage", "invalid time");
			return;
		}

		for (QuestionInExam q : questionInExamObservable) {
			sumOfPoints += q.getPoints();
			if (q.getPoints() == 0) {
				openScreen("ErrorMessage", "You cant set a question with 0 points.");
				return;
			}
		}
		if (sumOfPoints != 100) {
			openScreen("ErrorMessage", "Points are not match to 100");
			return;
		}
		Exam exam = new Exam();// creating a new exam;
		Time time = null;
		String[] courseID = coursesComboBox.getValue().split("-");// we want the course id
		String[] subjectSubString = subjectsComboBox.getValue().split("-");
		exam.setE_id(subjectSubString[0].trim() + "" + courseID[0].trim());// making the start of the id of the exam
		ArrayList<QuestionInExam> questioninexam = (ArrayList<QuestionInExam>) questionInExamObservable.stream()
				.collect(Collectors.toList());// making the observable a lis
		exam.setRemarksForStudent(remarksForStudent.getText());
		exam.setRemarksForTeacher(remarksForTeacher.getText());
		exam.setTeacherUserName(getMyUser().getUsername());
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
			e1.printStackTrace();
		}
	}

	/* moving the question to the question in exam table view */
	@SuppressWarnings("unchecked")
	public void toQuestionInExam(ActionEvent e) {
		int flag = 0;
		if (questionTableView.getSelectionModel().getSelectedItem() == null) {
			openScreen("ErrorMessage", "Please choose question");
			return;
		}
		if(!pageLabel.getText().equals("Update question in exam"))
		{
		subjectsComboBox.setDisable(true);
		coursesComboBox.setDisable(true);
		}
		QuestionInExam questioninexam = new QuestionInExam();// creating new questioninexam
		Question questionDetails = questionTableView.getSelectionModel().getSelectedItem();
		questioninexam.setQuestionID(questionDetails.getId());
		questioninexam.setTeacherUserName(questionDetails.getTeacherName());
		questioninexam.setQuestionContent(questionDetails.getQuestionContent());
		questioninexam.setPoints(0);
		setToQuestionInExamTableView();
		for (QuestionInExam item : questionInExamObservable) {
			if (item.getQuestionID().equals(questionDetails.getId()))
				flag = 1;
		}
		if (flag == 0) {
			questionObservableList.remove(questionTableView.getSelectionModel().getSelectedIndex());
			questionInExamObservable.add(questioninexam);
			questionsInExamTableView.getSortOrder().setAll(questionNameTableView);
		}

	}

	/* setting the question in the table view (question in exam) */
	private void setToQuestionInExamTableView() {
		questionPointsTableView.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
		questionNameTableView.setCellValueFactory(new PropertyValueFactory<>("questionID"));// display the id in the
																							// table view
		questionPointsTableView.setCellValueFactory(new PropertyValueFactory<>("points"));// display the points in table
																							// view // the
		questionsInExamTableView.setItems(questionInExamObservable);
	}

	/* removing the question from the tableview */
	@SuppressWarnings("unchecked")
	public void removeFromTableView(ActionEvent e) {
		ObservableList<QuestionInExam> questiontoremove;
		int flag = 0;
		try {
			questiontoremove = questionsInExamTableView.getSelectionModel().getSelectedItems();
			Question question = new Question();
			question.setQuestionContent(questiontoremove.get(0).getQuestionContent());
			question.setTeacherName(questiontoremove.get(0).getTeacherUserName());
			question.setId(questiontoremove.get(0).getQuestionID());
			for (Question item : questionObservableList) {
				if (item.getId().equals(question.getId()))
					flag = 1;
			}
			if (flag == 0) {
				questionObservableList.add(question);
				questionTableView.getSortOrder().setAll(qid);
			}

			questiontoremove.forEach(questionInExamObservable::remove);
			if(questionInExamObservable.isEmpty()) {
				subjectsComboBox.setDisable(false);
				coursesComboBox.setDisable(false);
			}
		} catch (RuntimeException exception) {
			openScreen("ErrorMessage", "Please choose question to delete");
			return;
		}
		// add the question back to the tableview
	}

	/* removing the question from the tableview */
	public void updateQuestionInExam(ActionEvent e) {
		int sumOfPoints = 0;
		for (QuestionInExam q : questionInExamObservable) {
			sumOfPoints += q.getPoints();
			if (q.getPoints() == 0) {
				openScreen("ErrorMessage", "You cant set a question with 0 points.");
				return;
			}
		}
		if (sumOfPoints != 100) {
			openScreen("ErrorMessage", "Points are not match to 100");
			return;
		}
		ArrayList<QuestionInExam> questioninexam = (ArrayList<QuestionInExam>) questionInExamObservable.stream()
				.collect(Collectors.toList());// making the observable a lis
		messageToServer[0] = "updateQuestionInExam";
		messageToServer[1] = questioninexam;
		messageToServer[2] = tempExamId;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer);// send the message to server
		try {
			chat.closeConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/* set new points in the table view */
	@SuppressWarnings("unlikely-arg-type")
	public void setPoints(CellEditEvent<QuestionInExam, Float> edittedCell) {
		QuestionInExam questionSelected = questionsInExamTableView.getSelectionModel().getSelectedItem();
		if (!edittedCell.getNewValue().toString().equals(questionSelected.getPoints())) {
			questionSelected.setPoints(edittedCell.getNewValue());
		}
		if (pageLabel.getText().equals("Update question in exam")) {
			updateBtn.setDisable(false);

		}
		backButton.setDisable(false);
	}

	/* event for locking the back button when u editing points */
	public void blockBackButton() {
		backButton.setDisable(true);
	}
	
	/********************************************* Create exam code screen ***********************************************************/

	/* creating exam code */
	public void createExamCode(ActionEvent e) {
		ExecutedExam exam;
		String examID = examComboBox.getValue();
		String executedExamId = examCode.getText();
		if (subjectsComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please choose subject");
			return;
		}
		if (coursesComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please choose course");
			return;
		}
		if (examComboBox.getValue() == null) {
			openScreen("ErrorMessage", "Please choose exam");
			return;
		}
		if (executedExamId.length() != 4) {
			openScreen("ErrorMessage", "You must enter exactly 4 letters & number");
			return;
		}
		for (int i = 0; i < executedExamId.length(); i++) {
			char ch = executedExamId.charAt(i);

			if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))) {
				openScreen("ErrorMessage", "You must enter only letters and numbers.");
				return;
			}
		}
		exam = new ExecutedExam();
		exam.setExecutedExamID(executedExamId);
		exam.setTeacherName(getMyUser().getUsername());
		exam.setExam_id(examID);
		messageToServer[0] = "setExamCode";
		messageToServer[1] = exam;
		connect(this);
		chat.handleMessageFromClientUI(messageToServer);
		
	}

	/* loading courses from database by subject*/
	public void loadCourses(ActionEvent e) throws IOException {
		/* ask for the courses name */
		try {
			String subject = subjectsComboBox.getValue(); // get the subject code
			String[] subjectSubString = subject.split("-");
			coursesComboBox.getSelectionModel().clearSelection();
			connect(this); // connecting to server
			messageToServer[0] = "getCourses";
			messageToServer[1] = subjectSubString[0].trim();
			messageToServer[2] = getMyUser().getUsername();
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
		} catch (NullPointerException exception) {
			return;
		}
	}

	/********************************************* Extend exam time screen ***********************************************************/

	/* creating an extend time request */
	public void createExtendTimeRequest(ActionEvent e) throws IOException {
		if (timeForExamHours.getText().equals("") || timeForExamMinute.getText().equals("")) {
			openScreen("ErrorMessage", "Please fill the time you want to extend by");
			return;
		}
		if (reasonForChange.getText().trim().equals("")) {
			openScreen("ErrorMessage", "Please fill the reason for changing the time");
			return;
		}
		ExecutedExam executedexam = executedExamTableView.getSelectionModel().getSelectedItem();
		if (executedexam == null) {
			openScreen("ErrorMessage", "Please choose an exam");
			return;
		}
		RequestForChangingTimeAllocated request = new RequestForChangingTimeAllocated();
		request.setIDexecutedExam(executedexam.getExecutedExamID());
		request.setReason(reasonForChange.getText());
		request.setMenagerApprove("waiting");
		request.setTeacherName(getMyUser().getUsername());
		request.setTimeAdded(timeForExamHours.getText() + "" + timeForExamMinute.getText());
		connect(this); // connecting to server
		messageToServer[0] = "createChangingRequest";
		messageToServer[1] = request;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
		chat.closeConnection();
	}
	
	/*********************************************************** TOM ???? ************************************************************/

	/* locking the subject function (subject combobox) */
	public void lockSubject(ActionEvent e) {
		subjectsComboBox.setDisable(true);
	}

	/* locking the exam */
	public void lockExam(ActionEvent e) throws IOException {
		ExecutedExam executedexam = executedExamTableView.getSelectionModel().getSelectedItem();
		if (executedexam == null) {
			openScreen("ErrorMessage", "Please choose an exam");
			return;
		}
		connect(this); // connecting to server
		messageToServer[0] = "setExecutedExamLocked";
		messageToServer[1] = executedexam.getExecutedExamID();
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}
	
	/*********************************************************** Check exam ************************************************************/

}
