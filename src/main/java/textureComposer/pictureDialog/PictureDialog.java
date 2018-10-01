package textureComposer.pictureDialog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Pair;
import textureComposer.TextureData;
import textureComposer.pictureDialog.MenuUtil.BackGroundColor;
import textureComposer.pictureDialog.MenuUtil.OnOrOff;
import textureComposer.pictureDialog.seqReadDialog.SeqReadDialog;

public class PictureDialog extends Stage implements MenuUtil.MenuCallback {

	private Canvas canvas = new Canvas();
	private MenuBar menuBar = new MenuBar();
	private Tooltip tooltip = new Tooltip();

	private int textureID = 0;
	private int canvasSizeX = 256;
	private int canvasSizeY = 256;
	private int gridSizeX = 64;
	private int gridSizeY = 64;

	private String pictureName = "no title";
	private WritableImage image = new WritableImage(canvasSizeX, canvasSizeY);

	private BackGroundColor backGroundColor = BackGroundColor.Checkerd;
	private boolean isGridOn = true;

	private Callback<TextureData, Void> cbOfChangedImage;

	public PictureDialog(Window parentWnd) {

		this.initOwner(parentWnd);
		setPictureName(pictureName);

		Font tipFont = new Font(16);
		tooltip.setFont(tipFont);
		Tooltip.install(canvas, tooltip);
		canvas.setOnMouseMoved(event -> setDataTooltip(event));

		setScene();
		fitDialogSize(canvasSizeX, canvasSizeY);

		drawScreen();
	}

	public void setChangedImageListener(Callback<TextureData, Void> callBack) {

		cbOfChangedImage = callBack;
	}

	public void setTexID(int texID) {

		this.textureID = texID;
	}

	public void setGridSize(int x, int y) {

		if (x <= 0 || y <= 0)
			return;
		if (image.getWidth() % x != 0 || image.getHeight() % y != 0)
			return;

		this.gridSizeX = x;
		this.gridSizeY = y;

		if (isGridOn)
			drawScreen();
	}

	public TextureData getTextureData() {

		TextureData result = new TextureData();

		result.textureID = this.textureID;
		result.pictureName = this.pictureName;
		result.image = this.image;
		result.gridSizeX = this.gridSizeX;
		result.gridSizeY = this.gridSizeY;

		return result;
	}

	private void setScene() {

		menuBar = MenuUtil.generateMenu(this);

		VBox root = new VBox();
		root.getChildren().addAll(menuBar, canvas);
		Scene scene = new Scene(root);
		this.setScene(scene);
	}

	private void setPictureName(String name) {

		pictureName = name;
		this.setTitle(pictureName);
	}

	private void fitDialogSize(int width, int height) {

		canvasSizeX = width;
		canvasSizeY = height;

		canvas.setWidth(width);
		canvas.setHeight(height);

		this.sizeToScene();
	}

	private void drawScreen() {

		GraphicsContext gc = canvas.getGraphicsContext2D();

		fillBackGround();
		gc.drawImage(image, 0, 0);
		drawGrid();
	}

	private void fillBackGround() {

		switch (backGroundColor) {

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

	private void fillMonotone(Color color) {

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(color);
		gc.fillRect(0, 0, canvasSizeX, canvasSizeY);
	}

	private void fillCheckeredPattern() {

		final int PAT_SIZE = 16;
		GraphicsContext gc = canvas.getGraphicsContext2D();

		int x = 0, y;
		do {
			y = 0;
			do {
				gc.setFill((x + y) % 2 == 0 ? Color.WHITE : Color.GAINSBORO);
				gc.fillRect(PAT_SIZE * x, PAT_SIZE * y, PAT_SIZE, PAT_SIZE);

			} while (++y * PAT_SIZE < canvasSizeY);
		} while (++x * PAT_SIZE < canvasSizeX);
	}

	private void drawGrid() {

		if (!isGridOn)
			return;

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.FIREBRICK);
		gc.setLineDashes(4);

		int x = gridSizeX, y;

		while (x < canvasSizeX) {
			y = gridSizeY;
			gc.strokeLine(x, 0, x, canvasSizeY);

			while (y < canvasSizeY) {
				gc.strokeLine(0, y, canvasSizeX, y);
				y += gridSizeY;
			}
			x += gridSizeX;
		}
	}

	// Menuからのコールバック処理

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

		File file = new ReadImgDialog().open();

		if (file == null)
			return;

		validateImage(file);
	}

	public void validateImage(File file) {

		setImage(file);
		drawScreen();

		if (cbOfChangedImage != null)
			cbOfChangedImage.call(getTextureData());
	}

	private void setImage(File file) {

		setPictureName(file.getName());

		Image readImage = new Image(Paths.get(file.getPath()).toUri().toString());
		convertImageToWritable(readImage);
		fitDialogSize((int) image.getWidth(), (int) image.getHeight());
	}

	private void convertImageToWritable(Image srcImage) {

		int srcWidth = (int) srcImage.getWidth();
		int srcHeight = (int) srcImage.getHeight();

		image = new WritableImage(srcWidth, srcHeight);
		PixelReader reader = srcImage.getPixelReader();
		image.getPixelWriter().setPixels(0, 0, srcWidth, srcHeight, reader, 0, 0);
	}

	private void setDataTooltip(MouseEvent event) {

		int frameX = (int) event.getX() / gridSizeX;
		int frameY = (int) event.getY() / gridSizeY;

		int frameNum = frameY * (int) (image.getWidth() / gridSizeX) + frameX;

		String data = String.format("textureID: %d\nframe : %d", textureID, frameNum);
		tooltip.setText(data);
	}

	@Override
	public void addSeqCutFrames() {

		SeqReadDialog seqReadDlg = new SeqReadDialog(this, getTextureData());
		seqReadDlg.open();
		seqReadDlg.setOnHidden(e -> drawScreen());
	}

	@Override
	public void saveImage() {

		File file = new WriteImgDialog().open();

		if (file == null)
			return;

		String suffix;
		String fileName = file.getName();
		String[] nameParts = fileName.split(".");

		if (nameParts.length == 0) {

			file = new File(file.getPath() + ".png");
			suffix = "png";
		} else {

			suffix = nameParts[1];
		}

		try {

			ImageIO.write(SwingFXUtils.fromFXImage(image, null), suffix, file);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	@Override
	public void clearImage() {
		
		for(int x=0; x<image.getWidth(); x++) {
			for(int y=0; y<image.getHeight(); y++) {
				
				image.getPixelWriter().setArgb(x, y, getBackGroundArgb());
			}
		}
		drawScreen();
	}

	@Override
	public void changeImageSize() {

		InputImageSizeDialog dialog = new InputImageSizeDialog();

		Optional<Pair<Integer, Integer>> result = dialog.open(this.getX() - 100, this.getY() - 100,
				(int) image.getWidth(), (int) image.getHeight());

		if (result.isPresent()) {
			int newWidth = result.get().getKey();
			int newHeight = result.get().getValue();

			image = getNewSizeCopiedImage(newWidth, newHeight);

			if (cbOfChangedImage != null)	cbOfChangedImage.call(getTextureData());
			fitDialogSize(newWidth, newHeight);
			drawScreen();
		}
	}

	private WritableImage getNewSizeCopiedImage(int newWidth, int newHeight) {
		
		WritableImage newImage = new WritableImage(newWidth, newHeight);
		PixelReader reader = image.getPixelReader();
		
		for(int x=0; x<newWidth; x++) {
			for(int y=0; y<newWidth; y++) {
				
				int argb;
				
				try {
					argb = reader.getArgb(x, y);
				}catch(Exception e) {
					
					argb = getBackGroundArgb();
				}
				newImage.getPixelWriter().setArgb(x, y, argb);
			}
		}
		return newImage;
	}
	
	private int getBackGroundArgb() {
		
		int argb;
		
		switch(backGroundColor) {
		
		case Black:
			argb = 0xFF000000;	break;
		case White:
			argb = 0xFFFFFFFF;	break;
		case Checkerd:
		default:
			argb = 0x00000000;
		}
		return argb;
	}
}
