package textureComposer.pictureDialog;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuUtil {
	
	public enum BackGroundColor{
		Black, White, Checkerd
	}
	
	public enum OnOrOff{
		On, Off
	}
	
	public interface MenuCallback{
	
		void setBackGroundColor(BackGroundColor color);
		void setFrameGrid(OnOrOff sw);
		
		void openImageFile();
		void addSeqCutFrames();
		void saveImage();
		
		void clearImage();
		void changeImageSize();
	}
	
	private static MenuCallback menuCallback;

	public static MenuBar generateMenu(MenuCallback callable){
		
		menuCallback = callable;
		MenuBar menuBar = new MenuBar();
		
		String[] menuNames = {"File","Image","Setting"};
		Menu[] menus = generateMenuArray(menuNames);
		menuBar.getMenus().addAll(menus);
		
		setFileMenu(menus[0]);
		setImageMenu(menus[1]);
		setSettingMenu(menus[2]);
	
		return menuBar;
	}
	
	private static void setFileMenu(Menu menu){
		
		String[] menuItemNames = {"Open Image File", "Add Sequencial CutFrames","Save Image"};
		MenuItem[] menuItems = generateMenuItemArray(menuItemNames);
		menu.getItems().addAll(menuItems);
		
		menuItems[0].setOnAction(event->menuCallback.openImageFile());
		menuItems[1].setOnAction(event->menuCallback.addSeqCutFrames());
		menuItems[2].setOnAction(event->menuCallback.saveImage());
	}
	
	private static void setImageMenu(Menu menu){
		
		String[] menuItemNames = {"Clear Image","Change Image Size"};
		MenuItem[] menuItems = generateMenuItemArray(menuItemNames);
		menu.getItems().addAll(menuItems);
		
		menuItems[0].setOnAction(event->menuCallback.clearImage());
		menuItems[1].setOnAction(event->menuCallback.changeImageSize());
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
