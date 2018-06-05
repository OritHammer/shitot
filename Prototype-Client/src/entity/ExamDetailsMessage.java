package entity;

import java.io.Serializable;

public class ExamDetailsMessage implements Serializable {

	private String examID ;
	private String examDate;
	private String examGrade;
	
	public ExamDetailsMessage(String eID,String eDate,String eGrade) {
		examID = eID;
		examDate = eDate;
		examGrade = eGrade;
	}

	public String getExamID() {
		return examID;
	}

	public void setExamID(String examID) {
		this.examID = examID;
	}

	public String getExamDate() {
		return examDate;
	}

	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	public String getExamGrade() {
		return examGrade;
	}

	public void setExamGrade(String examGrade) {
		this.examGrade = examGrade;
	}
	
	
}
