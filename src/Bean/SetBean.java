package Bean;

import java.util.ArrayList;

public class SetBean {

	private String setName;
	private Integer setId;
	private boolean setNameVis;
	private Integer dayTypeId;
	private String dayType;
	
	
	
	private ItemBean itemBean = new ItemBean();
	private ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public ArrayList<ItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<ItemBean> itemList) {
		this.itemList = itemList;
	}

	public ItemBean getItemBean() {
		return itemBean;
	}

	public void setItemBean(ItemBean itemBean) {
		this.itemBean = itemBean;
	}

	public boolean isSetNameVis() {
		return setNameVis;
	}

	public void setSetNameVis(boolean setNameVis) {
		this.setNameVis = setNameVis;
	}

	public Integer getDayTypeId() {
		return dayTypeId;
	}

	public void setDayTypeId(Integer dayTypeId) {
		this.dayTypeId = dayTypeId;
	}

	public String getDayType() {
		return dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}
	
}
