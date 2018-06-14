package control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entity.MyFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import ocsf.client.AbstractClient;


public class ManualExamControl extends AbstractClient implements Initializable {
	
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
		// TODO Auto-generated method stub
		
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
		}
		else {
			wordLogo.setVisible(false);
			uploadManualExamButton.setDisable(true);
		}
	}
	public void uploadFileToServer(ActionEvent e) {
		  MyFile file= new MyFile(fileFromClient.get(0).getName());
		  String LocalfilePath=fileFromClient.get(0).getAbsolutePath();
			
		  try{
			      File newFile = new File (LocalfilePath);
			      byte [] mybytearray  = new byte [(int)newFile.length()];
			      FileInputStream fis = new FileInputStream(newFile);
			      BufferedInputStream bis = new BufferedInputStream(fis);			  
			      
			      file.initArray(mybytearray.length);
			      file.setSize(mybytearray.length);
			      
			      bis.read(file.getMybytearray(),0,mybytearray.length);
			      sendToServer(file);		      
			    }
			catch (Exception exception) {
				System.out.println("Error send (Files)msg) to Server");
			}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
