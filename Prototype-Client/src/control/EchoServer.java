package control;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Course;
import entity.Exam;
import entity.ExamDetailsMessage;
import entity.ExecutedExam;
import entity.Question;
import entity.RequestForChangingTimeAllocated;
import entity.TeachingProfessionals;
import entity.User;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	MysqlConnection con=new MysqlConnection();
	//Question questionDetails = new Question();
	Object[] serverMessage=new Object[3];
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */

	// change data to our form
	public int parsingTheData(String Id) {
		return Integer.parseInt(Id);
	}

	@SuppressWarnings("unchecked")
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		con.runDB(); 
		//String[] message = ((String) msg).split(" ");
		Object[] message = (Object[])msg; // message = message returned from Client
		System.out.println("Message received: " + msg + " from " + client);
		// split the msg to 2 strings (message[0]=the name of the method the server need to call,message[1]=search key to work with in SQL 
		serverMessage[0] = message[0];
		switch ((String)message[0]) {
		case "getSubjects": { /* if the client request all the subject */
			ArrayList<TeachingProfessionals> tp = con.getSubjectList(message[1]);
			serverMessage[1] =tp;
			this.sendToAllClients(serverMessage);
			System.out.println("arraylist to deliver");
			break;
		}
		
		case "getCourses": {/*client request all all the courses under some subject*/
			ArrayList<Course> courseList = con.getCourseList(message[1],message[2]);
			serverMessage[1] =courseList;
			this.sendToAllClients(serverMessage);
			break;
		}
		
		case "getExecutedExams": {/*client request all all the courses under some subject*/
			ArrayList<ExecutedExam> executedexam = con.getExecutedExam(message[1]);
			serverMessage[1] =executedexam;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "checkExecutedExam": {/*check the executed exam id validity*/
			Object[] questioninexam = con.checkExecutedExam(message[1]);
			Time solutionTime=con.getSolutionTime(message[1]);
			serverMessage[1] =questioninexam;
			serverMessage[2] =solutionTime;
			this.sendToAllClients(serverMessage);
			break;
		}
		
		case "getExams": {/*client request all all the courses under some subject*/
			ArrayList<String> examsList = con.getExams(message[1]);
			serverMessage[1] =examsList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "createChangingRequest": {
			con.createChangingRequest(message[1]);
			break;
		}
		

		
		case "getQuestions": {/*client request all all the questions under some subject*/
			ArrayList<String> questionList = con.getQuestionList(message[1],message[2]);
			serverMessage[1] =questionList;
			this.sendToAllClients(serverMessage);
			break;
		}
		
		case "getQuestionsToTable": {/*client request all all the questions under some subject*/
			ArrayList<Question> questionList = con.getQuestionListToTable(message[1],message[2]);
			serverMessage[1] = questionList;
			this.sendToAllClients(serverMessage);
			break;
		}
		
		case "setExam": {/*client request is to create exam in DB*/
			con.createExam(message[1],message[2]);
			break;
		}
		
		
		case "setExamCode": {/*client request is to create exam code in DB*/
			Boolean createExamCodeStatus;
			createExamCodeStatus = con.createExamCode(message[1]);
			serverMessage[1] = createExamCodeStatus;
			this.sendToAllClients(serverMessage);
			break;
		}
		
		case "getQuestionDetails" :
			{
				Question q=con.getQuestionDetails(message[1]);
				serverMessage[1]=q;
				this.sendToAllClients(serverMessage);
				break;
			}
		case "updateCorrectAnswer": {
			try {
				con.updateAnswer(message[1], message[2]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "updateQuestion": {
				try {
					con.updateQuestion(message[1]);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		}
		case "checkUserDetails": {
			User user=con.checkUserDetails(message[1], message[2]);
			serverMessage[1]=user;
			this.sendToAllClients(serverMessage);
			break;
		}

		case "setExecutedExamLocked": {
			con.setExecutedExamLocked(message[1]);
			break;
		}
		case "SetQuestion": {
			con.createQuestion(message[1], message[2]);
			break;
		} 
		case "logoutProcess" :{
			con.performLogout(message[1]);
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getTimeRequestList":{
			ArrayList<RequestForChangingTimeAllocated> requestsList=con.getAddingTimeRequests();
			serverMessage[1]=requestsList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getTimeRequestDetails":{
			RequestForChangingTimeAllocated request=con.getAddingTimeRequestsDetails((String)message[1]);
			serverMessage[1]=request;
			this.sendToAllClients(serverMessage);
			break;
		}   
		case  "getExamsByUserName" : {
			ArrayList<ExamDetailsMessage> examsPrefDetails = con.getPrefExamDetails((String)message[1]);
			serverMessage[1] = examsPrefDetails ; 
			this.sendToAllClients(serverMessage);
			break;
		}
		
		case "SetStatusToApproved" :{
			con.setStatusToAddingTimeRequest(((Object[])msg)[1],"approved");
			break;
		}
		case "SetStatusToReject" :{
			con.setStatusToAddingTimeRequest(((Object[])msg)[1],"rejected");
			break;
		}

		case "finishExam" :{
			con.finishExam((String[]) message[1],(HashMap<String,Integer>)message[2]);
			break;
		}
	/*	case "getExecutedExamCodeList" :{// for using on confirm request of adding time to exam
			con.getRequestsList(message[1]);
			break;
		} */ 
			default:{
				System.out.println("Error on switch case ");
			}
		}
		
		// saveUserToDB(con, (ArrayList<String>)msg);
		/* System.out.println("Message received: " + msg + " from " + client); */
		//this.sendToAllClients(msg);
		System.out.println("Handle massege success  " + (String)message[0]);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0]
	 *            The port number to listen on. Defaults to 5555 if no argument is
	 *            entered.
	 */
	public static void main(String[] args) {
	 	int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
