package entity;

import java.io.Serializable;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;

public class ExamDetailsMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SimpleStringProperty examID ;
	private final SimpleStringProperty examGrade;
	private final SimpleStringProperty examDate;
	private final SimpleStringProperty examCourse;
	
	public ExamDetailsMessage(String eID,String eGrade,String eDate ) {
		examID = new SimpleStringProperty(eID);
		examDate = new SimpleStringProperty(eDate);
		examGrade = new SimpleStringProperty(eGrade);
		examCourse = new SimpleStringProperty(eID.substring(2, 4));
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SimpleStringProperty getExamID() {
		return examID;
	}

	public SimpleStringProperty getExamGrade() {
		return examGrade;
	}

	public SimpleStringProperty getExamDate() {
		return examDate;
	}

	public SimpleStringProperty getExamCourse() {
		return examCourse;
	}


}
