package textureComposer.pictureDialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class InputImageSizeDialog {
	
	Dialog<Pair<String,String>> dialog = new Dialog<>();
	
	public InputImageSizeDialog(){
		
		dialog.setTitle("ImageSize Dialog");
		dialog.setHeaderText("Input New Image Size");
		
		setButton();
		setGrid();
	}
	
	public void open(double posX, double posY) {
		
		dialog.setX(posX);
		dialog.setY(posY);
		dialog.show();
	}
	
	private void setButton() {
		
		ButtonType changeButtonType = new ButtonType("Change", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);
	}
	
	private void setGrid() {
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10,10,10,10));
		
		TextField imgWidthText = new TextField();
		TextField imgHeightText = new TextField();
		
		grid.add(new Label("Image Width:"),0,0);
		grid.add(new Label("Image Height:"),0,1);
		grid.add(imgWidthText, 1, 0);
		grid.add(imgHeightText, 1, 1);
		
		dialog.getDialogPane().setContent(grid);
	}
}
