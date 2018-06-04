package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Exam;
import entity.Question;
import entity.QuestionInExam;
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
			 /*//updating user status 
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
				       "SET status=\"unconnected\" WHERE username=\""+userName+"\";");
			System.out.println("user set as unconnected"); 
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<TeachingProfessionals> getSubjectList() {
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
				ResultSet rs = stmt.executeQuery("SELECT * FROM teachingprofessionals;");// questions is the name on the
																							// DB
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

	public ArrayList<String> getQuestionList(Object subject) {
		/*
		 * The function return the question list by the given subject code
		 */
		// Statement stmt;
		ArrayList<String> questionList = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT question_id,Question_Text FROM questions" + " WHERE Question_id like " + "\"" + subject + "%\"" + ";");
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

	public void createExam(Object questionInExams,Object examDetails) {
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
			+exam.getRemarksForTeacher()+"\",\""+exam.getRemarksForStudent()+"\",\""+exam.getType()+"\");");
			
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
}
