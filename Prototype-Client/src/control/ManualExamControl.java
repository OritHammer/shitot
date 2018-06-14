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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import ocsf.client.AbstractClient;

public class ManualExamControl extends AbstractClient implements Initializable {
	public static Time solutionTime;
	public static int remainTime;
	private static String executedID;
	private Object[] messageToServer = new Object[3];
	private List<File> fileFromClient;
	@FXML
	private ImageView imageDraging;
	@FXML
	private ImageView wordLogo;
	@FXML
	private ImageView uploadImage;
	@FXML
	private Button uploadManualExamButton;

	public ManualExamControl(String host, int port) {
		super(host, port);
	}

	public ManualExamControl() throws IOException {
		super("localhost", 5555);
		openConnection();
	}

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

	public void dropFileToImage(DragEvent e) {
		fileFromClient = e.getDragboard().getFiles();
		boolean wordFile = fileFromClient.get(0).getAbsolutePath().contains(".docx");
		if (wordFile) {
			wordLogo.setVisible(true);
			uploadManualExamButton.setDisable(false);
		} else {
			wordLogo.setVisible(false);
			uploadManualExamButton.setDisable(true);
		}
	}

	@SuppressWarnings("resource")
	public void uploadFileToServer(ActionEvent e) {
		MyFile file = new MyFile(fileFromClient.get(0).getName());
		String LocalfilePath = fileFromClient.get(0).getAbsolutePath();

		try {
			File newFile = new File(LocalfilePath);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);

			file.initArray(mybytearray.length);
			file.setSize(mybytearray.length);

			bis.read(file.getMybytearray(), 0, mybytearray.length);
			messageToServer[0] = "saveExamOfStudent";
			//messageToServer[1] = getMyUser().getuserName();
			messageToServer[2] = file;
			sendToServer(file);
		} catch (Exception exception) {
			System.out.println("Error send (Files)msg) to Server");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
