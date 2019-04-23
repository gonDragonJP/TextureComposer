package textureComposer.database;


public class TableMakerForTextureComposer extends SQLiteManager{
	
	static String dbPath = ".\\texDatabase\\texDB.db";
	
	static String tableName;
	
	static String primaryKey;
	
	static String[] columnData;
	
	public static void make(){
		
		SQLiteManager instance = new SQLiteManager();
		
		instance.initDatabase(dbPath);
		String columnsDef = instance.getColumnsDef(columnData, primaryKey);
		instance.createTable(tableName, columnsDef);
		instance.closeDatabase();
	}
}
