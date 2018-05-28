package entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String questionContent;
	private ArrayList<String> answers;
	private int correctAnswer;
	private String teacherName; // The teacher who wrote the question
	
	public Question() {
		answers= new ArrayList<String>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public int getTrueAnswer() {
		return correctAnswer;
	}

	public void setTrueAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
}
