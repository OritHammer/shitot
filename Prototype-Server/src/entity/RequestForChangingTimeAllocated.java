package entity;

import java.io.Serializable;

public class RequestForChangingTimeAllocated implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String RequestID;
	private String teacherName;
	private String menagerApprove; 
	private String reason;
	private String timeAdded;
	private String IDexecutedExam;
	
	public RequestForChangingTimeAllocated() {
		menagerApprove="waiting";
	}
<<<<<<< HEAD
	
	public String getRequestID() { 
=======
	public RequestForChangingTimeAllocated(String rID,String tName,String approved,String reason,String time,String examID) {
		menagerApprove="waiting";
		RequestID=rID;
		teacherName=tName;
		menagerApprove=approved;
		this.reason=reason;
		timeAdded=time;
		IDexecutedExam=examID;
	}
	public String getRequestID() {
>>>>>>> branch 'master' of https://github.com/avivMahulya/shitot.git
		return RequestID;
	}
	public void setRequestID(String requestID) {
		RequestID = requestID;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getMenagerApprove() {
		return menagerApprove;
	}
	public void setMenagerApprove(String menagerApprove) {
		this.menagerApprove = menagerApprove;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTimeAdded() {
		return timeAdded;
	}
	public void setTimeAdded(String timeAdded) {
		this.timeAdded = timeAdded;
	}
	
}
