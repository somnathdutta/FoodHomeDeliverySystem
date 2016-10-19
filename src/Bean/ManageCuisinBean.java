package Bean;

import java.util.ArrayList;

import org.zkoss.image.AImage;

public class ManageCuisinBean {
	
	public String cuisinName;
	
	public Integer cuisinId;
	
	public String status;
	
	public String cuisinePicturePath;
	
	public AImage cuisineImage;
		
	public Boolean chkCuisin=false;
	
	public String categoryItemName;
	
	public Integer categoryItemId;
	
	public Boolean categoryListVisibility = false;

	public ManageCategoryBean categoryBean =new ManageCategoryBean();
	
	public ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
	
	public String getCuisinName() {
		return cuisinName;
	}

	public void setCuisinName(String cuisinName) {
		this.cuisinName = cuisinName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCuisinId() {
		return cuisinId;
	}

	public void setCuisinId(Integer cuisinId) {
		this.cuisinId = cuisinId;
	}

	public Boolean getChkCuisin() {
		return chkCuisin;
	}

	public void setChkCuisin(Boolean chkCuisin) {
		this.chkCuisin = chkCuisin;
	}

	public String getCuisinePicturePath() {
		return cuisinePicturePath;
	}

	public void setCuisinePicturePath(String cuisinePicturePath) {
		this.cuisinePicturePath = cuisinePicturePath;
	}

	public AImage getCuisineImage() {
		return cuisineImage;
	}

	public void setCuisineImage(AImage cuisineImage) {
		this.cuisineImage = cuisineImage;
	}

	public String getCategoryItemName() {
		return categoryItemName;
	}

	public void setCategoryItemName(String categoryItemName) {
		this.categoryItemName = categoryItemName;
	}

	public Integer getCategoryItemId() {
		return categoryItemId;
	}

	public void setCategoryItemId(Integer categoryItemId) {
		this.categoryItemId = categoryItemId;
	}

	public ArrayList<ManageCategoryBean> getCategoryBeanList() {
		return categoryBeanList;
	}

	public void setCategoryBeanList(ArrayList<ManageCategoryBean> categoryBeanList) {
		this.categoryBeanList = categoryBeanList;
	}

	public ManageCategoryBean getCategoryBean() {
		return categoryBean;
	}

	public void setCategoryBean(ManageCategoryBean categoryBean) {
		this.categoryBean = categoryBean;
	}

	public Boolean getCategoryListVisibility() {
		return categoryListVisibility;
	}

	public void setCategoryListVisibility(Boolean categoryListVisibility) {
		this.categoryListVisibility = categoryListVisibility;
	}
	
}
