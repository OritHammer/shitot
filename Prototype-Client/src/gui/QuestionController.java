package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import common.ChatIF;
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

public class QuestionController implements Initializable,ChatIF {
	
	private int flag;
	private String ip="192.168.175.50";//server ip
	String quest;
	String subject;
	final public static int DEFAULT_PORT = 5555;
	ChatClient chat;
	
	@FXML
	private ToggleGroup group;

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
	private TextField questionNum;
	@FXML
	private TextField teacherName;
	

	@FXML
	private RadioButton right1;
	@FXML
	private RadioButton right2;
	@FXML
	private RadioButton right3;
	@FXML
	private RadioButton right4;

	@FXML
	private Button button;

	@FXML
	private ComboBox<String> questionsComboBox;
	@FXML
	private ComboBox<String> subjectsComboBox;

	public void loadQuestions(ActionEvent e) throws IOException {
		connect();
		subject = subjectsComboBox.getValue();
		chat.handleMessageFromClientUI("getQuestions "+subject);
		questionsComboBox.getSelectionModel().clearSelection();
	}
	
	public void showAnswers(ActionEvent e) throws IOException {
		/*flag = 1;
		quest = questionsComboBox.getValue();
		ArrayList<String> answerList = myDB.getAnswers(quest);

		answer1.setText(answerList.get(0));
		answer2.setText(answerList.get(1));
		answer3.setText(answerList.get(2));
		answer4.setText(answerList.get(3));
		
		switch (answerList.get(4)) {
			case("1"):{
				right1.setSelected(true);
				break;
			}
			case("2"):{
				right2.setSelected(true);
				break;
			}
			case("3"):{
				right3.setSelected(true);
				break;
			}
			case("4"):{
				right4.setSelected(true);
				break;
			}
		}*/
	}


	public void showSubjects(ArrayList<String> subjectList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
		//subjectsComboBox.getSelectionModel().clearSelection();
		subjectsComboBox.setItems(observableList);
	}
	
	public void showQuestions(ArrayList<String> questionsList) {
		ObservableList<String> observableList = FXCollections.observableArrayList(questionsList);
		questionsComboBox.setItems(observableList);
	}

	  public void connect() 
	  {
		    try 
		    {
		      chat= new ChatClient(ip, DEFAULT_PORT, this);
		    } 
		    catch(IOException exception) 
		    {
		      System.out.println("Error: Can't setup connection!"
		                + " Terminating client.");
		      System.exit(1);
		    }
	  }
		public void updateCorrectAnswer(ActionEvent e) throws IOException, SQLException {
			if (flag == 1) {
				RadioButton selected = (RadioButton) group.getSelectedToggle();
				String newAnswer = selected.getId();
				//myDB.updateAnswer(quest, newAnswer);
			}
		}
	  
	public void initialize(URL arg0, ResourceBundle arg1) {
		connect();
		chat.handleMessageFromClientUI("getSubjects");
	}

	  public void checkMessage(Object message) 
	  {
		  try {
			chat.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Object [] msg=(Object[]) message;
		  switch(msg[0].toString()) {
			  case("getSubjects"):
			  	{
				  showSubjects((ArrayList<String>)msg[1]);
				  break;
			  	}
			  case("getQuestions"):
			  {
				  showQuestions((ArrayList<String>)msg[1]);
				  break;
			  }
		  }
	  }
	  
}
