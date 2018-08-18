package textureComposer;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import textureComposer.database.DatabaseAccess;

public class TableModule {
	
	private TableView<TextureData> tableView = new TableView<>();	
	private ArrayList<TextureData> dataList = new ArrayList<>();
	
	private Callback<Void, Void> cbOfDeleteTexData;
	private Callback<TextureData, Void> cbOfSelectRow; 
	
	public TableModule(){
		
	}
	
	public TableView getView(){
		
		return tableView;
	}
	
	public void setDeleteTexDataListener(Callback<Void, Void> callback){
		
		cbOfDeleteTexData =callback;
	}
	
	public void setSelectRowListener(Callback<TextureData, Void> callback){
		
		cbOfSelectRow =callback;
	}
	
	public void setTable(String databasePath){
		
		dataList.clear();
		
		DatabaseAccess dbAccess = new DatabaseAccess(databasePath);
		dbAccess.setTexDataList(dataList);
		
		ObservableList<TextureData> tableData = FXCollections.observableArrayList();
		tableData.setAll(dataList);
		tableView.itemsProperty().setValue(tableData);
	}
	
	public void onMouseClicked(MouseEvent event){
		
		if(event.getButton() == MouseButton.PRIMARY){
			
			if(event.getClickCount() == 1) selectTableRow();
		}
	
		if(event.getButton() == MouseButton.SECONDARY){
			
			if(event.getClickCount() == 1) openEventContextMenu();
		}
	}
	
	private void selectTableRow(){
		
		TextureData data = tableView.getSelectionModel().getSelectedItem();
		
		cbOfSelectRow.call(data);
	}
	
	private void openEventContextMenu(){
		
		ContextMenu contextMenu = new ContextMenu();
		MenuItem[] menuItem  = new MenuItem[1];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		String[] menuText = {"delete"};
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e -> cbOfDeleteTexData.call(null));
	
		contextMenu.getItems().addAll(menuItem);
		tableView.setContextMenu(contextMenu);
	}
	
	public void deleteSelectedTexData(String databasePath){
		
		TextureData data = tableView.getSelectionModel().getSelectedItem();
		
		DatabaseAccess dbAccess = new DatabaseAccess(databasePath);
		dbAccess.deleteTextureData(data);
		
		setTable(databasePath);
	}
}
