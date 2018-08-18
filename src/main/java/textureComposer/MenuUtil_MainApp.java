package textureComposer;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuUtil_MainApp {
	
	public enum BackGroundColor{
		Black, White, Checkerd
	}
	
	public enum OnOrOff{
		On, Off
	}
	
	public interface MenuCallback{
	
		void setBackGroundColor(BackGroundColor color);
		void setFrameGrid(OnOrOff sw);
		void openDatabase();
	}
	
	private static MenuCallback menuCallback;

	public static MenuBar generateMenu(MenuCallback callable){
		
		menuCallback = callable;
		MenuBar menuBar = new MenuBar();
		
		String[] menuNames = {"Database","Setting"};
		Menu[] menus = generateMenuArray(menuNames);
		menuBar.getMenus().addAll(menus);
		
		setDatabaseMenu(menus[0]);
		//setSettingMenu(menus[1]);
	
		return menuBar;
	}
	
	private static void setDatabaseMenu(Menu menu){
		
		String[] menuItemNames = {"Open Database"};
		MenuItem[] menuItems = generateMenuItemArray(menuItemNames);
		menu.getItems().addAll(menuItems);
		
		menuItems[0].setOnAction(event->{menuCallback.openDatabase();});
	}
	
	private static void setSettingMenu(Menu menu){
		
		String[] menuNames = {"BackGroundColor", "FrameGrid"};
		Menu[] menus = generateMenuArray(menuNames);
		menu.getItems().addAll(menus);
		
		setBackGroundColorMenu(menus[0]);
		setFrameGridMenu(menus[1]);
	}
	
	private static void setBackGroundColorMenu(Menu menu){
		
		MenuItem[] menuItems = generateMenuItemArray(BackGroundColor.values());
		menu.getItems().addAll(menuItems);
		
		for(BackGroundColor e: BackGroundColor.values()){
			
			menuItems[e.ordinal()].setOnAction(event -> {menuCallback.setBackGroundColor(e);});
		}
	}
	
	private static void setFrameGridMenu(Menu menu){
		
		MenuItem[] menuItems = generateMenuItemArray(OnOrOff.values());
		menu.getItems().addAll(menuItems);
		
		for(OnOrOff e: OnOrOff.values()){
			
			menuItems[e.ordinal()].setOnAction(event -> {menuCallback.setFrameGrid(e);});
		}
	}
	
	private static Menu[] generateMenuArray(String[] menuNames){
		
		Menu[] menus = new Menu[menuNames.length];
		for(int i=0; i<menus.length; i++){
			menus[i] = new Menu(menuNames[i]);
		}
		return menus;
	}
	
	private static MenuItem[] generateMenuItemArray(Object[] menuItemEnums){
		
		MenuItem[] menuItems = new MenuItem[menuItemEnums.length];
		for(int i=0; i<menuItems.length; i++){
			menuItems[i] = new MenuItem(menuItemEnums[i].toString());
		}
		return menuItems;
	}
}
