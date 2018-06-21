package control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.List;
import java.util.ResourceBundle;

import entity.MyFile;
import control.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import ocsf.client.AbstractClient;

public class ManualExamControl extends AbstractClient implements Initializable {
	public static Time solutionTime;
	private String myUserName;
	public static int remainTime;
	private static String executedID;
	private Object[] messageToServer = new Object[4];
	private List<File> fileFromClient;
	@FXML
	private ImageView imageDraging;
	@FXML
	private ImageView wordLogo;
	@FXML
	private ImageView uploadImage;
	@FXML
	private Button uploadManualExamButton;
    @FXML
    private Label fileName;

	public ManualExamControl(String host, int port) {
		super(host, port);
	}
	public ManualExamControl() throws IOException {
		super("localhost", 5555);
		openConnection();
	}
	/**
	 * handleMessageFromServer(Object msg) 
	*  Arguments:Object message
	*  The  method handle the message from server
	* @author Aviv Mahulya
	*/
	@Override
	protected void handleMessageFromServer(Object msg) {
		final Object[] msgFromServer = (Object[]) msg;
		switch (msgFromServer[0].toString()) {
		case "addTime": {
			addTimeToExam(msgFromServer);
			break;
		}
		}
	}
	/**
	 * addTimeToExam(Object[] message)
	*  Arguments:Object[] message
	*  The  method add time to the exam that the student perform right now
	* @author Aviv Mahulya
	*/
	public void addTimeToExam(Object[] message) {
		int timeToAdd;
		Time timeFromMessage = (Time) message[2];
		if (executedID.equals((String) message[1])) {// if the student perform the relevant exam
			timeToAdd = timeFromMessage.getHours() * 3600 + timeFromMessage.getMinutes() * 60
					+ timeFromMessage.getSeconds();// reamin time is he time in secods
			remainTime += timeToAdd;
		}
	}
	public void dragOver(DragEvent e) {
		if (e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.ANY);
		}
	}
	/**
	 * dropFileToImage(DragEvent e)
	*  Arguments:DragEvent e
	*  The  method handle the drop file into the area
	* @author Tom Zarhin
	*/
	public void dropFileToImage(DragEvent e) {
		fileFromClient = e.getDragboard().getFiles();
		boolean wordFile = fileFromClient.get(0).getAbsolutePath().contains(".docx");
		if (wordFile) {//If its a word file
			wordLogo.setVisible(true);//show the image
			uploadManualExamButton.setDisable(false);//enable the button
			fileName.setText(fileFromClient.get(0).getName());
			fileName.setVisible(true);
		} else {
			wordLogo.setVisible(false);
			uploadManualExamButton.setDisable(true);
			fileName.setVisible(false);
		}
	}
	/**
	 * uploadFileToServer(ActionEvent e)
	*  Arguments:ActionEvent e
	*  The  method upload the file to the server
	* @author Tom Zarhin
	*/
	@SuppressWarnings("resource")
	public void uploadFileToServer(ActionEvent e) {
		MyFile file = new MyFile(fileFromClient.get(0).getName()+".docx");
		String LocalfilePath = fileFromClient.get(0).getAbsolutePath();

		try {
			File newFile = new File(LocalfilePath);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			file.initArray(mybytearray.length);
			file.setSize(mybytearray.length);
			bis.read(file.getMybytearray(), 0, mybytearray.length);
			String details[] = new String[2];
			details[0] = executedID;//the executed exam
			details[1] = myUserName;//name of the student
			messageToServer[0] = "saveExamOfStudent";
			messageToServer[1] = details;
			messageToServer[2] = file;
			messageToServer[3] =e==null?false:true;/*If the student finish the exam or not*/
			sendToServer(messageToServer);
			/*Go to home screen*/
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/studentBoundary/NewDesignHomeScreenStudent.fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = Main.getStage();
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			System.out.println("Error send (Files)msg) to Server");
		}
	}

	public void setDetails(String myUserName,String executedExamId) {
		this.myUserName=myUserName;
		executedID=executedExamId;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
