package entity;

import java.io.Serializable;

public class RequestForChangingTimeAllocated implements Serializable{
	private String RequestID;
	private String teacherName;
	private String menagerApprove; 
	private String reason;
	private String timeAdded;
	
	public RequestForChangingTimeAllocated() {
		menagerApprove="waiting";
	}
	 
	public String getRequestID() {
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
