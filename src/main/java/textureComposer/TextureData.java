package textureComposer;

import javafx.scene.image.WritableImage;

public class TextureData{
	
	private int databaseID; // �f�[�^�x�[�X�̂݃A�N�Z�X
	
	public int textureID;
	public String pictureName;
	public WritableImage image;
	public int gridSizeX;
	public int gridSizeY;
	
	public void setDatabaseID(int id){
		
		this.databaseID = id;
	}
	
	public int getDatabaseID(){
		
		return databaseID;
	}
}
