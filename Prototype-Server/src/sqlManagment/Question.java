package sqlManagment;

import java.util.ArrayList;

public class Question {
	private String id;
	private String questionContent;
	private ArrayList<String> answers;
	private String correctAnswer;
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

	public String getTrueAnswer() {
		return correctAnswer;
	}

	public void setTrueAnswer(String trueAnswer) {
		this.correctAnswer = trueAnswer;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	
	
	
	
}
