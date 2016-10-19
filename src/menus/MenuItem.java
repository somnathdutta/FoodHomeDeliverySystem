package menus;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

	private String name;
	private List<MenuItem> children;
	private int level;
	private Integer menusId;

	private String pageLink = "index.zul";
	
	public String addAccess;
	
	public String viewAccess;
	
	public String deleteAccess;
	
	public String editAccess;
	
	public MenuItem(String name, int level, String pageLink, Integer menusId, String addAccess, 
								String viewAccess, String editAccess, String deleteAccess) {
		
		this.name = name;
		this.level = level;
		this.pageLink = pageLink;
		this.menusId = menusId;
		this.addAccess = addAccess;
		this.viewAccess = viewAccess;
		this.editAccess = editAccess;
		this.deleteAccess = deleteAccess;
				
		
		children = new ArrayList<MenuItem>();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PAGE NAME: "+ name;
	}
	
	
	
	public Integer getMenusId() {
		return menusId;
	}

	public void setMenusId(Integer menusId) {
		this.menusId = menusId;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	public void addChild(MenuItem node) {
		children.add(node);
	}

	public void appendChild(MenuItem child) {
		if (children == null)
			children = new ArrayList<MenuItem>();
		children.add(child);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
