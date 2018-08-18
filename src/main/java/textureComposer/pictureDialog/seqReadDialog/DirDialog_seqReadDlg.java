package textureComposer.pictureDialog.seqReadDialog;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class DirDialog_seqReadDlg{
	
	private DirectoryChooser dc = new DirectoryChooser();
	
	private Window ownerWindow;
	
	DirDialog_seqReadDlg(Window parent){
		
		this.ownerWindow = parent;
		initialize();
	}
	
	private void initialize(){
		
		dc.setTitle("Select Series Directory");
		dc.setInitialDirectory(new File(".\\texDatabase\\image"));
	}
	
	public File open(){
		
		File file = dc.showDialog(ownerWindow);
		
		return file;
	}
	
	public void outFiles(File file){ //　デバッグ用
		
		if(file == null) return;
		
		File[] files = file.listFiles();
		for(File e: files){
			
			System.out.println(e.getPath());
		}
	}

}
