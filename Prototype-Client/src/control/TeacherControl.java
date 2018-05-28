package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class TeacherControl extends  UserControl implements Initializable  {

	private Boolean trueAnsFlag=false;// 
	String selectedQuestion;
	String subject;
	Question questionDetails;
	String[] messageToServer=new String[3];
	
	/* fxml variables */
	@FXML
	private Text userText;

	@FXML
	private TextField answer1;
	@FXML
	private TextField answer2;
	@FXML
	private TextField answer3;
	@FXML
	private TextField answer4;
	@FXML
	private TextField questionID;
	@FXML
	private TextField teacherName;
/*buttons of display the correct answer*/
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
	private Tab createQuestion;
	@FXML
	public void initializeQuestions() {
		connect(this); 
		messageToServer[0]="getSubjects";
		messageToServer[1]=null;
		messageToServer[2]=null;
		chat.handleMessageFromClientUI(messageToServer);//send the message to server
	}
	
	@FXML
	private ComboBox<String> subjectsComboBoxInCreate;
	@FXML
	private ComboBox<String> questionsComboBox;
	@FXML
	private ComboBox<String> subjectsComboBox;

/*initialized the update Question window*/
	
	public void loadQuestions(ActionEvent e) throws IOException {
		/*ask for the qustions text*/
		questionsComboBox.getSelectionModel().clearSelection(); 
		clearForm();
		subject = subjectsComboBox.getValue(); // get the subject code
		if(subject==null)
			return;
		connect(this); //connecting to server
		messageToServer[0]="getQuestions";
		messageToServer[1]=subject;
		messageToServer[2]=null;
		chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
	}

	public void askForQuestionDetails(ActionEvent e) throws IOException {
		
		selectedQuestion = questionsComboBox.getValue(); // get the selected question
		if(selectedQuestion==null)
			return;
		connect(this); // connecting to server
		messageToServer[0]="getQuestionDetails";
		messageToServer[1]=selectedQuestion;
		messageToServer[2]=null;
		clearForm();
		if(selectedQuestion!=null)
		chat.handleMessageFromClientUI(messageToServer);// ask for details of specific question
		
	}

	/* this method show the subjects list on the combobox */
	public void showSubjects(ArrayList<String> subjectList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
		if(createQuestion.isSelected()) {
			subjectsComboBoxInCreate.setItems(observableList);
		}
		else {
			subjectsComboBox.setItems(observableList);
		}
	}

	/* this method show the questions list on the combobox */
	public void showQuestions(ArrayList<String> questionsList) 
	{
		try {
		ObservableList<String> observableList = FXCollections.observableArrayList(questionsList);
		questionsComboBox.setItems(observableList);
		}
		catch(Exception e) {}
	}

	/* this method show the question details to user*/
	public void showQuestionDetails(Question q){
		try {
		trueAnsFlag=true;// Permit to user change the correct answer
		questionID.setText(q.getId());
		teacherName.setText(q.getTeacherName());
		ArrayList<String> answers = q.getAnswers();
		answer1.setText(answers.get(0));
		answer2.setText(answers.get(1));
		answer3.setText(answers.get(2));
		answer4.setText(answers.get(3));
		
		/*set up the correct answer button*/
		switch (q.getTrueAnswer()) {/*The number of the correct answers*/
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
		}
		catch(Exception e) {}
	}

	
/*send to server request for update correct answer*/
	public void updateCorrectAnswer(ActionEvent e) throws IOException, SQLException {
		
		
		if (trueAnsFlag) {//if there is permission to update the question
			String qID=questionID.getText();
			RadioButton selected = (RadioButton)group.getSelectedToggle();
			String selectedId=selected.getId();
			messageToServer[0]="updateCorrectAnswer";
			messageToServer[1]=qID;
			messageToServer[2]= selectedId;
			connect(this);
			chat.handleMessageFromClientUI(messageToServer); //send the request to the server
			chat.closeConnection();
		}
	}
	/*cancel button was pressed*/
	public void cancel(ActionEvent e) throws IOException, SQLException {
		System.exit(0);/*close the program (of client only)*/
		
	}
	

	/* check the content message from server */
	@SuppressWarnings("unchecked")
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();//close the connection

			Object[] msg = (Object[]) message;
			switch (msg[0].toString()) {
			case ("getSubjects"): /* get the subjects list from server */
			{
				showSubjects((ArrayList<String>) msg[1]);
				break;
			}
			case ("getQuestions"): /* get the questions list from server */
			{
				showQuestions((ArrayList<String>) msg[1]);
				break;
			}
			case ("getQuestionDetails"): /* get the subject list from server */
			{
				showQuestionDetails((Question) msg[1]);
				break;
			}
			default:{
				System.out.println("Error in input");
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	/*clear all the text fields and radio buttons*/
	public void clearForm()
	{
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

   public void initialize(URL url, ResourceBundle rb) {
		userText.setText(userNameFromDB);
	}
 
}
