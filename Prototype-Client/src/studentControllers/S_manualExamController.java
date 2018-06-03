package studentControllers;

import control.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class S_manualExamController extends UserControl {
	/*********************GUI Variable declaration *************************/
	@FXML
	private TextField codeTextField;
	private Object[] messageToServer=new Object[3];
	/*************** Class Methods *******************************/
	public void downloadExamPressed(ActionEvent e) {
		
	}
	public void backToHomePressed (ActionEvent e) {
		
	}
}
