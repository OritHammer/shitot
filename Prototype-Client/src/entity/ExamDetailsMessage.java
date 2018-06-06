package entity;

import java.io.Serializable;
import java.util.Date;

public class ExamDetailsMessage implements Serializable {

	private String examID ;
	private String examGrade;
	private String examDate;
	
	public ExamDetailsMessage(String eID,String eGrade,String eDate ) {
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

	public String getExamGrade() {
		return examGrade;
	}

	public void setExamGrade(String examGrade) {
		this.examGrade = examGrade;
	}


	public String getExamDate() {
		return examDate;
	}


	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

}
