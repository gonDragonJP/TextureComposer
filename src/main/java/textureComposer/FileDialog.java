package textureComposer;

import java.io.File;

import javafx.stage.FileChooser;

public class FileDialog{
	
	private FileChooser fc = new FileChooser();
	
	FileDialog(){
		
		initialize();
	}
	
	private void initialize(){
		
		fc.setTitle("画像ファイル選択");
		fc.setInitialDirectory(new File(".\\texDatabase\\image"));
	}
	
	public File open(){
		
		File file = fc.showOpenDialog(null);
		
		return file;
	}

}
