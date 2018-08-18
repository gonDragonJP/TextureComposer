package textureComposer.pictureDialog.seqReadDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import textureComposer.TextureData;

public class SeqReadDialog extends Stage {
	
	private PicDialog_seqReadDlg picDlg;

	private TableView<File> tableView = new TableView<>();

	private TextField textFrameSize = new TextField();
	private TextField textCutLeft = new TextField();
	private TextField textCutTop = new TextField();
	private TextField textInsertFrame = new TextField();
	
	private Button insertButton = new Button("Insert");
	
	private int gridSizeX, gridSizeY, cutLeft, cutTop, insertFrame;
	private List<File> fileList = new ArrayList<>();
	
	private TextureData parentTexData;
	

	public SeqReadDialog(Window window, TextureData texData) {
		
		parentTexData = texData;
		picDlg = new PicDialog_seqReadDlg(this, texData.gridSizeX, texData.gridSizeY);

		this.setTitle("SequencialRead");
		this.initOwner(window);
		this.initModality(Modality.APPLICATION_MODAL);

		setScene();
		setTable();
	}
	
	public boolean open(){
		
		this.gridSizeX = parentTexData.gridSizeX;
		this.gridSizeY = parentTexData.gridSizeY;
		textFrameSize.setText(String.format("%d * %d", gridSizeX, gridSizeY));
		
		setFileListFromDialog();
		if(fileList == null) return false;
		sortFileList();
		
		setFileListToTable();
		this.show();
		textCutLeft.requestFocus();
		
		return true;
	}
	
	public void insertExecute(){
		
		WritableImage dstImg = (WritableImage)parentTexData.image;
		double parentWidth = parentTexData.image.getWidth();
		double parentHeight = parentTexData.image.getHeight();
		
		int frameStride = (int)parentWidth / gridSizeX;
		int frame = insertFrame;
		
		for(File e: fileList){
		
			int x = (frame % frameStride) * gridSizeX;
			int y = (frame / frameStride) * gridSizeY;
	
			picDlg.validateImage(e);
			picDlg.pasteCutImage(dstImg, x, y);
			frame++;
		}
		
		this.close();
	}

	private void setScene() {

		VBox vbox = new VBox();
		vbox.getChildren().addAll(getTextFieldBox(), tableView);

		Scene scene = new Scene(vbox);
		this.setScene(scene);
	}

	private VBox getTextFieldBox(){
		
		setTextField();
		
		Label label0 = new Label("Frame Size");
		Label label1 = new Label("Cut Left");
		Label label2 = new Label("Cut Top");
		Label label3 = new Label("Insert Frame");
		
		HBox labelBox = new HBox();
		labelBox.setSpacing(30);
		labelBox.getChildren().addAll(label0, textFrameSize);
	
		HBox fieldBox = new HBox();
		fieldBox.setSpacing(20);
		fieldBox.getChildren().addAll(label1, textCutLeft, label2, textCutTop);
		
		HBox insertBox = new HBox();
		insertBox.setSpacing(20);
		insertBox.getChildren().addAll(label3, textInsertFrame, insertButton);
	
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		vBox.getChildren().addAll(labelBox, fieldBox, insertBox);
		
		return vBox;
	}

	private void setTextField() {

		textFrameSize.setPrefWidth(150);
		textFrameSize.setEditable(false);
		textCutLeft.setPrefWidth(50);
		textCutTop.setPrefWidth(50);
		textInsertFrame.setPrefWidth(50);
		
		StringConverter<Integer> converter = new IntegerStringConverter();
		TextFormatter[] formatters = new TextFormatter[3];
		for(int i=0; i<3; i++){
			formatters[i] = new TextFormatter<Integer>(converter);
		} 
		//　formatter は　control固有の情報を持つ為、各個に別物が必要です
		
		textCutLeft.setOnKeyReleased(event->validateCutPos());
		textCutLeft.setTextFormatter(formatters[0]);
		textCutTop.setOnKeyReleased(event->validateCutPos());
		textCutTop.setTextFormatter(formatters[1]);
		textInsertFrame.setOnKeyReleased(event->validateInsertFrame());
		textInsertFrame.setTextFormatter(formatters[2]);
		
		insertButton.setOnAction(event->insertExecute());
	}
	
	private void validateCutPos(){
		
		try{
		
			cutLeft = Integer.valueOf(textCutLeft.getText());
			cutTop = Integer.valueOf(textCutTop.getText());
		}
		catch(Exception e){
			
		}
		
		picDlg.validateCutPos(cutLeft, cutTop);
	}
	
	private void validateInsertFrame(){
		
		try{
			insertFrame = Integer.valueOf(textInsertFrame.getText());
		}
		catch(Exception e){
			
		}
	}

	private void setTable() {

		TableColumn<File, String> column = new TableColumn<>("picture name");
		column.setPrefWidth(300);
		column.setCellValueFactory(param -> {

			return new SimpleStringProperty(param.getValue().getName());
		});

		column.setSortable(false);
		tableView.getColumns().add(column);
		tableView.setOnMouseClicked(event->onMouseClicked(event));
	}
	
	private void onMouseClicked(MouseEvent event){
		
		if(event.getButton() == MouseButton.PRIMARY){
			
			if(event.getClickCount() == 1) selectRow();
		}
		if(event.getButton() == MouseButton.SECONDARY){
			
			if(event.getClickCount() == 1) openContextMenu();
		}
	}

	private void setFileListToTable() {

		ObservableList<File> tableData = FXCollections.observableArrayList();
		tableData.setAll(fileList);
		tableView.itemsProperty().setValue(tableData);
	}

	private void setFileListFromDialog() {

		File file = new DirDialog_seqReadDlg(this).open();

		if (file != null) {

			for(File e: file.listFiles()) fileList.add(e);
		}
	}
	
	private void sortFileList(){
		
		for(int i=0; i<fileList.size()-1; i++){
			for(int j=i+1; j<fileList.size(); j++){
				
				File srcFile = fileList.get(i);
				File dstFile = fileList.get(j);
				if(srcFile.compareTo(dstFile)>0){
					fileList.remove(j);
					fileList.add(i,dstFile);
				}
			}
		}
	}
	
	private void selectRow(){
		
		File file = tableView.getSelectionModel().getSelectedItem();
		
		if (file != null) {
			
			picDlg.show();
			picDlg.validateImage(file);
		}
	}
	
	private void openContextMenu(){
		
		int index = tableView.getSelectionModel().getSelectedIndex();
		if(index == -1) return;
		int lastIndex = fileList.size() - 1;
		
		ContextMenu contextMenu = new ContextMenu();
		String[] menuText = {"Up", "Down", "Remove"};
		
		MenuItem[] menuItem  = new MenuItem[menuText.length];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		if (index == 0) menuItem[0].setDisable(true);
		if (index == lastIndex) menuItem.clone()[1].setDisable(true);
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e ->upDownInTheList(index, true));
		menuItem[1].setOnAction(e ->upDownInTheList(index, false));
		menuItem[2].setOnAction(e ->RemoveFromTheList(index));
	
		contextMenu.getItems().addAll(menuItem);
		tableView.setContextMenu(contextMenu);
	}
	
	private void upDownInTheList(int index, boolean doUp){
		
		File file = fileList.get(index);
		fileList.remove(index);
		fileList.add(doUp ? index -1 : index + 1, file);
		
		setFileListToTable();
	}
	
	private void RemoveFromTheList(int index){
		
		fileList.remove(index);
		
		setFileListToTable();
	}
}
