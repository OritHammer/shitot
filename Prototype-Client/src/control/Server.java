package control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import entity.Course;
import entity.Exam;
import entity.ExamDetailsMessage;
import entity.ExecutedExam;
import entity.MyFile;
import entity.Question;
import entity.QuestionInExam;
import entity.RequestForChangingTimeAllocated;
import entity.StudentPerformExam;
import entity.TeachingProfessionals;
import entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
public class Server extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	MysqlConnection con = new MysqlConnection();
	// Question questionDetails = new Question();
	Object[] serverMessage = new Object[4];
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public Server(int port) {
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
		// String[] message = ((String) msg).split(" ");
		Object[] message = (Object[]) msg; // message = message returned from Client
		System.out.println("Message received: " + msg + " from " + client);
		// split the msg to 2 strings (message[0]=the name of the method the server need
		// to call,message[1]=search key to work with in SQL
		serverMessage[0] = message[0];
		switch ((String) message[0]) {
		case "getSubjects": { /* if the client request all the subject */
			ArrayList<TeachingProfessionals> tp = con.getSubjectList(message[1]);
			serverMessage[1] = tp;
			this.sendToAllClients(serverMessage);
			System.out.println("arraylist to deliver");
			break;
		}
		case "getCourses": {/* client request all all the courses under some subject */
			ArrayList<Course> courseList = con.getCourseList(message[1], message[2]);
			serverMessage[1] = courseList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getExecutedExams": {/* client request all all the courses under some subject */
			ArrayList<ExecutedExam> executedexam = con.getExecutedExam(message[1], message[2]);
			serverMessage[1] = executedexam;
			this.sendToAllClients(serverMessage);
			break;
		}

		case "getStudenstInExam": {/* */
			ArrayList<StudentPerformExam> studentsInExam = con.getStudenstInExam(message[1]);
			serverMessage[1] = studentsInExam;
			this.sendToAllClients(serverMessage);
			break;
		}

		case "checkExecutedExam": {/* check the executed exam id validity */
			String executedExamID = (String) message[1];
			try {
				Object[] executedexam = con.checkExecutedExam(message[1], message[2]);
				serverMessage[1] = executedexam[0];// question in exam
				serverMessage[2] = executedexam[1];// exam
				Exam exam = (Exam) executedexam[1];

				if (exam.getType().equals("manual")) {
					MyFile file = new MyFile(exam.getE_id() + ".docx");
					String LocalfilePath = "Exams/" + exam.getE_id() + ".docx";

					try {
						File newFile = new File(LocalfilePath);
						byte[] mybytearray = new byte[(int) newFile.length()];
						FileInputStream fis = new FileInputStream(newFile);
						BufferedInputStream bis = new BufferedInputStream(fis);

						file.initArray(mybytearray.length);
						file.setSize(mybytearray.length);

						bis.read(file.getMybytearray(), 0, mybytearray.length);
						serverMessage[3] = file;// צריך דחוףףףףף לסדר את זההההה
					} catch (Exception exception) {
						System.out.println("Error send (Files)msg) to Server");
					}
				}
				con.updateStudentToExecutedExam(executedExamID);
			} catch (NullPointerException exception) {
				System.out.println("This student cant perform this exam");
				serverMessage[1] = null;
			}

			this.sendToAllClients(serverMessage);
			break;
		}
		case "getStudentAnswers": {
			ArrayList<Question> questioninexam = con.getQuestionFromCloseExam((String) message[1]);
			String userName = (String) message[2];
			serverMessage[0] = "showingCopy";
			HashMap<String, Integer> studentAns = con.getStudentAns(userName, (String) message[1]);
			serverMessage[2] = studentAns;
			serverMessage[1] = questioninexam;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "saveExamOfStudent": {// saving the manual exam of the student
			FileOutputStream fileOutputStream = null;
			BufferedOutputStream bufferedOutputStream = null;
			if (message[2] != null) {
				MyFile file = (MyFile) message[2];
				try {
					File diagFromClient = new File("Students Exams/" + file.getFileName());
					System.out.println("Please wait downloading file"); // reading file from socket
					fileOutputStream = new FileOutputStream(diagFromClient);
					bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
					bufferedOutputStream.write(file.getMybytearray(), 0, file.getSize()); // writing byteArray to file
					bufferedOutputStream.flush(); // flushing buffers
					System.out.println(
							"File " + diagFromClient + " downloaded ( size: " + file.getSize() + " bytes read)");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fileOutputStream != null)
						try {
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					if (bufferedOutputStream != null)
						try {
							bufferedOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}

			}
			con.finishExam((String[]) message[1], null, (boolean) message[3]);
			break;
		}
		case "getExams": {/* client request all all the exams under some courses */
			ArrayList<Exam> examsList = con.getExams(message[1]);
			serverMessage[1] = examsList;
			this.sendToAllClients(serverMessage);
			break;
		}

		case "createChangingRequest": {
			con.createChangingRequest(message[1]);
			break;
		}

		case "getQuestionsToTable": {/* client request all all the questions under some subject */
			ArrayList<Question> questionList = con.getQuestionListToTable(message[1], message[2]);
			serverMessage[1] = questionList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "setExam": {/* client request is to create exam in DB */
			String examId = con.createExam(message[1], message[2]);
			if (((Exam) message[2]).getType().equals("manual")) {
				ArrayList<Question> questions = con.getQuestions(message[1]);
				((Exam) message[2]).setE_id(examId);
				createManualExam((Exam) message[2], questions);// This method create word(docx)file to manual exam
			}
			break;
		}
		case "setExamCode": {/* client request is to create exam code in DB */
			Boolean createExamCodeStatus;
			createExamCodeStatus = con.createExamCode(message[1]);
			serverMessage[1] = createExamCodeStatus;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getQuestionDetails": {
			Question q = con.getQuestionDetails(message[1]);
			serverMessage[1] = q;
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
			boolean flag;
			flag = con.updateQuestion(message[1]);
			serverMessage[1] = flag;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "deleteQuestion": {
			Boolean flag;
			try {
				flag = con.deleteQuestion(message[1]);
				serverMessage[1] = flag;
				this.sendToAllClients(serverMessage);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case "checkUserDetails": {
			User user = con.checkUserDetails(message[1], message[2]);
			serverMessage[1] = user;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "updateExam": {
			Boolean inserted = con.updateExam(message[1]);
			serverMessage[1] = inserted;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "updateQuestionInExam": {
			con.updateQuestionInExam(message[1], message[2]);
			Exam exam = con.getExam(message[2]);
			if (exam.getType().equals("manual")) {
				ArrayList<Question> questions = con.getQuestions(message[1]);
				createManualExam(exam, questions);// This method create word(docx)file to manual exam
			}
			this.sendToAllClients(serverMessage);
			break;
		}

		case "deleteExam": {
			Boolean deleted = con.deleteExam(message[1]);
			serverMessage[1] = deleted;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "setExecutedExamLocked": {
			Boolean isLocked = con.setExecutedExamLocked(message[1]);
			serverMessage[1] = isLocked;
			serverMessage[2]=message[1];
			this.sendToAllClients(serverMessage);
			break;
		}
		case "SetQuestion": {
			con.createQuestion(message[1], message[2], message[3]);
			break;
		}
		case "logoutProcess": {
			con.performLogout(message[1]);
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getTimeRequestList": {
			ArrayList<RequestForChangingTimeAllocated> requestsList = con.getAddingTimeRequests();
			serverMessage[1] = requestsList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getTimeRequestDetails": {
			RequestForChangingTimeAllocated request = con.getAddingTimeRequestsDetails((String) message[1]);
			serverMessage[1] = request;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getQuestionInExam": {
			Boolean examFlag;
			ArrayList<QuestionInExam> questioninexam = con.getQuestionInExam(message[1]);
			examFlag = con.checkIfExamIsNotActive(message[1]);
			serverMessage[1] = questioninexam;
			serverMessage[2] = examFlag;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getExamsByUserName": {
			ArrayList<ExamDetailsMessage> examsPrefDetails = con.getPrefExamDetails((String) message[1]);
			serverMessage[1] = examsPrefDetails;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "SetStatusToApproved": {
			con.setStatusToAddingTimeRequest(((Object[]) msg)[1], "approved");
			Object tmp[] = con.getadditionalTime((String) ((Object[]) message)[1]);
			con.setRealTimeOfExecutedExam((String) ((Object[]) message)[1]);
			serverMessage[0] = "addTime";
			serverMessage[1] = tmp[0];// serverMessage[0]=requestId(String)
			serverMessage[2] = tmp[1];// serverMessage[0]=time to add (Time)
			serverMessage[3]=((Object[]) msg)[1];
			this.sendToAllClients(serverMessage);
			break;
		}
		case "SetStatusToReject": {
			con.setStatusToAddingTimeRequest(((Object[]) msg)[1], "rejected");
			break;
		}
		case "getStudentsList": {// send to client list of all the student in the system
			ArrayList<String> studentList = con.returnListForGetReport("Student");
			serverMessage[0] = "getStudentsList";
			serverMessage[1] = studentList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getTeachersList": {// send to client list of all the teachers in the system
			ArrayList<String> teacherList = con.returnListForGetReport("Teacher");
			serverMessage[0] = "getTeachersList";
			serverMessage[1] = teacherList;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getReportByTeacher": {//// send to client statistic report of all the exam that teacher dose
			ArrayList<ExecutedExam> teacherReportDetails = con.returnReportByTeacherOrCoursesDetails(message[0],
					message[1]);
			serverMessage[0] = "getReportByTeacher";
			serverMessage[1] = teacherReportDetails;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getReportByCourse": {
			ArrayList<ExecutedExam> courseReportDetails = con.returnReportByTeacherOrCoursesDetails(message[0],
					message[1]);
			serverMessage[0] = "getReportByCourse";
			serverMessage[1] = courseReportDetails;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getReportByStudent": {
			ArrayList<Integer> studentReportDetails = con.returnReportByStudent(message[1]);
			serverMessage[0] = "getReportByStudent";
			serverMessage[1] = studentReportDetails;
			this.sendToAllClients(serverMessage);
			break;
		}
		case "finishExam": {
			String[] details=((String[]) message[1]);
			con.finishExam(details, (HashMap<String, Integer>) message[2], (boolean) message[3]);
			con.checkIfAllStudentFinishedExam(details[0]);
			break;
		}
		case "isChanged":{
			break;
		}
		
		case "confirmExecutedExam": {
			con.confirmExecutedExam(message[1]);
			break;
		}
		
		/*
		 * case "getExecutedExamCodeList" :{// for using on confirm request of adding
		 * time to exam con.getRequestsList(message[1]); break; }
		 */
		default: {
			System.out.println("Error on switch case ");
		}
		}

		// saveUserToDB(con, (ArrayList<String>)msg);
		/* System.out.println("Message received: " + msg + " from " + client); */
		// this.sendToAllClients(msg);
		System.out.println("Handle massege success  " + (String) message[0]);
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

		Server sv = new Server(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	public static void createManualExam(Exam exam, ArrayList<Question> qustionsInExam) {
		/*
		 * who ever touch this function,notice that: the function recieves Exam the
		 * function recieves arraylist of Question
		 */
		int i = 1;
		System.out.println("Im here");
		XWPFDocument wordExam = new XWPFDocument();
		try {
			FileOutputStream out = new FileOutputStream(new File("Exams/" + exam.getE_id() + ".docx"));
			XWPFParagraph title = wordExam.createParagraph();
			XWPFRun run = title.createRun();

			title.setAlignment(ParagraphAlignment.RIGHT);
			run.setText(" Manual Exam:");
			run.setBold(true);
			XWPFRun runExamId = title.createRun();
			runExamId.setText(exam.getE_id());
			runExamId.addCarriageReturn();
			XWPFRun run1 = title.createRun();
			run1.setText(" note:");
			run1.setBold(true);
			XWPFRun runNote = title.createRun();
			runNote.setText(exam.getRemarksForStudent());
			runNote.addCarriageReturn();
			XWPFRun run2 = title.createRun();
			run2.setText(" Time for exam:");
			run2.setBold(true);
			XWPFRun runSolutionTime = title.createRun();
			runSolutionTime.setText(exam.getSolutionTime());
			runSolutionTime.addCarriageReturn();
			System.out.println("Im here123");
			for (Question q : qustionsInExam) {

				title = wordExam.createParagraph();
				run = title.createRun();
				run.setText(i + ") " + q.getQuestionContent());
				run.setBold(true);
				run.addCarriageReturn();
				run = title.createRun();
				run.setText("1." + q.getAnswer1());
				run.addCarriageReturn();
				run = title.createRun();
				run.setText("2." + q.getAnswer2());
				run.addCarriageReturn();
				run = title.createRun();
				run.setText("3." + q.getAnswer3());
				run.addCarriageReturn();
				run = title.createRun();
				run.setText("4." + q.getAnswer4());
				run.addCarriageReturn();
				title.setAlignment(ParagraphAlignment.RIGHT);
				i++;
			}
			System.out.println("Im here34");
			title = wordExam.createParagraph();
			run = title.createRun();
			run.setText("Good luck!");
			run.setBold(true);
			title.setAlignment(ParagraphAlignment.CENTER);
			run.setFontSize(42);
			wordExam.write(out);
			out.close();
			wordExam.close();
			System.out.println("now im hehre");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
// End of EchoServer class
