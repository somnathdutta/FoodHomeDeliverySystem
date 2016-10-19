package menus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

public class MenuItemData {

private static  java.sql.Connection connection = null;
	
	
	public static void loadTree(MenusHierarchyBean hierarchyBean, MenuItem m1, Integer userId, Integer roleId) {
		
		try {

			sql1:{
			
				PreparedStatement preparedStatementSQL1 = null;
				
				try {

					preparedStatementSQL1 = connection.prepareStatement("SELECT * " +
																		"FROM vw_master_page_menus_user_privs_details " +
																		"WHERE parent_menus_id = ? and user_id = ? and role_id = ?");
					
					preparedStatementSQL1.setInt(1, hierarchyBean.menusId);
					preparedStatementSQL1.setInt(2, userId);
					preparedStatementSQL1.setInt(3, roleId);
					
					
					System.out.println(preparedStatementSQL1);
					
					ResultSet resultSetSQL1 = preparedStatementSQL1.executeQuery();
					
					while (resultSetSQL1.next()) {

						MenusHierarchyBean menuName = new MenusHierarchyBean();
						
						menuName.menusId = resultSetSQL1.getInt("menus_id");

						menuName.parentMenusId = resultSetSQL1.getInt("parent_menus_id");

						menuName.menusName = resultSetSQL1.getString("menus_name");

						System.out.println("-->>MENUS NAME: "+ menuName.menusName+"<<--");
						
						menuName.level = resultSetSQL1.getInt("level");
						
						menuName.pageLink = resultSetSQL1.getString("menus_page_link");
						
						System.out.println("-->>MENUS LINK: "+ menuName.pageLink+"<<--");
						
						menuName.addAccess = resultSetSQL1.getString("add_access");
						
						menuName.viewAccess = resultSetSQL1.getString("view_access");
						
						menuName.editAccess = resultSetSQL1.getString("edit_access");
						
						menuName.deleteAccess = resultSetSQL1.getString("delete_access");	
						
					    MenuItem m1_lv_level = new MenuItem(menuName.menusName, menuName.level, menuName.pageLink, menuName.menusId,
					    			menuName.addAccess, menuName.viewAccess, menuName.editAccess, menuName.deleteAccess);
						m1.addChild(m1_lv_level);
						
						loadTree(menuName, m1_lv_level, userId, roleId);
						
					}
					
				} catch (Exception e) {

					Messagebox.show("Error Due To: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();

				} finally {

					if (preparedStatementSQL1 != null) {
						preparedStatementSQL1.close();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static List<MenuItem> getAllMenus(MenusHierarchyBean masterBean, java.sql.Connection connection2, Integer userId, Integer roleId, ArrayList<MenuItem> menus) {

		//menus.clear();
		
		System.out.println("---||| MENUS SIZE FIRST: "+ menus.size()+"--|||");
		MenuItem m1 = new MenuItem(masterBean.menusName, 1, masterBean.pageLink, masterBean.menusId,
										masterBean.addAccess,masterBean.viewAccess ,masterBean.editAccess, masterBean.deleteAccess);

		MenuItemData.connection = connection2;
		
		loadTree(masterBean, m1, userId, roleId);	
		menus.add(m1);

		System.out.println("---||| MENUS SIZE BEFORE RETURN: "+ menus.size()+"--|||");

		return new ArrayList<MenuItem>(menus);
	}
}
