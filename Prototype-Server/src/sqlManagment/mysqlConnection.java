package sqlManagment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class mysqlConnection {
	private String serverName ;
	private String userPassword ;
	private String DBname  ;
	static Connection conn;
	public mysqlConnection() {
	Scanner sc = new Scanner(System.in);
	System.out.println("enter your DB name: ");
	serverName = sc.nextLine();
	System.out.println("enter your server name: ");
	userPassword = sc.nextLine();
	System.out.println("enter your password: ");
	DBname = sc.nextLine();
}
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

	public ArrayList<String> getSubjectList(ArrayList<String> subjectList) {
		/*
		 * This function separate the subject id from the whole Question id for 
		 * useful query 
		 */
		//ArrayList<String> subjectList = new ArrayList<String>();
		String subjectID = "";
		Statement stmt;
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
		Statement stmt;
		try {
			 stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT Question_Text FROM questions "	+ "WHERE Question_id like "+"\""+subject+"%\""+";" );
			// ResultSet rs = stmt.executeQuery("SELECT Question_Text FROM questions WHERE Question_id like \""+subject+"\" ;");
			while (rs.next())
				questionList.add(rs.getString(1));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionList;
	}

	public ArrayList<String> getAnswers(String quest) {
		int i;
		ArrayList<String> answerList = new ArrayList<String>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT answer1, answer2, answer3, answer4, correct_answer FROM questions where question_text=\""
							+ quest + "\";");
			rs.next();
			for (i = 1; i < 5; i++)
				answerList.add(rs.getString(i));
			answerList.add(Integer.toString(rs.getInt(5)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return answerList;
	}

	public void updateAnswer(String question, String newAnswer) throws SQLException {
		Statement stmt;
		stmt = conn.createStatement();
		stmt.executeUpdate("UPDATE questions SET correct_answer=\"" + Integer.parseInt(newAnswer)
				+ "\" WHERE question_text=\"" + question + "\";");
	}
}
