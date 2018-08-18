package textureComposer.pictureDialog.seqReadDialog;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import textureComposer.pictureDialog.MenuUtil.BackGroundColor;


public class PicDialog_seqReadDlg extends Stage {
	
	private Canvas canvas = new Canvas();
	
	private int canvasSizeX = 256;
	private int canvasSizeY = 256;
	
	private int gridSizeX, gridSizeY;
	private int cutLeft, cutTop;
	
	private String pictureName ="no title";
	private Image image = new WritableImage(canvasSizeX, canvasSizeY);
	private BackGroundColor backGroundColor = BackGroundColor.Checkerd;
	
	public PicDialog_seqReadDlg(Window parentWnd, int gridSizeX, int gridSizeY){
		
		this.gridSizeX = gridSizeX;
		this.gridSizeY = gridSizeY;
		
		this.initOwner(parentWnd);
		setPictureName(pictureName);
		
		setScene();
		fitDialogSize(canvasSizeX, canvasSizeY);
		
		drawScreen();
	}
	
	public void pasteCutImage(WritableImage dstImg, int x, int y){
		
		int[] buffer = getIntBufferOfCutImage();
		writeCutImageFromIntBuffer(dstImg, x, y, buffer);
	}
	
	public void validateCutPos(int x, int y){
		
		if(x<=0 || y<=0) return;
		if(x + gridSizeX > image.getWidth() || y + gridSizeY > image.getHeight()) return;
		
		cutLeft = x;
		cutTop = y;
		
		drawScreen();
	}
	
	private void setScene(){
	
		VBox root = new VBox();
		root.getChildren().addAll(canvas);
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
		drawCutFrame();
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
	
	private void drawCutFrame(){
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.FIREBRICK);
		gc.setLineDashes(4);
		
		gc.strokeRect(cutLeft, cutTop, gridSizeX, gridSizeY);
	}
	
	public void validateImage(File file){
		
		setImage(file);
		drawScreen();
	}
	
	private void setImage(File file){
		
		setPictureName(file.getName());
		
		image = new Image(
				Paths.get(file.getPath()).toUri().toString()
		);
		
		fitDialogSize((int)image.getWidth(), (int)image.getHeight());
	}
	
	private int[] getIntBufferOfCutImage(){
		
		WritablePixelFormat<IntBuffer> pixelformat = WritablePixelFormat.getIntArgbInstance();
		int[] buffer = new int[gridSizeX * gridSizeY];
		int offset = 0;
		int scanlineStride = gridSizeX;
		
		image.getPixelReader().getPixels(
				cutLeft, cutTop, gridSizeX, gridSizeY, 
				pixelformat, buffer, offset, scanlineStride
		);
		return buffer;
	}
	
	private void writeCutImageFromIntBuffer(WritableImage dstImg, int x, int y, int[] buffer){
		
		WritablePixelFormat<IntBuffer> pixelformat = WritablePixelFormat.getIntArgbInstance();
		int offset = 0;
		int scanlineStride = gridSizeX;
		
		dstImg.getPixelWriter().setPixels(
				x, y, gridSizeX, gridSizeY, 
				pixelformat, buffer, offset, scanlineStride
		);
	}
}
