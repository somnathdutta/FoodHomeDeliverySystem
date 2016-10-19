package Bean;

import java.util.ArrayList;

public class Pack {

	public String packType;
	public int packTypeId;
	
	public String foodType;
	public int foodTypeId;
	
	public String cuisineName;
	public String categoryName;
	public double packPrice;
	
	public MealBean meal = new MealBean();
	public Flavour flavour = new Flavour();
	
	public ArrayList<MealBean> mealTypeList = new ArrayList<MealBean>();
	public ArrayList<ManageCuisinBean> cuisineList = new ArrayList<ManageCuisinBean>();
	public ArrayList<ManageCategoryBean> categoryList = new ArrayList<ManageCategoryBean>();
	public ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
	
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public String getCuisineName() {
		return cuisineName;
	}
	public void setCuisineName(String cuisineName) {
		this.cuisineName = cuisineName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public double getPackPrice() {
		return packPrice;
	}
	public void setPackPrice(double packPrice) {
		this.packPrice = packPrice;
	}
	public ArrayList<ManageCuisinBean> getCuisineList() {
		return cuisineList;
	}
	public void setCuisineList(ArrayList<ManageCuisinBean> cuisineList) {
		this.cuisineList = cuisineList;
	}
	public ArrayList<ManageCategoryBean> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(ArrayList<ManageCategoryBean> categoryList) {
		this.categoryList = categoryList;
	}
	public ArrayList<ItemBean> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<ItemBean> itemList) {
		this.itemList = itemList;
	}
	public int getPackTypeId() {
		return packTypeId;
	}
	public void setPackTypeId(int packTypeId) {
		this.packTypeId = packTypeId;
	}
	public String getFoodType() {
		return foodType;
	}
	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}
	public int getFoodTypeId() {
		return foodTypeId;
	}
	public void setFoodTypeId(int foodTypeId) {
		this.foodTypeId = foodTypeId;
	}
	public MealBean getMeal() {
		return meal;
	}
	public void setMeal(MealBean meal) {
		this.meal = meal;
	}
	public Flavour getFlavour() {
		return flavour;
	}
	public void setFlavour(Flavour flavour) {
		this.flavour = flavour;
	}
	public ArrayList<MealBean> getMealTypeList() {
		return mealTypeList;
	}
	public void setMealTypeList(ArrayList<MealBean> mealTypeList) {
		this.mealTypeList = mealTypeList;
	}
	
}
