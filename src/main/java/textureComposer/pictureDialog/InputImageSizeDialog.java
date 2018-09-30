package textureComposer.pictureDialog;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class InputImageSizeDialog {
	
	Dialog<Pair<Integer,Integer>> dialog = new Dialog<>();
	
	public InputImageSizeDialog(){
		
		dialog.setTitle("ImageSize Dialog");
		dialog.setHeaderText("Input New Image Size");
		
		setButton();
		setGrid();
		setResultConverter();
	}
	
	private int oldWidth, oldHeight;
	
	public Optional<Pair<Integer, Integer>> 
		open(double posX, double posY, int oldSizeX, int oldSizeY) {
		
		this.oldWidth = oldSizeX;	this.oldHeight = oldSizeY;
		imgWidthText.setPromptText(String.valueOf(oldSizeX));
		imgHeightText.setPromptText(String.valueOf(oldSizeY));
		dialog.setX(posX);
		dialog.setY(posY);
		return dialog.showAndWait();
	}
	
	private void setButton() {
		
		ButtonType changeButtonType = new ButtonType("Change", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);
	}
	
	private TextField imgWidthText, imgHeightText;
	
	private void setGrid() {
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10,10,10,10));
		
		imgWidthText = new TextField();
		imgHeightText = new TextField();
		
		grid.add(new Label("Image Width:"),0,0);
		grid.add(new Label("Image Height:"),0,1);
		grid.add(imgWidthText, 1, 0);
		grid.add(imgHeightText, 1, 1);
		
		dialog.getDialogPane().setContent(grid);
	}
	
	private void setResultConverter() {
		
		dialog.setResultConverter(buttonType -> {
			
			int width,height;
			
			try {
				width = Integer.valueOf(imgWidthText.getText());
				height = Integer.valueOf(imgHeightText.getText());
			}catch(Exception e) {
				
				return null;
			}
			if(buttonType.getButtonData() == ButtonData.OK_DONE) return new Pair <>(width, height);
			else return null;
		});
	}
}
