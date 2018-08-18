package textureComposer.database;

public class TableMakerForTextureData 
					extends TableMakerForTextureComposer{

	static {
	
		tableName = "TextureData";
	
		primaryKey = "ID";
	}
	
	static String[] fieldData ={
			
			"ID INT",
			"textureID INT",
			"pictureName VERCHAR(30)",
			"gridSizeX INT",
			"gridSizeY INT"
	};
	
//	public static void main(String[] args) {
//		
//		columnData = fieldData;
//		
//		make();
//	}
	
	public static void makeTable(){
		
		columnData = fieldData;
		
		make();
	}
}
