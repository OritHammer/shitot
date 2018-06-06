package entity;

import java.io.Serializable;
import java.util.Date;

public class ExamDetailsMessage implements Serializable {

	private String examID ;
	private String examGrade;
	private String examDate;
	private String examCourse;
	
	public ExamDetailsMessage(String eID,String eGrade,String eDate ) {
		examID = eID;
		examDate = eDate;
		examGrade = eGrade;
		examCourse = examID.substring(2, 4);
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
