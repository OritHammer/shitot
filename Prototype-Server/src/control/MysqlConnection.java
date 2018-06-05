package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Course;
import entity.Exam;
import entity.ExecutedExam;
import entity.Question;
import entity.QuestionInExam;
import entity.RequestForChangingTimeAllocated;
import entity.TeachingProfessionals;
import entity.User;

public class MysqlConnection {
	static Connection conn;
	private Statement stmt;
	ArrayList<TeachingProfessionals> subjectList = null;

	/************************** Class Constructor ********************************/
	public MysqlConnection() {
		/*
		 * used to enter server details Scanner sc = new Scanner(System.in);
		 * System.out.println("enter your DB name: "); serverName = sc.nextLine();
		 * System.out.println("enter your server name: "); userPassword = sc.nextLine();
		 * System.out.println("enter your password: "); DBname = sc.nextLine();
		 */
	}
 
	/**************************** Class Methods **********************************/
	public void runDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/shitot", "root", "1234");
			System.out.println("SQL connection succeed");
			// createTableQuestion();
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	public void setStatusToAddingTimeRequest(Object RequestID,String newStatus) {
		try {
		String reqID=(String)RequestID;
		stmt = conn.createStatement();
		stmt.executeUpdate("Update shitot.requestforchangingtimeallocated SET isApproved='"+newStatus+"' where requestID='"+reqID+"';");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public synchronized void createQuestion(Object subject, Object question) {

		String fullQuestionNumber;
		int questionNumber;
		Question q = (Question) question;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT MAX(Question_id) FROM questions" + " WHERE Question_id like " + "\"" + subject + "%\"" + ";");
			rs.next();
			if((rs.getString(1) == null))
				questionNumber = 0;
			else
				questionNumber = Integer.parseInt(rs.getString(1).substring(2, 5));
			questionNumber = questionNumber+1;
			fullQuestionNumber = (String)subject; 
			fullQuestionNumber = fullQuestionNumber +""+  String.format("%03d", questionNumber);
			stmt. executeUpdate(
			"INSERT INTO shitot.questions VALUES(\""
			+fullQuestionNumber.trim()+"\",\""+q.getTeacherName().trim()+"\",\""
			+q.getQuestionContent()+"\",\""+q.getAnswers().get(0)+"\",\""+q.getAnswers().get(1)+"\",\""
			+q.getAnswers().get(2)+"\",\""+q.getAnswers().get(3)+"\",\""+String.valueOf(q.getTrueAnswer())+"\");");
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public User checkUserDetails(Object userID, Object userPass) {
		try {
			stmt = conn.createStatement();
			// query check existent of such details base on user name and password
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM users WHERE username=\"" + userID + "\" AND password=\"" + userPass + "\""
							+ "AND status = \"unconnected\";");
			// if there is no user with given details
			rs.next();
			if (!rs.first()) {
				return null;
			}
			// if the user is existing
			 User newUser =  new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5) ,
					rs.getString(6)); // in section 5 need to insert "connected"
			/* //updating user status 
			stmt.executeUpdate(
				     "UPDATE users " + 
				       "SET status=\"connected\" WHERE username=\""+userID+"\" AND password=\""+userPass+"\";");//  setting a new status                                                    
				   System.out.println("user set as connected"); */
				   return newUser ; 
			// in the end userDetails will have the UserID,userName,role
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void performLogout(Object userName) {
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE users " + 
				       "SET status=\"unconnected\" WHERE username=\""+userName.toString()+"\";");
			System.out.println("user set as unconnected");  
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<String[]> getPrefExamDetails(String userName){
		ArrayList<String[]> detailsList = new ArrayList<String[]>();
		String[] details =  new String[3] ;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT  E.exam_id  , std.grade  , std.date "
					+ " FROM shitot.executedexam as E , shitot.studentperformedexam as std"
					+ "WHERE E.executedExamID = std.executedexam_id AND std.student_UserName =\""+ userName+"\" AND std.finished = '1'; " );
			
			while(rs.next()) {
			if (!rs.first()) {
				return null;
			}
			details[0] = rs.getString(1);
			details[1] = rs.getString(1);
			details[2] = rs.getString(2);
			detailsList.add(details);
			}
			System.out.println("sending exams details");  
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return detailsList;
	}
	public ArrayList<TeachingProfessionals> getSubjectList(Object teacherUserName) {
		/*
		 * This function separate the subject id from the whole Question id for useful
		 * query
		 */
		if (subjectList == null) {
			subjectList = new ArrayList<TeachingProfessionals>();
			// Statement stmt;
			TeachingProfessionals teachingprofessions;
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT tp.tp_ID,tp.name FROM teachingprofessionals tp,teacherincourse tc,courses c WHERE "
						+ "tp.tp_ID=c.tp_ID AND c.courseID=tc.courseID AND tc.UserNameTeacher=\""+teacherUserName.toString()+"\";");
				while (rs.next()) {
					teachingprofessions = new TeachingProfessionals();
					teachingprofessions.setTp_id(rs.getString(1));
					teachingprofessions.setName(rs.getString(2));
					subjectList.add(teachingprofessions);
				}
				rs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return (subjectList);
	}
	
	public ArrayList<Course> getCourseList(Object subject, Object teacherUserName) {
		/*
		 * The function return the course list by the given subject code
		 */
		// Statement stmt;
		ArrayList<Course> courseList = new ArrayList<Course>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
							"SELECT c.courseID,c.name FROM courses c,teacherincourse tc" +
							" WHERE tp_ID=\""+subject+"\" AND tc.courseID=c.courseID AND tc.UserNameTeacher=\""
							+teacherUserName.toString()+"\";");
			while (rs.next()) {
				courseList.add(new Course(rs.getString(1),rs.getString(2)));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseList;
	}
	public ArrayList<RequestForChangingTimeAllocated> getAddingTimeRequests() {
		/*
		 * The function return the course list by the given subject code
		 */
		ArrayList<RequestForChangingTimeAllocated> requestList = new ArrayList<RequestForChangingTimeAllocated>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM shitot.requestforchangingtimeallocated where isApproved='waiting';");
			while (rs.next()) {
				requestList.add(new RequestForChangingTimeAllocated(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return requestList;
	}
	public ArrayList<String> getQuestionList(Object subject, Object teacherUserName) {
		/*
		 * The function return the question list by the given subject code
		 */
		// Statement stmt;
		ArrayList<String> questionList = new ArrayList<String>();
		String userName =(String)teacherUserName;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT question_id,Question_Text FROM questions,teacherincourse" +
			" WHERE Question_id like " + "\"" + subject + "%\" AND UserNameTeacher=\""+userName+"\"And courseID like \""+subject+"%\";");
			while (rs.next()) {
				questionList.add(rs.getString(1)+"-"+rs.getString(2));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionList;
	}

	public Question getQuestionDetails(Object quest) {
		// ArrayList<String> answerList = new ArrayList<String>();
		// Statement stmt;
		Question question = null;
		try {
			stmt = conn.createStatement();
			// Query return all the details of specific question
			ResultSet rs = stmt.executeQuery(
					"SELECT Question_id,Teacher_Name,answer1,answer2,answer3,answer4,Correct_Answer FROM questions "
							+ "WHERE Question_Text=\"" + quest + "\";");
			// The next commands get the returned details from DB and insert them to
			// question object
 
			rs.next();
			// inserting the data to String List , order by the same order in DB
			question = new Question();
			question.setId(rs.getString(1));
			question.setTeacherName(rs.getString(2));
			ArrayList<String> answers = new ArrayList<String>();
			answers.add(rs.getString(3));
			answers.add(rs.getString(4));
			answers.add(rs.getString(5));
			answers.add(rs.getString(6));
			question.setAnswers(answers);
			question.setTrueAnswer(Integer.parseInt(rs.getString(7)));

			rs.close();

			// end insert details
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}
	//
	
	public RequestForChangingTimeAllocated getAddingTimeRequestsDetails(String requestID) {
	
		RequestForChangingTimeAllocated tmpRequest=null;
		try {
			stmt = conn.createStatement();
			// Query return all the details of specific question
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM requestforchangingtimeallocated "
							+ "WHERE requestID=\"" + requestID + "\";");
			// The next commands get the returned details from DB and insert them to
			// question object
 
			rs.next();
			// inserting the data to String List , order by the same order in DB
			tmpRequest = new RequestForChangingTimeAllocated(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
			
			rs.close();

			// end insert details
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tmpRequest;
	}
	
	//
	public void updateAnswer(Object questionID, Object newAnswer) throws SQLException {
		try {
		// Statement stmt;
		stmt = conn.createStatement();
		// query update on DB the correct answer of question that have the given
		// questionID from client
		stmt.executeUpdate(
				"UPDATE questions SET Correct_answer=\"" + newAnswer + "\" WHERE Question_id=\"" + questionID + "\";");
		System.out.println("question:" + questionID + "new answer:" + newAnswer);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void createExam(Object questionInExams,Object examDetails) {
		ArrayList<QuestionInExam> questionInExam=(ArrayList<QuestionInExam>)questionInExams;
		Exam exam=(Exam)examDetails;
		String fullExamNumber;
		String examNumber=exam.getE_id();
		int examNum;
		int questionCounter=1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT MAX(e_id) FROM exams" + " WHERE e_id like " + "\"" + examNumber + "%\"" + ";");
			rs.next();
			if((rs.getString(1) == null))
				examNum = 0;
			else
				examNum = Integer.parseInt(rs.getString(1).substring(4, 6));
			examNum++;
			fullExamNumber =examNumber; 
			fullExamNumber = fullExamNumber +""+  String.format("%02d", examNum);
			stmt. executeUpdate(
			"INSERT INTO shitot.exams VALUES(\""
			+fullExamNumber.trim()+"\",\""+exam.getSolutionTime()+"\",\""
			+exam.getRemarksForTeacher()+"\",\""+exam.getRemarksForStudent()+"\",\""+exam.getType()+"\",\""
			+exam.getTeacherUserName()+"\");");
			
			for(QuestionInExam q:questionInExam) {
				stmt. executeUpdate(
						"INSERT INTO shitot.questioninexam VALUES(\""
						+fullExamNumber.trim()+"\",\""+q.getQuestionID()+"\",\""
						+(questionCounter++)+"\",\""+q.getPoints()+"\");");
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//stmt. executeUpdate("INSERT INTO shitot.exams VALUES(
		
	}
	
	public synchronized void createExamCode(Object excutedExam) {
		
		ExecutedExam exam=(ExecutedExam)excutedExam;
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT executedExamID FROM shitot.executedexam WHERE executedExamID = \"" + exam.getExecutedExamID() + "\";");
			
			if(rs.isBeforeFirst())
			{
				System.out.println("There is already a code like that, please choose another code");
			rs.close();
				return;
			}
			else {
			stmt. executeUpdate(
			"INSERT INTO shitot.executedexam VALUES(\""
			+exam.getExecutedExamID().trim()+"\",0,0,0,0,0,\""+exam.getTeacherName()+"\",\""+exam.getExam_id()+"\",0,0,0,0,0,0);");
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//stmt. executeUpdate("INSERT INTO shitot.exams VALUES(
		
	}
	/************************************************************director functions*********************************************************************************************/
	public void getRequestsList(Object list) {// get list of executed exam code of exams that exist request to adding them time
		list=new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * FROM shitot.requestforchangingtimeallocated where isApproved='waiting'");
			//insert to table view
		
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		
	}

	public ArrayList<ExecutedExam> getExecutedExam(Object teacherUserName) {//////////////////////////////////////////kaki
		/*
		 * The function return the course list by the given subject code
		 */
		// Statement stmt;
		ArrayList<ExecutedExam> executedexam = new ArrayList<ExecutedExam>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
							"SELECT * FROM executedexam WHERE teacherName=\""+teacherUserName.toString()+"\";");
			while (rs.next()) {
				executedexam.add(new ExecutedExam(rs.getString(1),Integer.parseInt(rs.getString(2)),
						Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)),Float.parseFloat(rs.getString(5)) ,
						Float.parseFloat(rs.getString(6)),rs.getString(7), rs.getString(8),Integer.parseInt(rs.getString(9)),
						Integer.parseInt(rs.getString(10)),Integer.parseInt(rs.getString(11)), Integer.parseInt(rs.getString(12)),
						Integer.parseInt(rs.getString(13)) ,Integer.parseInt(rs.getString(14))));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return executedexam;
	}///////////////////////////////////////////////////////kaki

	public ArrayList<String> getExams(Object examIDStart) {
		ArrayList<String> examList = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
							"SELECT e_id FROM exams WHERE e_id like \""+examIDStart+"%\";");
			while (rs.next()) {
				examList.add(rs.getString(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return examList;	
	}
	
	public synchronized void createChangingRequest(Object requestDetails) {
		RequestForChangingTimeAllocated request = (RequestForChangingTimeAllocated)requestDetails;
		String fullRequestNumber;
		int requestNum;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT MAX(requestID) FROM requestforchangingtimeallocated;");
			rs.next();
			if((rs.getString(1) == null))
				requestNum = 0;
			else
				requestNum = Integer.parseInt(rs.getString(1));
			requestNum++;
			fullRequestNumber = String.valueOf(requestNum); 
			stmt. executeUpdate(
			"INSERT INTO shitot.requestforchangingtimeallocated VALUES(\""
			+fullRequestNumber.trim()+"\",\""+request.getTeacherName()+"\",\""
			+request.getReason()+"\",\""+request.getMenagerApprove()+"\",\""+request.getIDexecutedExam()+"\",\""
			+request.getTimeAdded()+"\");");
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//stmt. executeUpdate("INSERT INTO shitot.exams VALUES(
		
	}

}
