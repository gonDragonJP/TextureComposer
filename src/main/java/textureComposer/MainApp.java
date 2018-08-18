package textureComposer;

import java.io.File;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import textureComposer.MenuUtil_MainApp.BackGroundColor;
import textureComposer.MenuUtil_MainApp.OnOrOff;
import textureComposer.database.DatabaseAccess;
import textureComposer.pictureDialog.PictureDialog;
import textureComposer.pictureDialog.seqReadDialog.SeqReadDialog;

public class MainApp extends Application 
						implements MenuUtil_MainApp.MenuCallback{
	
	private final String databaseDir = ".\\texDataBase\\";
	private final String databasePath = databaseDir + "texDB.db";
	private final String imageDir = databaseDir + "image\\";

	private PictureDialog pictureDialog;
	
	public TextField textTextureID = new TextField();
	public TextField textPictureName = new TextField();
	public TextField textGridSizeX = new TextField();
	public TextField textGridSizeY = new TextField();
	
	public Button storeButton = new Button("Store To DB");
	
	public TableModule tableModule = new TableModule();
	
	
	public static void main(String[] args){
		
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		SceneUtil.setScene(this, stage);
		setPictureDialog(stage);
		
		tableModule.setTable(databasePath);
		tableModule.setDeleteTexDataListener(e->{
					deleteSelectedTexData();
					return null;
					});
		tableModule.setSelectRowListener(e->{
					selectDBTableRow(e);
					return null;
					});
		
		stage.show();
		pictureDialog.show();
	}
	
	public TableModule getTableModule(){
		
		return tableModule;
	}
	
	private void setPictureDialog(Stage stage){
	
		Window wnd = stage;
		pictureDialog = new PictureDialog(wnd);
		
		setTextField(pictureDialog.getTextureData());
		
		pictureDialog.setChangedImageListener(
				picdata -> {setTextField(picdata); return null;}
		);
	}
	
	private void setTextField(TextureData data){
		
		String picSizePS =""; 
		
		if(data.image !=null){
			
			picSizePS = String.format(" ( %d * %d )", 
					(int)data.image.getWidth(), (int)data.image.getHeight());
		}
		
		textTextureID.setText(String.valueOf(data.textureID));
		textPictureName.setText(data.pictureName  + picSizePS);
		textGridSizeX.setText(String.valueOf(data.gridSizeX));
		textGridSizeY.setText(String.valueOf(data.gridSizeY));
	}
	
	private void deleteSelectedTexData(){
		
		tableModule.deleteSelectedTexData(databasePath);
	}
	
	private void selectDBTableRow(TextureData selectedData){
		
		File file = new File(imageDir + selectedData.pictureName);
		pictureDialog.validateImage(file);
		pictureDialog.show();
		
		pictureDialog.setGridSize(selectedData.gridSizeX, selectedData.gridSizeY);
		setTextField(selectedData);
		// picDlg::validateImage()で一回呼び出せれますがグリッドを変更したため再度呼び出します
	}
	
	public void setTexID(){
		
		int texID = Integer.valueOf(textTextureID.getText());
		
		pictureDialog.setTexID(texID);
	}
	
	public void setGridSize(){
		
		int gridSizeX = Integer.valueOf(textGridSizeX.getText());
		int gridSizeY = Integer.valueOf(textGridSizeY.getText());
		
		pictureDialog.setGridSize(gridSizeX, gridSizeY);
	}
	
	public void storeToDB(){
		
		DatabaseAccess dbAccess = new DatabaseAccess(databasePath);
		
		TextureData storeData = pictureDialog.getTextureData();
		dbAccess.addTextureData(storeData);
		
		tableModule.setTable(databasePath);
	}

	@Override
	public void openDatabase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackGroundColor(BackGroundColor color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFrameGrid(OnOrOff sw) {
		// TODO Auto-generated method stub
		
	}
}
