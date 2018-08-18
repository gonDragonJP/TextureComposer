package textureComposer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
	
	private static Connection connection;
	private static Statement statement;

	public static void initDatabase(String filePath){
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			
			connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
			statement = connection.createStatement();
			
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void closeDatabase(){
		
		try {
			
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void createTable(String tableName, String defColumns){

		try {
			statement.executeUpdate(
					"create table "+ tableName +"("+ defColumns +");"
			);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static String getColumnsDef(String[] fieldData, String primaryKey){
		
		String result ="";
		
		for(String e: fieldData) result += (e+",");
		
		result += "primary Key(" + primaryKey +")";
		
		return result;
	}
	
	public static void addFields(String tableName, String[] fieldData){
		
		for(String e: fieldData){
			
			addField(tableName, e);
		}
	}
	
	public static void addField(String tableName, String fieldData){
		
		try {
			statement.executeUpdate
				("alter table "+ tableName + " add " + fieldData +";");
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void update(String sql){
		
		try {
			statement.executeUpdate(sql);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static ResultSet getResultSet(String sql){
		
		ResultSet result = null;
		
		try {
			result = statement.executeQuery(sql);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return result;
	}
}
