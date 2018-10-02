
package textureComposer;

import java.lang.reflect.Field;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneUtil {
	
	public static TextField textTextureID = new TextField();
	public static TextField textPictureName = new TextField();
	public static TextField textGridSizeX = new TextField();
	public static TextField textGridSizeY = new TextField();
	public static TextField textTableName = new TextField();
	
	public static Button storeButton = new Button("Store To DB");
	
	private static MainApp mainApp;
	
	public static void setScene(MainApp argApp, Stage stage){
		
		mainApp = argApp;
		
		set(stage);
	}
	
	public static void setTextField(TextureData data){
		
		String picSizePS =""; 
		
		if(data.image !=null){
			
			picSizePS = String.format(" ( %d * %d )", 
					(int)data.image.getWidth(), (int)data.image.getHeight());
		}
		
		textTextureID.setText(String.valueOf(data.textureID));
		textPictureName.setText(data.pictureName  + picSizePS);
		textGridSizeX.setText(String.valueOf(data.gridSizeX));
		textGridSizeY.setText(String.valueOf(data.gridSizeY));
		textTableName.setText(mainApp.getEditTableName());
	}
	
	private static void set(Stage stage){
		
		stage.setTitle("TextureComposer ver1.0");
		stage.setWidth(650);
		stage.setHeight(500);
		
		VBox root = new VBox();
		
		VBox box = new VBox();
		box.setPadding(new Insets(30));
		box.setSpacing(10);
		box.getChildren().addAll(genPicDataBox(), genTablePane());
		
		
		root.getChildren().addAll(MenuUtil_MainApp.generateMenu(mainApp), box);
		Scene scene = new Scene(root);
		stage.setScene(scene);

	}
	
	private static Pane genPicDataBox(){
		
		Label label0= new Label("TexID");
		Label label1= new Label("PictureName (Size)");
		Label label2= new Label("GridSizeX");
		Label label3= new Label("GridSizeY");
		
		Label label4= new Label("DB_TableName");
		
		textTextureID.setPrefWidth(50);
		textPictureName.setMaxWidth(250);
		textPictureName.setMinWidth(250);
		textPictureName.setEditable(false);
		textGridSizeX.setMaxWidth(50);
		textGridSizeY.setMaxWidth(50);
		textTableName.setPrefWidth(250);
		textTableName.setEditable(false);
		storeButton.setPrefWidth(100);
		
		VBox box = new VBox();
		box.setSpacing(10);
		
		HBox box2 = new HBox();
		box2.setSpacing(30);
		box2.getChildren().addAll(label0, label1, label2, label3);
		
		HBox box3 = new HBox();
		box3.setSpacing(10);
		box3.getChildren().addAll(
				textTextureID,
				textPictureName, 
				textGridSizeX, 
				textGridSizeY,
				storeButton
				);
		
		textTextureID.setOnAction(event->mainApp.setTexID());
		textGridSizeX.setOnAction(event->mainApp.setGridSize());
		textGridSizeY.setOnAction(event->mainApp.setGridSize());
		
		storeButton.setOnAction(event->mainApp.storeToDB());
		
		HBox box4 = new HBox();
		box4.setSpacing(10);
		box4.getChildren().addAll(label4, textTableName);
	
		box.getChildren().addAll(box2, box3, box4);
		
		return box;
	}
	
	public enum TexDataColumn{
		
		TexID(40,"textureID"),
		Sheet_PictureName(200,"pictureName"),
		GridX(40,"gridSizeX"),
		GridY(40,"gridSizeY");
		
		public int width;
		public String fieldName;
		
		TexDataColumn(int width, String fieldName){
			
			this.width = width;
			this.fieldName = fieldName;
		}
	}
	
	private static Pane genTablePane(){
		
		TableView<TextureData> tableView = mainApp.tableModule.getView();
		Pane pane = new Pane();
		
		tableView.setPrefWidth(550);
		tableView.setPrefHeight(300);
		
		TableColumn<TextureData, String>[] columns 
			= new TableColumn[TexDataColumn.values().length];
		
		for(TexDataColumn e: TexDataColumn.values()){
			
			columns[e.ordinal()] = new TableColumn<TextureData, String>(e.toString());
			columns[e.ordinal()].setPrefWidth(e.width);
		
			columns[e.ordinal()].setCellValueFactory(
					param -> {
						String colString = 
								getReflectedFieldAsString(param.getValue(),e.fieldName);
						return new SimpleStringProperty(colString);
					}
			);
		}
		
		tableView.getColumns().addAll(columns);
		tableView.setOnMouseClicked(event -> mainApp.tableModule.onMouseClicked(event));
		
		pane.getChildren().add(tableView);
		
		return pane;
	}
	
	private static String getReflectedFieldAsString(Object object, String fieldName){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(fieldName);
			
			return field.get(object).toString();
		
		}catch(ReflectiveOperationException e){
		
		}
		return "null";
	}
}
