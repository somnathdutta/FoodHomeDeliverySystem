package Bean;

import java.util.ArrayList;

import org.zkoss.image.AImage;

public class ItemBean implements Comparable<ItemBean> {

	public String itemName,itemCode,itemDescription,cusineName,categoryName,status,dayName,itemTypeName,setName,packingName;
	public int itemId,cuisineId,categoryId,qty,dayId,packId,packDetailsId,lunchStock,tommorrowLunchStock,
	dinnerStock,tomorrowDinnerStock,itemTypeId,setId,packingId;
	
	public Double itemPrice;
	public AImage itemImage;
	public String itemmagePath;
	
	public boolean isChecked=false;
	
	public MealBean meal = new MealBean();
	public ArrayList<MealBean> mealTypeList = new ArrayList<MealBean>();
	
	public DayBean day = new DayBean();
	public ArrayList<DayBean> dayList = new ArrayList<DayBean>();
	
	public Pack pack = new Pack();
	public ArrayList<Pack> packList = new ArrayList<Pack>();
	
	public Flavour flavour = new Flavour();
	public ArrayList<Flavour> flavourList = new ArrayList<Flavour>();
	
	public ItemBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ItemBean(String itemName, String itemCode, String itemDescription,
			String cusineName, String categoryName, String status, int itemId,
			int cuisineId, int categoryId) {
		super();
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.itemDescription = itemDescription;
		this.cusineName = cusineName;
		this.categoryName = categoryName;
		this.status = status;
		this.itemId = itemId;
		this.cuisineId = cuisineId;
		this.categoryId = categoryId;
	}
	
	
	public ItemBean(String itemName, String itemCode, String itemDescription,
			int itemId,Double itemPrice, int itemTypeId) {
		super();
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.itemDescription = itemDescription;
		this.itemId = itemId;
		this.itemPrice = itemPrice;
		this.itemTypeId = itemTypeId;
	}
	
	@Override
	public int compareTo(ItemBean another) {
		// TODO Auto-generated method stub
		return another.dayId- this.dayId  ;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return itemCode+" "+itemName+" "+itemPrice;
	}
	
	public Double getItemPrice() {
		return itemPrice;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getCusineName() {
		return cusineName;
	}
	public void setCusineName(String cusineName) {
		this.cusineName = cusineName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getCuisineId() {
		return cuisineId;
	}
	public void setCuisineId(int cuisineId) {
		this.cuisineId = cuisineId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	
	public AImage getItemImage() {
		return itemImage;
	}
	public void setItemImage(AImage itemImage) {
		this.itemImage = itemImage;
	}
	public String getItemmagePath() {
		return itemmagePath;
	}
	public void setItemmagePath(String itemmagePath) {
		this.itemmagePath = itemmagePath;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public ArrayList<MealBean> getMealTypeList() {
		return mealTypeList;
	}
	public void setMealTypeList(ArrayList<MealBean> mealTypeList) {
		this.mealTypeList = mealTypeList;
	}
	public MealBean getMeal() {
		return meal;
	}
	public void setMeal(MealBean meal) {
		this.meal = meal;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	public int getDayId() {
		return dayId;
	}
	public void setDayId(int dayId) {
		this.dayId = dayId;
	}
	public Pack getPack() {
		return pack;
	}
	public void setPack(Pack pack) {
		this.pack = pack;
	}
	public ArrayList<Pack> getPackList() {
		return packList;
	}
	public void setPackList(ArrayList<Pack> packList) {
		this.packList = packList;
	}
	public Flavour getFlavour() {
		return flavour;
	}
	public void setFlavour(Flavour flavour) {
		this.flavour = flavour;
	}
	public ArrayList<Flavour> getFlavourList() {
		return flavourList;
	}
	public void setFlavourList(ArrayList<Flavour> flavourList) {
		this.flavourList = flavourList;
	}
	public int getPackId() {
		return packId;
	}
	public void setPackId(int packId) {
		this.packId = packId;
	}
	public int getPackDetailsId() {
		return packDetailsId;
	}
	public void setPackDetailsId(int packDetailsId) {
		this.packDetailsId = packDetailsId;
	}
	public DayBean getDay() {
		return day;
	}
	public void setDay(DayBean day) {
		this.day = day;
	}
	public ArrayList<DayBean> getDayList() {
		return dayList;
	}
	public void setDayList(ArrayList<DayBean> dayList) {
		this.dayList = dayList;
	}
	public int getLunchStock() {
		return lunchStock;
	}
	public void setLunchStock(int lunchStock) {
		this.lunchStock = lunchStock;
	}
	public int getTommorrowLunchStock() {
		return tommorrowLunchStock;
	}
	public void setTommorrowLunchStock(int tommorrowLunchStock) {
		this.tommorrowLunchStock = tommorrowLunchStock;
	}
	public int getDinnerStock() {
		return dinnerStock;
	}
	public void setDinnerStock(int dinnerStock) {
		this.dinnerStock = dinnerStock;
	}
	public int getTomorrowDinnerStock() {
		return tomorrowDinnerStock;
	}
	public void setTomorrowDinnerStock(int tomorrowDinnerStock) {
		this.tomorrowDinnerStock = tomorrowDinnerStock;
	}
	public String getItemTypeName() {
		return itemTypeName;
	}
	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	public int getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(int itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
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
	public String getPackingName() {
		return packingName;
	}
	public void setPackingName(String packingName) {
		this.packingName = packingName;
	}
	public int getPackingId() {
		return packingId;
	}
	public void setPackingId(int packingId) {
		this.packingId = packingId;
	}
		
}
