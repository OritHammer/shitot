package entity;

import java.io.Serializable;

public class ExecutedExam implements Serializable,Comparable {
	private static final long serialVersionUID = 1L;
	private String executedExamID;
	private int numOfStudentStarted;
	private int numOfStudentFinished;
	private int numOfStudentDidntFinished;
	private float average;
	private float median;
	private String teacherName;
	private String exam_id;
	private int range0to54;
	private int range55to64;
	private int range65to74;
	private int range75to84;
	private int range85to94;
	private int range95to100;
	private String status;
	
	
	public ExecutedExam(String executedExamID, int numOfStudentStarted, int numOfStudentFinished,
			int numOfStudentDidntFinished, float average, float median, String teacherName, String exam_id,
			int range0to54, int range55to64, int range65to74, int range75to84, int range85to94, int range95to100,String status) {
		super();
		this.executedExamID = executedExamID;
		this.numOfStudentStarted = numOfStudentStarted;
		this.numOfStudentFinished = numOfStudentFinished;
		this.numOfStudentDidntFinished = numOfStudentDidntFinished;
		this.average = average;
		this.median = median;
		this.teacherName = teacherName;
		this.exam_id = exam_id;
		this.range0to54 = range0to54;
		this.range55to64 = range55to64;
		this.range65to74 = range65to74;
		this.range75to84 = range75to84;
		this.range85to94 = range85to94;
		this.range95to100 = range95to100;
		this.status=status;
	}
	public int getRange0to54() {
		return range0to54;
	}
	public void setRange0to54(int range0to54) {
		this.range0to54 = range0to54;
	}
	public int getRange55to64() {
		return range55to64;
	}
	public void setRange55to64(int range55to64) {
		this.range55to64 = range55to64;
	}
	public int getRange65to74() {
		return range65to74;
	}
	public void setRange65to74(int range65to74) {
		this.range65to74 = range65to74;
	}
	public int getRange75to84() {
		return range75to84;
	}
	public void setRange75to84(int range75to84) {
		this.range75to84 = range75to84;
	}
	public int getRange85to94() {
		return range85to94;
	}
	public void setRange85to94(int range85to94) {
		this.range85to94 = range85to94;
	}
	public int getRange95to100() {
		return range95to100;
	}
	public void setRange95to100(int range95to100) {
		this.range95to100 = range95to100;
	}
	public ExecutedExam() {
		// TODO Auto-generated constructor stub
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public int compareTo(Object o) {// compare between 2 executedExam by they grade
		ExecutedExam temp=(ExecutedExam)o;
		if (this.average==temp.average) return 0;
		if (this.average>temp.average) return 1;
		return -1;
	}
	
	
}
