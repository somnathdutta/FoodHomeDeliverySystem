package Bean;

import java.util.ArrayList;

public class SetBean {

	private String setName;
	private int setId;
	
	private ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public int getSetId() {
		return setId;
	}

	public void setSetId(int setId) {
		this.setId = setId;
	}

	public ArrayList<ItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<ItemBean> itemList) {
		this.itemList = itemList;
	}
	
}
