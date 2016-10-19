package menus;

public class MenusHierarchyBean {

public Integer menusId;
	
	public Integer parentMenusId;
	
	public String menusName;
	
	public Integer level;
	
	public String path;
	
	public String pageLink ;
	
	public String addAccess;
	
	public String viewAccess;
	
	public String deleteAccess;
	
	public String editAccess;
	
	
	
	public String getAddAccess() {
		return addAccess;
	}

	public void setAddAccess(String addAccess) {
		this.addAccess = addAccess;
	}

	public String getViewAccess() {
		return viewAccess;
	}

	public void setViewAccess(String viewAccess) {
		this.viewAccess = viewAccess;
	}

	public String getDeleteAccess() {
		return deleteAccess;
	}

	public void setDeleteAccess(String deleteAccess) {
		this.deleteAccess = deleteAccess;
	}

	public String getEditAccess() {
		return editAccess;
	}

	public void setEditAccess(String editAccess) {
		this.editAccess = editAccess;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	public Integer getMenusId() {
		return menusId;
	}

	public void setMenusId(Integer menusId) {
		this.menusId = menusId;
	}

	public Integer getParentMenusId() {
		return parentMenusId;
	}

	public void setParentMenusId(Integer parentMenusId) {
		this.parentMenusId = parentMenusId;
	}

	public String getMenusName() {
		return menusName;
	}

	public void setMenusName(String menusName) {
		this.menusName = menusName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
