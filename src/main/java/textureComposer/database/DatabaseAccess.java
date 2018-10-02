package textureComposer.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import textureComposer.TextureData;

public class DatabaseAccess {
	
	private String filePath;
	
	private String tableName;
	
	public DatabaseAccess(String filePath, String tableName){
		
		this.filePath = filePath;
		this.tableName = tableName;
	}
	
	public void setTexDataList(ArrayList<TextureData> texDataList){
		
		SQLiteManager.initDatabase(filePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from "+tableName+" order by textureID;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				texDataList.add(generateTexData(resultSet));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private TextureData generateTexData(ResultSet resultSet){
		
		TextureData TextureData = new TextureData();
		
		setTextureData(TextureData, resultSet);
		
		return TextureData;
	}
	
	private void setTextureData(TextureData TextureData, ResultSet resultSet){
		
		try {
			TextureData.setDatabaseID(resultSet.getInt("ID"));
			TextureData.textureID = resultSet.getInt("textureID");
			TextureData.pictureName = resultSet.getString("pictureName");
			TextureData.gridSizeX = resultSet.getInt("gridSizeX");
			TextureData.gridSizeY = resultSet.getInt("gridSizeY");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void addTextureList(ArrayList<TextureData> textureList){
		
		SQLiteManager.initDatabase(filePath);
		
		for(TextureData e: textureList){
			
			add(e);
		}
		
		SQLiteManager.closeDatabase();
	}
	
	public void addTextureData(TextureData TextureData){
		
		SQLiteManager.initDatabase(filePath);
			
		add(TextureData);
		
		SQLiteManager.closeDatabase();
	}
	
	private void add(TextureData textureData){
		
		String sql = "insert into "+tableName+" values(";
		
		sql += "NULL,";
		sql += String.valueOf(textureData.textureID) +",";
		sql += "'"+ textureData.pictureName +"',";
		sql += String.valueOf(textureData.gridSizeX) +",";
		sql += String.valueOf(textureData.gridSizeY);
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	public void addNewTextureData(){
		
		SQLiteManager.initDatabase(filePath);
		
		add(generateNewTextureData());
		
		SQLiteManager.closeDatabase();
	}
	
	private TextureData generateNewTextureData(){
		
		TextureData TextureData = new TextureData();
		
		return TextureData;
	}
	
	public void deleteTextureData(TextureData textureData){
		
		SQLiteManager.initDatabase(filePath);
		
		String sql = "delete from "+tableName+" where ID=";
		
		sql += String.valueOf(textureData.getDatabaseID());
		
		sql += ";";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
		
		SQLiteManager.closeDatabase();
	}
}

