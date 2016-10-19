package Bean;

import java.util.Date;

public class FeedBackFoodBean {

	public String kitchenName;
	
	public String orderNo;
	
	public String categoryName;
	
	public Integer quantity;
	
	public Date orderDate ;
	
	public String orderDateValue;
	
	public String negativeFields;
	
	public String rating;

	public int stars;
	
	public double choiceOfMenu,foodTaste,qnty,packaging,timing;
	
	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderDateValue() {
		return orderDateValue;
	}

	public void setOrderDateValue(String orderDateValue) {
		this.orderDateValue = orderDateValue;
	}

	public String getNegativeFields() {
		return negativeFields;
	}

	public void setNegativeFields(String negativeFields) {
		this.negativeFields = negativeFields;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public double getChoiceOfMenu() {
		return choiceOfMenu;
	}

	public void setChoiceOfMenu(double choiceOfMenu) {
		this.choiceOfMenu = choiceOfMenu;
	}

	public double getFoodTaste() {
		return foodTaste;
	}

	public void setFoodTaste(double foodTaste) {
		this.foodTaste = foodTaste;
	}

	public double getQnty() {
		return qnty;
	}

	public void setQnty(double qnty) {
		this.qnty = qnty;
	}

	public double getPackaging() {
		return packaging;
	}

	public void setPackaging(double packaging) {
		this.packaging = packaging;
	}

	public double getTiming() {
		return timing;
	}

	public void setTiming(double timing) {
		this.timing = timing;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}
}
