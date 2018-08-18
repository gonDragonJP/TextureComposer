package textureComposer.pictureDialog;

import java.io.File;

import javafx.stage.FileChooser;

public class ReadImgDialog{
	
	private FileChooser fc = new FileChooser();
	
	ReadImgDialog(){
		
		initialize();
	}
	
	private void initialize(){
		
		fc.setTitle("�摜�t�@�C���I��");
		fc.setInitialDirectory(new File(".\\texDatabase\\image"));
	}
	
	public File open(){
		
		File file = fc.showOpenDialog(null);
		
		return file;
	}

}
