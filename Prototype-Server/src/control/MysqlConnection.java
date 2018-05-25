package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
public class MysqlConnection {
static Connection conn;
private Statement stmt;
/************************** Class Constructor ********************************/
	public MysqlConnection() {
	/*used to enter server details
	Scanner sc = new Scanner(System.in);
	System.out.println("enter your DB name: ");
	serverName = sc.nextLine();
	System.out.println("enter your server name: ");
	userPassword = sc.nextLine();
	System.out.println("enter your password: ");
	DBname = sc.nextLine();
	*/
}

/**************************** Class Methods **********************************/
	public void runDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/sys","root","Braude");
			System.out.println("SQL connection succeed");
			// createTableQuestion();
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	public ArrayList<String> checkUserDetails(ArrayList<String> userDetails,String userID,String userPass) {
		try {
			stmt = conn.createStatement();
			//query check existent of such details base on  user name and password
			ResultSet rs = stmt.executeQuery("SELECT UserID,userName,role FROM questions"
															  + "WHERE userID=\""+userID+"\" AND password=\""+userPass+"\";");
			//if there is no user with given details
			if(rs == null)
				return null ;
			else {
				while(rs.next()) {
					userDetails.add(rs.getString(1));
					//in the end userDetails will have the UserID,userName,role
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return userDetails;
	}
	public ArrayList<String> getSubjectList(ArrayList<String> subjectList) {
		/*
		 * This function separate the subject id from the whole Question id for 
		 * useful query 
		 */
		
		String subjectID = "";
		//Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Question_id FROM questions;");//questions is the name on the DB
			while (rs.next()) {
				subjectID = rs.getString(1).substring(0, 2);
				if (!subjectList.contains(subjectID))
					subjectList.add(subjectID);
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return subjectList;
	}

	public ArrayList<String> getQuestionList(String subject,ArrayList<String> questionList) {
		/*
		 * The function return the question list by the given subject code
		 */
		//Statement stmt;
		try {
			 stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT Question_Text FROM questions"
					 						+ " WHERE Question_id like "+"\""+subject+"%\""+";" );
			while (rs.next())
				questionList.add(rs.getString(1));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionList;
	}

	public ArrayList<String> getQuestionDetails(String quest, ArrayList<String> DetailsList) {
		int i;
		//ArrayList<String> answerList = new ArrayList<String>();
		//Statement stmt;
		try {
			stmt = conn.createStatement();
			//Query return all the details of specific question
			ResultSet rs = stmt.executeQuery("SELECT Question_id,Teacher_Name,answer1,answer2,answer3,answer4,Correct_Answer FROM questions " + "WHERE Question_Text=\""+ quest + "\";");
			//The next commands get the returned details from DB and insert them to question object 
			
			rs.next(); 
			//inserting the data to String List , order by the same order in DB  
			for(i=1;i<8;i++)
				DetailsList.add(rs.getString(i));
			
			rs.close();
			
			
			//end insert details	
		} catch (SQLException e) {
			
			
		}
		return DetailsList;
	}

	public void updateAnswer(String questionID, String newAnswer) throws SQLException {
		//Statement stmt;
		stmt = conn.createStatement();
		//query update on DB the correct answer of question that have the given questionID from client
		stmt.executeUpdate("UPDATE questions SET Correct_answer=\"" + newAnswer
							+ "\" WHERE Question_id=\"" + questionID + "\";");
	System.out.println("question:"+ questionID + "new answer:" + newAnswer );
	}
}
