package control;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import entity.Question;
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
	ArrayList<String> objectList = new ArrayList<String>();
	//Question questionDetails = new Question();
	Object[] serverMessage=new Object[2];
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

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		
		con.runDB();
		//String[] message = ((String) msg).split(" ");
		String[] message = (String[])msg; // message = message returned from Client
		System.out.println("Message received: " + msg + " from " + client);
		// split the msg to 2 strings (message[0]=the name of the method the server need to call,message[1]=search key to work with in SQL 
		serverMessage[0] = message[0];
		serverMessage[1] =objectList;
		switch (message[0]) {
		case "getSubjects": { /* if the client request all the subject */
			objectList = con.getSubjectList(objectList);
			this.sendToAllClients(serverMessage);
			System.out.println("arraylist to deliver");
			break;
		}
		case "getQuestions": {/*client request all all the questions under some subject*/
			objectList = con.getQuestionList(message[1],objectList);
			this.sendToAllClients(serverMessage);
			break;
		}
		case "getQuestionDetails" :
			{
				//questionDetails=con.getQuestionDetails(message[1],questionDetails); + changing the method
				objectList=con.getQuestionDetails(message[1],objectList);
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
			default:{
				System.out.println("Error on switch case ");
			}
		}
		
		// saveUserToDB(con, (ArrayList<String>)msg);
		/* System.out.println("Message received: " + msg + " from " + client); */
		//this.sendToAllClients(msg);
		System.out.println("Handle massege success");
		objectList.clear();
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
