package entity;

import java.io.Serializable;
import java.util.Date;

public class ExamDetailsMessage implements Serializable {

	private String examID ;
	private Integer examGrade;
	private Date examDate;
	
	
	public ExamDetailsMessage(String eID,Integer eGrade,Date eDate ) {
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


	public Integer getExamGrade() {
		return examGrade;
	}


	public void setExamGrade(Integer examGrade) {
		this.examGrade = examGrade;
	}


	public Date getExamDate() {
		return examDate;
	}


	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	
}
