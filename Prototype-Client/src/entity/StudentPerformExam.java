package entity;

import java.io.Serializable;

public class StudentPerformExam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date = " ";
	private String time = " ";
	private Boolean finished = false ;
	private Float grade ;
	private String excecutedExamID = " ";
	private String userName = " ";
	private Boolean isApproved = false ;
	private String reasonForChangeGrade = " "; 
	private String studentId = " "; 
	private String studentFullName = " "; 

	public StudentPerformExam(String dateDB,String timeDB, String finishedDB,
			String excecutedExamIDDB,String userNameDB,String gradeDB,String isApprovedDB,String reasonForChangeGradeDB,String id,String fullName) {
		super();
		date = dateDB;
		time = timeDB;
		if (finishedDB == "1") {
			finished = true;
		}
		grade = Float.parseFloat(gradeDB);
		excecutedExamID = excecutedExamIDDB ; 
		userName = userNameDB ; 
		if (isApprovedDB == "1") {
			isApproved = true;
		}
		reasonForChangeGrade = reasonForChangeGradeDB ; 
		this.studentId = id;
		this.studentFullName = fullName;
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

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Float getGrade() {
		return grade;
	}

	public void setGrade(Float grade) {
		this.grade = grade;
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

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getReasonForChangeGrade() {
		return reasonForChangeGrade;
	}

	public void setReasonForChangeGrade(String reasonForChangeGrade) {
		this.reasonForChangeGrade = reasonForChangeGrade;
	}

	
}
