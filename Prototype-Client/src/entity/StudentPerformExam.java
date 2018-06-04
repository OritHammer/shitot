package entity;

import java.io.Serializable;

public class StudentPerformExam implements Serializable{
	private String date = " ";
	private String time = " ";
	private String finished = "0" ;
	private String excecutedExamID = " ";
	private String userName = " ";
	private String isApproved = "0" ;
	private String reasonForChangeGrade = " "; 
	 
	public StudentPerformExam(String dateDB,String timeDB, String finishedDB,String excecutedExamIDDB,String userNameDB,String isApprovedDB,String reasonForChangeGradeDB) {
		super();
		date = dateDB;
		time = timeDB;
		finished = finishedDB;
		excecutedExamID = excecutedExamIDDB ; 
		userName = userNameDB ; 
		isApproved = isApprovedDB ; 
		reasonForChangeGrade = reasonForChangeGradeDB ; 
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFinished() {
		return finished;
	}

	public void setFinished(String finished) {
		this.finished = finished;
	}

	public String getExcecutedExamID() {
		return excecutedExamID;
	}

	public void setExcecutedExamID(String excecutedExamID) {
		this.excecutedExamID = excecutedExamID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public String getResonForChangeGrade() {
		return reasonForChangeGrade;
	}

	public void setResonForChangeGrade(String resonForChangeGrade) {
		this.reasonForChangeGrade = resonForChangeGrade;
	}
	
	
}
