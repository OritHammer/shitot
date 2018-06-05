package entity;

import java.io.Serializable;

public class ExecutedExam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String executedExamID;
	private int numOfStudentStarted;
	private int numOfStudentFinished;
	private int numOfStudentDidntFinished;
	private float average;
	private float median;
	private String teacherName;
	private String exam_id;
	private int range0to55;
	private int range55to65;
	private int range65to75;
	private int range75to85;
	private int range85to95;
	private int range95to100;
	
	
	
	public ExecutedExam(String executedExamID, int numOfStudentStarted, int numOfStudentFinished,
			int numOfStudentDidntFinished, float average, float median, String teacherName, String exam_id,
			int range0to55, int range55to65, int range65to75, int range75to85, int range85to95, int range95to100) {
		super();
		this.executedExamID = executedExamID;
		this.numOfStudentStarted = numOfStudentStarted;
		this.numOfStudentFinished = numOfStudentFinished;
		this.numOfStudentDidntFinished = numOfStudentDidntFinished;
		this.average = average;
		this.median = median;
		this.teacherName = teacherName;
		this.exam_id = exam_id;
		this.range0to55 = range0to55;
		this.range55to65 = range55to65;
		this.range65to75 = range65to75;
		this.range75to85 = range75to85;
		this.range85to95 = range85to95;
		this.range95to100 = range95to100;
	}
	public String getExecutedExamID() {
		return executedExamID;
	}
	public void setExecutedExamID(String executedExamID) {
		this.executedExamID = executedExamID;
	}
	public int getNumOfStudentStarted() {
		return numOfStudentStarted;
	}
	public void setNumOfStudentStarted(int numOfStudentStarted) {
		this.numOfStudentStarted = numOfStudentStarted;
	}
	public int getNumOfStudentFinished() {
		return numOfStudentFinished;
	}
	public void setNumOfStudentFinished(int numOfStudentFinished) {
		this.numOfStudentFinished = numOfStudentFinished;
	}
	public int getNumOfStudentDidntFinished() {
		return numOfStudentDidntFinished;
	}
	public void setNumOfStudentDidntFinished(int numOfStudentDidntFinished) {
		this.numOfStudentDidntFinished = numOfStudentDidntFinished;
	}
	public float getAverage() {
		return average;
	}
	public void setAverage(float average) {
		this.average = average;
	}
	public float getMedian() {
		return median;
	}
	public void setMedian(float median) {
		this.median = median;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getExam_id() {
		return exam_id;
	}
	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}
	public int getRange0to55() {
		return range0to55;
	}
	public void setRange0to55(int range0to55) {
		this.range0to55 = range0to55;
	}
	public int getRange55to65() {
		return range55to65;
	}
	public void setRange55to65(int range55to65) {
		this.range55to65 = range55to65;
	}
	public int getRange65to75() {
		return range65to75;
	}
	public void setRange65to75(int range65to75) {
		this.range65to75 = range65to75;
	}
	public int getRange75to85() {
		return range75to85;
	}
	public void setRange75to85(int range75to85) {
		this.range75to85 = range75to85;
	}
	public int getRange85to95() {
		return range85to95;
	}
	public void setRange85to95(int range85to95) {
		this.range85to95 = range85to95;
	}
	public int getRange95to100() {
		return range95to100;
	}
	public void setRange95to100(int range95to100) {
		this.range95to100 = range95to100;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
