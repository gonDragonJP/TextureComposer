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
	
	private final String basicTableName ="TextureTable_Stage_";
	public int editStage = 2;

	private PictureDialog pictureDialog;
	public TableModule tableModule;
	
	public static void main(String[] args){
		
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		pictureDialog = new PictureDialog(stage);
		tableModule = new TableModule();
		
		SceneUtil.setScene(this, stage);
		
		setPictureDialog();
		setTableModule();
		
		stage.show();
		pictureDialog.show();
	}
	
	private void setPictureDialog(){
		
		SceneUtil.setTextField(pictureDialog.getTextureData());
		
		pictureDialog.setChangedImageListener(
				picdata -> {SceneUtil.setTextField(picdata); return null;}
		);
	}
	
	public String getEditTableName(){
		
		return basicTableName + String.valueOf(editStage);
	}
	
	private void setTableModule(){
		
		DatabaseAccess dbAccess = 
				new DatabaseAccess(databasePath, getEditTableName());
	
		tableModule.setTable(dbAccess);
		
		TableActionable actionable = new TableActionable(){

			@Override
			public void deleteTexData() {
				
				deleteSelectedTexData();
			}

			@Override
			public void selectRow(TextureData textureData) {
				
				selectDBTableRow(textureData);
			}
			
		};
		
		tableModule.setTableActionable(actionable);
	}
	
	private void deleteSelectedTexData(){
		
		DatabaseAccess dbAccess = 
				new DatabaseAccess(databasePath, getEditTableName());
		
		tableModule.deleteSelectedTexData(dbAccess);
	}
	
	private void selectDBTableRow(TextureData selectedData){
		
		File file = new File(imageDir + selectedData.pictureName);
		pictureDialog.validateImage(file);
		pictureDialog.show();
		
		pictureDialog.setGridSize(selectedData.gridSizeX, selectedData.gridSizeY);
		SceneUtil.setTextField(selectedData);
		// picDlg::validateImage()で一回呼び出せれますがグリッドを変更したため再度呼び出します
	}
	
	public void setTexID(){
		
		int texID = Integer.valueOf(SceneUtil.textTextureID.getText());
		
		pictureDialog.setTexID(texID);
	}
	
	public void setGridSize(){
		
		int gridSizeX = Integer.valueOf(SceneUtil.textGridSizeX.getText());
		int gridSizeY = Integer.valueOf(SceneUtil.textGridSizeY.getText());
		
		pictureDialog.setGridSize(gridSizeX, gridSizeY);
	}
	
	public void storeToDB(){
		
		DatabaseAccess dbAccess = 
				new DatabaseAccess(databasePath, getEditTableName());
		
		TextureData storeData = pictureDialog.getTextureData();
		dbAccess.addTextureData(storeData);
		
		tableModule.setTable(dbAccess);
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
