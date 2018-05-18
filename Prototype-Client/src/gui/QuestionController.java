package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import common.ChatIF;
import logic.Question;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class QuestionController implements Initializable, ChatIF {

	private Boolean trueAnsFlag=false;// 
	String selectedQuestion;
	String subject;
	Question questionDetails;
	String[] messageToServer=new String[3];
	/* connections variables */
	private String ip = "192.168.1.70";// server ip
	final public static int DEFAULT_PORT = 5555;
	ChatClient chat;

	/* fxml variables */
	@FXML
	private TextField text;

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
	private ComboBox<String> questionsComboBox;
	@FXML
	private ComboBox<String> subjectsComboBox;
/*initialized the update Question window*/
	public void initialize(URL arg0, ResourceBundle arg1) {
		connect();
		messageToServer[0]="getSubjects";
		messageToServer[1]=null;
		messageToServer[2]=null;
		chat.handleMessageFromClientUI(messageToServer);
	}
	public void loadQuestions(ActionEvent e) throws IOException {
		connect(); // connecting to server
		subject = subjectsComboBox.getValue(); // get the subject code
		messageToServer[0]="getQuestions";
		messageToServer[1]=subject;
		messageToServer[2]=null;
		chat.handleMessageFromClientUI(messageToServer); // ask from sever the list of question of this subject
		questionsComboBox.getSelectionModel().clearSelection(); // clear the question combobox
	}

	public void askForQuestionDetails(ActionEvent e) throws IOException {
		connect(); // connecting to server
		selectedQuestion = questionsComboBox.getValue(); // get the selected question
		messageToServer[0]="getQuestionDetails";
		messageToServer[1]=selectedQuestion;
		messageToServer[2]=null;
		chat.handleMessageFromClientUI(messageToServer);//  separate code
	}

	/* this method show the subjects list on the combobox */
	public void showSubjects(ArrayList<String> subjectList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
		subjectsComboBox.setItems(observableList);
	}

	/* this method show the questions list on the combobox */
	public void showQuestions(ArrayList<String> questionsList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(questionsList);
		questionsComboBox.setItems(observableList);
	}

	/* this method show the question details to user*/
	public void showQuestionDetails(ArrayList<String> q){
		trueAnsFlag=true;// Permit to user change the correct answer
		questionID.setText(q.get(0));
		teacherName.setText(q.get(1));
		answer1.setText(q.get(2));
		answer2.setText(q.get(3));
		answer3.setText(q.get(4));
		answer4.setText(q.get(5));
		/*set up the correct answer button*/
		switch (""+q.get(6)+"") {
		case ("1"): {
			correctAns1.setSelected(true);
			break;
		}
		case ("2"): {
			correctAns2.setSelected(true);
			break;
		}
		case ("3"): {
			correctAns3.setSelected(true);
			break;
		}
		case ("4"): {
			correctAns4.setSelected(true);
			break;
		}
		}
		
	}

	/* this method connected between client and server */
	public void connect(){
		try {
			chat = new ChatClient(ip, DEFAULT_PORT, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}
/*send to server request for update correct answer*/
	public void updateCorrectAnswer(ActionEvent e) throws IOException, SQLException {
		if (trueAnsFlag) {
			RadioButton selected = (RadioButton)group.getSelectedToggle();
			messageToServer[0]="updateCorrectAnswer";
			messageToServer[1]=questionID.getText();
			messageToServer[2]= selected.getId();
			chat.handleMessageFromClientUI(messageToServer); //
		}
	}
	

	/* check the content message from server */
	public void checkMessage(Object message) {
		try {
			chat.closeConnection();

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
				showQuestionDetails((ArrayList<String>) msg[1]);
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

}
