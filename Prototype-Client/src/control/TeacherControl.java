package control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.sun.nio.sctp.Notification;

import entity.Question;
import entity.TeachingProfessions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	private TextField createAnswer1;
	@FXML
	private TextField createAnswer2;
	@FXML
	private TextField createAnswer3;
	@FXML
	private TextField createAnswer4;
	
	@FXML
	private TextField questionName;
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
	private RadioButton createCorrectAnswer1;
	@FXML
	private RadioButton createCorrectAnswer2;
	@FXML
	private RadioButton createCorrectAnswer3;
	@FXML
	private RadioButton createCorrectAnswer4;
	
	@FXML
	private ToggleGroup group;
	@FXML
	private Button createQuestionBtn;
	
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
	public void createQuestionClick(ActionEvent e)throws IOException{
		Question question=new Question();
		ArrayList<String> answers=new ArrayList<String>();
		question.setQuestionContent(questionName.getText().trim());
		answers.add(createAnswer1.getText().trim());
		answers.add(createAnswer2.getText().trim());
		answers.add(createAnswer3.getText().trim());
		answers.add(createAnswer4.getText().trim());
		
		int correctAnswer=0;
		if(createCorrectAnswer1.isSelected()) {
			correctAnswer=1;
		}
		if(createCorrectAnswer2.isSelected()) {
			correctAnswer=2;
		}
		if(createCorrectAnswer3.isSelected()) {
			correctAnswer=3;
		}
		if(createCorrectAnswer4.isSelected()) {
			correctAnswer=4;
		}
		
		if((answers.get(0).equals("")) ||((answers.get(1).equals("")))||(answers.get(2).equals(""))||(answers.get(3).equals(""))||(correctAnswer==0)||(question.getQuestionContent().equals(""))) {
			   try{
		            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/boundary/ErrorMessage.fxml"));
		            Parent root1 = (Parent) fxmlLoader.load();
		            Stage stage = new Stage();
		            stage.setTitle("Error");
		            stage.setScene(new Scene(root1));  
		            stage.show();
		          }catch(Exception exception) {
		        	  
		          }
		}
		else {
			question.setAnswers(answers);
			question.setTrueAnswer(correctAnswer);
			connect(this); //connecting to server
			messageToServer[0]="SetQuestion";
			messageToServer[1]=subject;
			messageToServer[2]=null;
			chat.handleMessageFromClientUI(messageToServer); // ask from server the list of question of this subject
		}
			
	}
	
	public void loadQuestions(ActionEvent e) throws IOException {
		/*ask for the qustions text*/
		subject = subjectsComboBox.getValue(); // get the subject code
		if(subject==null)
			return;
		questionsComboBox.getSelectionModel().clearSelection(); 
		
		clearForm();
		
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
	public void showSubjects(ArrayList<TeachingProfessions> msg) {
		ObservableList<String> observableList = FXCollections.observableArrayList();
		for(TeachingProfessions tp:msg) {
			observableList.add(tp.getTp_id());
		}
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
				showSubjects((ArrayList<TeachingProfessions>) msg[1]);
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
