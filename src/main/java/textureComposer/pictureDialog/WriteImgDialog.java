package textureComposer.pictureDialog;

import java.io.File;

import javafx.stage.FileChooser;

public class WriteImgDialog{
	
	private FileChooser fc = new FileChooser();
	
	WriteImgDialog(){
		
		initialize();
	}
	
	private void initialize(){
		
		fc.setTitle("�摜�t�@�C���ۑ�");
		fc.setInitialDirectory(new File(".\\texDatabase\\image"));
	}
	
	public File open(){
		
		File file = fc.showSaveDialog(null);
		
		return file;
	}

}
