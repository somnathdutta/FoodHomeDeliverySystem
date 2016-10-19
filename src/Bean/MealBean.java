package Bean;

import java.util.ArrayList;

public class MealBean {

	public String dayName;
	
	public String typeOfMeal;
	
	public int mealTypeId;
	
	public Double price = 0d;

	public OrderItemDetailsBean orderItemDetailsBean = new OrderItemDetailsBean();
	
	public ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList = new ArrayList<OrderItemDetailsBean>();
	
	public ManageCuisinBean cuisinBean = new ManageCuisinBean();
	
	public ArrayList<ManageCuisinBean> cuisineBeanList = new ArrayList<ManageCuisinBean>();

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public String getTypeOfMeal() {
		return typeOfMeal;
	}

	public void setTypeOfMeal(String typeOfMeal) {
		this.typeOfMeal = typeOfMeal;
	}

	public OrderItemDetailsBean getOrderItemDetailsBean() {
		return orderItemDetailsBean;
	}

	public void setOrderItemDetailsBean(OrderItemDetailsBean orderItemDetailsBean) {
		this.orderItemDetailsBean = orderItemDetailsBean;
	}

	public ArrayList<OrderItemDetailsBean> getOrderItemDetailsBeanList() {
		return orderItemDetailsBeanList;
	}

	public void setOrderItemDetailsBeanList(
			ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList) {
		this.orderItemDetailsBeanList = orderItemDetailsBeanList;
	}

	public ManageCuisinBean getCuisinBean() {
		return cuisinBean;
	}

	public void setCuisinBean(ManageCuisinBean cuisinBean) {
		this.cuisinBean = cuisinBean;
	}

	public ArrayList<ManageCuisinBean> getCuisineBeanList() {
		return cuisineBeanList;
	}

	public void setCuisineBeanList(ArrayList<ManageCuisinBean> cuisineBeanList) {
		this.cuisineBeanList = cuisineBeanList;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getMealTypeId() {
		return mealTypeId;
	}

	public void setMealTypeId(int mealTypeId) {
		this.mealTypeId = mealTypeId;
	}
	
	
}
