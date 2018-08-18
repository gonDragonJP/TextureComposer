package textureComposer;

import java.io.File;
import java.nio.file.Paths;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import textureComposer.MenuUtil.BackGroundColor;
import textureComposer.MenuUtil.OnOrOff;

public class PictureDialog extends Stage implements MenuUtil.MenuCallback{
	
	private Canvas canvas = new Canvas();
	private MenuBar menuBar = new MenuBar();
	
	private String pictureName ="(no image)";
	private Image image;
	private int canvasSizeX = 256;
	private int canvasSizeY = 256;
	private int gridSizeX = 64;
	private int gridSizeY = 64;
	
	private BackGroundColor backGroundColor = BackGroundColor.Checkerd;
	private boolean isGridOn = true;
	
	public class PictureData{
		
		public String picureName;
		public Image image;
		public int gridSizeX;
		public int gridSizeY;
	}
	
	private Callback<PictureData, Void> cbOfChangedImage;
	
	public PictureDialog(Window parentWnd){
		
		setPictureName(pictureName);
		this.initOwner(parentWnd);
		fitDialogSize(canvasSizeX, canvasSizeY);
		setScene();
		
		drawScreen();
	}
	
	public void setChangedImageListener(Callback<PictureData, Void> callBack){
		
		cbOfChangedImage = callBack;
	}
	
	public void setGridSize(int x, int y){
		
		if(x<=0 || y<=0) return;
		if(image.getWidth()%x !=0 || image.getHeight()%y !=0) return;
		
		this.gridSizeX = x;
		this.gridSizeY = y;
		
		if(isGridOn) drawScreen();
	}
	
	public PictureData getPictureData(){
		
		PictureData result = new PictureData();

		result.picureName = this.pictureName;
		result.image = this.image;
		result.gridSizeX = this.gridSizeX;
		result.gridSizeY = this.gridSizeY;
		
		return result;
	}
	
	private void setScene(){
		
		menuBar = MenuUtil.generateMenu(this);
	
		VBox root = new VBox();
		root.getChildren().addAll(menuBar, canvas);
		Scene scene = new Scene(root);
		this.setScene(scene);
	}
	
	private void setPictureName(String name){
		
		pictureName = name;
		this.setTitle(pictureName);
	}
	
	private void fitDialogSize(int width, int height){
		
		canvasSizeX = width;
		canvasSizeY = height;
		
		canvas.setWidth(width);
		canvas.setHeight(height);
		
		this.sizeToScene();
	}
	
	private void drawScreen(){
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		fillBackGround();
		gc.drawImage(image, 0, 0);
		drawGrid();
	}
	
	private void fillBackGround(){
		
		switch(backGroundColor){
		
		case Black:
			fillMonotone(Color.BLACK);
			break;
		case White:
			fillMonotone(Color.WHITE);
			break;
		case Checkerd:
			fillCheckeredPattern();
			break;
		default:
		}
	}
	
	private void fillMonotone(Color color){
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(color);
		gc.fillRect(0, 0, canvasSizeX, canvasSizeY);
	}
	
	private void fillCheckeredPattern(){
		
		final int PAT_SIZE = 16;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		int x=0, y;
		do{	
			y=0;
			do{
				gc.setFill((x+y)%2==0 ? Color.WHITE : Color.GAINSBORO);
				gc.fillRect(PAT_SIZE*x, PAT_SIZE*y, PAT_SIZE, PAT_SIZE);
				
			}while(++y*PAT_SIZE < canvasSizeY);
		}while(++x*PAT_SIZE < canvasSizeX);
	}
	
	private void drawGrid(){
		
		if(!isGridOn) return;
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.FIREBRICK);
		gc.setLineDashes(4);
		
		int x= gridSizeX, y;
		
		while(x < canvasSizeX){	
			y= gridSizeY;
			gc.strokeLine(x, 0, x, canvasSizeY);
			
			while(y < canvasSizeY){
				gc.strokeLine(0, y, canvasSizeX, y);
				y+= gridSizeY;
			}
			x+= gridSizeX;
		}
	}
	
	//Menuからのコールバック処理

	@Override
	public void setBackGroundColor(BackGroundColor color) {
		
		backGroundColor = color;
		drawScreen();
	}

	@Override
	public void setFrameGrid(OnOrOff sw) {
		
		isGridOn = (sw == OnOrOff.On);
		drawScreen();
	}

	@Override
	public void openImageFile() {
		
		File file = new FileDialog().open();
		
		if(file == null) return;
		
		setImage(file);
		drawScreen();
		
		if(cbOfChangedImage != null) cbOfChangedImage.call(getPictureData());
	}
	
	private void setImage(File file){
		
		setPictureName(file.getName());
		
		image = new Image(
				Paths.get(file.getPath()).toUri().toString()
		);
		fitDialogSize((int)image.getWidth(), (int)image.getHeight());
	}
}
