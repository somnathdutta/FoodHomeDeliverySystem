package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import menus.MenuItem;
import menus.MenuItemData;
import menus.MenusHierarchyBean;
import menus.MyTreeModel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;

public class HomePageViewModel {

	Session session = null;

	private String name;

	private String departmentName;

	private Integer roleId;

	private Connection connection = null;

	@Wire("#mytree")
	private Tree mytree;
	
	@Wire("#inclResults")
	private Include include;

	private TreeModel _model;
	private List<MenuItem> allMenus = new ArrayList<MenuItem>();

	private ArrayList<MenusHierarchyBean> menusHierarchyBeanList = new ArrayList<MenusHierarchyBean>();
	
	/*USER ID IS THE ID OF THE TABLE CM_USER*/
	private Integer userId;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view)
			throws Exception {

		Selectors.wireComponents(view, this, false);

		allMenus.clear();
		menusHierarchyBeanList.clear();

		session = Sessions.getCurrent();

		if (session.getAttribute("login") != null) {

			name = "User: " + session.getAttribute("login");

			roleId = (Integer) session.getAttribute("userRoleId");

			connection = (Connection) session.getAttribute("sessionConnection");
			
			userId= (Integer)session.getAttribute("userId");
			
			getParentMenuHierarchy();
			
			doCollapseExpandAll(mytree, true);

		} else {

			//Executions.sendRedirect("../index.zul");
			Executions.sendRedirect("index.zul");
		}

	}
	
	public void getParentMenuHierarchy() {

		try {

			System.out.println("AT - ALL MENU SIZE: "+ allMenus.size());
			System.out.println("AT - MENU HIERARCHY: "+ menusHierarchyBeanList.size());

			int count = 0;
			
			sql1: {

				PreparedStatement preparedStatementSQL1 = null;

				try {

					preparedStatementSQL1 = connection.prepareStatement("SELECT * FROM vw_master_page_menus_user_privs_details "
							+ "where parent_menus_id = 0 and user_id = ? and role_id = ?");
					
					preparedStatementSQL1.setInt(1, userId);
					preparedStatementSQL1.setInt(2, roleId);
					
					System.out.println("----------\n"+preparedStatementSQL1);

					ResultSet resultSetSQL1 = preparedStatementSQL1.executeQuery();

					while (resultSetSQL1.next()) {

						MenusHierarchyBean hierarchyBean = new MenusHierarchyBean();

						hierarchyBean.menusId = resultSetSQL1.getInt("menus_id");

						hierarchyBean.parentMenusId = resultSetSQL1.getInt("parent_menus_id");

						hierarchyBean.menusName = resultSetSQL1.getString("menus_name");

						hierarchyBean.level = resultSetSQL1.getInt("level");

						hierarchyBean.path = resultSetSQL1.getString("path");
						
						hierarchyBean.addAccess = resultSetSQL1.getString("add_access");
						
						hierarchyBean.viewAccess = resultSetSQL1.getString("view_access");
						
						hierarchyBean.editAccess = resultSetSQL1.getString("edit_access");
						
						hierarchyBean.deleteAccess = resultSetSQL1.getString("delete_access");

						menusHierarchyBeanList.add(hierarchyBean);
						
						count++;
					}

				} catch (Exception e) {

					Messagebox.show("Error Due To: " + e.getMessage(), "Error",
							Messagebox.OK, Messagebox.ERROR);

					e.printStackTrace();

				} finally {

					if (preparedStatementSQL1 != null) {
						preparedStatementSQL1.close();
					}
				}

			}
			
			System.out.println("AFTER ENTRY MENU SIZE: "+ allMenus.size());
			System.out.println("AFTER ENTRY MENU HIERARCHY: "+ menusHierarchyBeanList.size());

			
			if(count == 0){
				
				System.out.println("--COUNT: "+0);
				
				Messagebox.show("Access denied. Please Contact System Administrator.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Command
	@NotifyChange("*")
	public void onClickTreeCell(@BindingParam("bean") MenuItem item){
		
		System.out.println("CHECK: "+ item.getPageLink());
		
		session.setAttribute("parentMenusItem", item);
		
		include.setSrc(item.getPageLink());
	}
	
	
	public TreeModel getModel() {
		if (_model == null) {
			
			
			MyTreeModel a = new MyTreeModel(getRoot());
			a.setMultiple(true);
			_model = a;
		}
		return _model;
	}

	public MenuItem getRoot() {
		
		MenuItem root = new MenuItem("Menu", 0, "", 1, "Y", "Y", "Y", "Y");
		
		int size = 0;
		
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
		
		for(MenusHierarchyBean bean: menusHierarchyBeanList){
			
			if(bean.parentMenusId == 0 && bean.menusId != 0){
		
				allMenus = MenuItemData.getAllMenus(bean, connection, userId, roleId, menus);
				
				MenuItem m1 = allMenus.get(size);
				
				size++;
				root.addChild(m1);
				
				//menus = new ArrayList<MenuItem>();
				
			}
		}
		
		System.out.println(size);
		return root;
	}

	public static void doCollapseExpandAll(Component component,
			boolean aufklappen) {

		if (component instanceof Treeitem) {
			Treeitem treeitem = (Treeitem) component;
			treeitem.setOpen(false);
			//treeitem.getChildren().clear();
		}

		Collection<?> com = component.getChildren();

		if (com != null) {
			for (Iterator<?> iterator = com.iterator(); iterator.hasNext();) {
				doCollapseExpandAll((Component) iterator.next(), aufklappen);

			}
		}
	}
	
	
	public static void clearRowTree(Component component) {

		if (component instanceof Treeitem) {
			Treeitem treeitem = (Treeitem) component;
			treeitem.getChildren().clear();
		}

		Collection<?> com = component.getChildren();

		if (com != null) {
			for (Iterator<?> iterator = com.iterator(); iterator.hasNext();) {
				clearRowTree((Component) iterator.next());

			}
		}
	}

	@Command
	@NotifyChange("*")
	public void onClickSignOut() throws SQLException {

		if (session != null) {

			session.removeAttribute("login");

			allMenus.clear();
			menusHierarchyBeanList.clear();
			clearRowTree(mytree);

			Connection connection = (Connection) session.getAttribute("sessionConnection");

			connection.close();
			
			System.out.println("AT LOGOUT- ALL MENU SIZE: "+ allMenus.size());
			System.out.println("AT LOGOUT- MENU HIERARCHY: "+ menusHierarchyBeanList.size());
			
			session.removeAttribute("sessionConnection");

			Executions.sendRedirect("../index.zul");
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Tree getMytree() {
		return mytree;
	}

	public void setMytree(Tree mytree) {
		this.mytree = mytree;
	}

	public Include getInclude() {
		return include;
	}

	public void setInclude(Include include) {
		this.include = include;
	}

	public TreeModel get_model() {
		return _model;
	}

	public void set_model(TreeModel _model) {
		this._model = _model;
	}

	public List<MenuItem> getAllMenus() {
		return allMenus;
	}

	public void setAllMenus(List<MenuItem> allMenus) {
		this.allMenus = allMenus;
	}

	public ArrayList<MenusHierarchyBean> getMenusHierarchyBeanList() {
		return menusHierarchyBeanList;
	}

	public void setMenusHierarchyBeanList(
			ArrayList<MenusHierarchyBean> menusHierarchyBeanList) {
		this.menusHierarchyBeanList = menusHierarchyBeanList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
