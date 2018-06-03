package entity;

import java.io.Serializable;

public class QuestionInExam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String examID;
	private String questionID;
	private int questionIndexInExam;
	private float points;
	private String name;
	
	public float getPoints() {
		return points;
	}
	public void setPoints(float points) {
		this.points = points;
	}
	public int getQuestionIndexInExam() {
		return questionIndexInExam;
	}
	public void setQuestionIndexInExam(int questionIndexInExam) {
		this.questionIndexInExam = questionIndexInExam;
	}
	public String getQuestionID() {
		return questionID;
	}
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}
	public String getExamID() {
		return examID;
	}
	public void setExamID(String examID) {
		this.examID = examID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}