package Bean;

import java.util.ArrayList;
import java.util.Date;

public class AdminSettingsBean {

	public Date bookingFromTime;
	
	public Date bookingToTime;
	
	public Date lunchFromTimings,lunchToTimings;
	
	public Date dinnerFromTimings,dinnerToTimings;
	
	public Date ebLunchFromTimings,ebLunchToTimings;
	
	public Date ebDinnerFromTimings,ebDinnerToTimings;
	
	public Date normalItemFromSettings,normalItemToSettings;
	
	public Date specialItemFromSettings,specialItemToSettings;
	
	public String lunchFromTimingsValue,lunchToTimingsValue;
	
	public String dinnerFromTimingsValue,dinnerToTimingsValue;
	
	public String ebLunchFromTimingsValue,ebLunchToTimingsValue;
	
	public String ebDinnerFromTimingsValue,ebDinnerToTimingsValue;
	
	public String normalItemFromSettingsValue,normalItemToSettingsValue;
	
	public String specialItemFromSettingsValue,specialItemToSettingsValue;
	
	public String welcomePromotion;
	
	public String locationBannerPicturePath;
	
	public String homeScreenBannerPicturePath;
	
	public double signUpCredit,orderCredit;

	public Integer lunchDinnerTimingId,specialTimingsId,creditId,ebLunchId,ebDinnerId;
	
	public ManageCategoryBean ebLunchBean = new ManageCategoryBean();
	
	public ManageCategoryBean ebDinnerBean = new ManageCategoryBean();
	
	public ArrayList<ManageCategoryBean> earlyBirdLunchCategoryList = new ArrayList<ManageCategoryBean>();
	
	public ArrayList<ManageCategoryBean> earlyBirdDinnerCategoryList = new ArrayList<ManageCategoryBean>();

	public int timingID;
	
	public String getWelcomePromotion() {
		return welcomePromotion;
	}

	public void setWelcomePromotion(String welcomePromotion) {
		this.welcomePromotion = welcomePromotion;
	}

	public String getLocationBannerPicturePath() {
		return locationBannerPicturePath;
	}

	public void setLocationBannerPicturePath(String locationBannerPicturePath) {
		this.locationBannerPicturePath = locationBannerPicturePath;
	}

	public String getHomeScreenBannerPicturePath() {
		return homeScreenBannerPicturePath;
	}

	public void setHomeScreenBannerPicturePath(String homeScreenBannerPicturePath) {
		this.homeScreenBannerPicturePath = homeScreenBannerPicturePath;
	}

	public Date getBookingFromTime() {
		return bookingFromTime;
	}

	public void setBookingFromTime(Date bookingFromTime) {
		this.bookingFromTime = bookingFromTime;
	}

	public Date getBookingToTime() {
		return bookingToTime;
	}

	public void setBookingToTime(Date bookingToTime) {
		this.bookingToTime = bookingToTime;
	}


	public double getSignUpCredit() {
		return signUpCredit;
	}

	public void setSignUpCredit(double signUpCredit) {
		this.signUpCredit = signUpCredit;
	}

	public double getOrderCredit() {
		return orderCredit;
	}

	public void setOrderCredit(double orderCredit) {
		this.orderCredit = orderCredit;
	}

	public Date getLunchFromTimings() {
		return lunchFromTimings;
	}

	public void setLunchFromTimings(Date lunchFromTimings) {
		this.lunchFromTimings = lunchFromTimings;
	}

	public Date getLunchToTimings() {
		return lunchToTimings;
	}

	public void setLunchToTimings(Date lunchToTimings) {
		this.lunchToTimings = lunchToTimings;
	}

	public Date getDinnerFromTimings() {
		return dinnerFromTimings;
	}

	public void setDinnerFromTimings(Date dinnerFromTimings) {
		this.dinnerFromTimings = dinnerFromTimings;
	}

	public Date getDinnerToTimings() {
		return dinnerToTimings;
	}

	public void setDinnerToTimings(Date dinnerToTimings) {
		this.dinnerToTimings = dinnerToTimings;
	}

	public Date getNormalItemFromSettings() {
		return normalItemFromSettings;
	}

	public void setNormalItemFromSettings(Date normalItemFromSettings) {
		this.normalItemFromSettings = normalItemFromSettings;
	}

	public Date getNormalItemToSettings() {
		return normalItemToSettings;
	}

	public void setNormalItemToSettings(Date normalItemToSettings) {
		this.normalItemToSettings = normalItemToSettings;
	}

	public Date getSpecialItemFromSettings() {
		return specialItemFromSettings;
	}

	public void setSpecialItemFromSettings(Date specialItemFromSettings) {
		this.specialItemFromSettings = specialItemFromSettings;
	}

	public Date getSpecialItemToSettings() {
		return specialItemToSettings;
	}

	public void setSpecialItemToSettings(Date specialItemToSettings) {
		this.specialItemToSettings = specialItemToSettings;
	}

	public String getLunchFromTimingsValue() {
		return lunchFromTimingsValue;
	}

	public void setLunchFromTimingsValue(String lunchFromTimingsValue) {
		this.lunchFromTimingsValue = lunchFromTimingsValue;
	}

	public String getLunchToTimingsValue() {
		return lunchToTimingsValue;
	}

	public void setLunchToTimingsValue(String lunchToTimingsValue) {
		this.lunchToTimingsValue = lunchToTimingsValue;
	}

	public String getDinnerFromTimingsValue() {
		return dinnerFromTimingsValue;
	}

	public void setDinnerFromTimingsValue(String dinnerFromTimingsValue) {
		this.dinnerFromTimingsValue = dinnerFromTimingsValue;
	}

	public String getDinnerToTimingsValue() {
		return dinnerToTimingsValue;
	}

	public void setDinnerToTimingsValue(String dinnerToTimingsValue) {
		this.dinnerToTimingsValue = dinnerToTimingsValue;
	}

	public String getNormalItemFromSettingsValue() {
		return normalItemFromSettingsValue;
	}

	public void setNormalItemFromSettingsValue(String normalItemFromSettingsValue) {
		this.normalItemFromSettingsValue = normalItemFromSettingsValue;
	}

	public String getNormalItemToSettingsValue() {
		return normalItemToSettingsValue;
	}

	public void setNormalItemToSettingsValue(String normalItemToSettingsValue) {
		this.normalItemToSettingsValue = normalItemToSettingsValue;
	}

	public String getSpecialItemFromSettingsValue() {
		return specialItemFromSettingsValue;
	}

	public void setSpecialItemFromSettingsValue(String specialItemFromSettingsValue) {
		this.specialItemFromSettingsValue = specialItemFromSettingsValue;
	}

	public String getSpecialItemToSettingsValue() {
		return specialItemToSettingsValue;
	}

	public void setSpecialItemToSettingsValue(String specialItemToSettingsValue) {
		this.specialItemToSettingsValue = specialItemToSettingsValue;
	}

	public Integer getLunchDinnerTimingId() {
		return lunchDinnerTimingId;
	}

	public void setLunchDinnerTimingId(Integer lunchDinnerTimingId) {
		this.lunchDinnerTimingId = lunchDinnerTimingId;
	}

	public Integer getSpecialTimingsId() {
		return specialTimingsId;
	}

	public void setSpecialTimingsId(Integer specialTimingsId) {
		this.specialTimingsId = specialTimingsId;
	}

	public Integer getCreditId() {
		return creditId;
	}

	public void setCreditId(Integer creditId) {
		this.creditId = creditId;
	}

	public ArrayList<ManageCategoryBean> getEarlyBirdLunchCategoryList() {
		return earlyBirdLunchCategoryList;
	}

	public void setEarlyBirdLunchCategoryList(
			ArrayList<ManageCategoryBean> earlyBirdLunchCategoryList) {
		this.earlyBirdLunchCategoryList = earlyBirdLunchCategoryList;
	}

	public ArrayList<ManageCategoryBean> getEarlyBirdDinnerCategoryList() {
		return earlyBirdDinnerCategoryList;
	}

	public void setEarlyBirdDinnerCategoryList(
			ArrayList<ManageCategoryBean> earlyBirdDinnerCategoryList) {
		this.earlyBirdDinnerCategoryList = earlyBirdDinnerCategoryList;
	}

	public Date getEbLunchFromTimings() {
		return ebLunchFromTimings;
	}

	public void setEbLunchFromTimings(Date ebLunchFromTimings) {
		this.ebLunchFromTimings = ebLunchFromTimings;
	}

	public Date getEbLunchToTimings() {
		return ebLunchToTimings;
	}

	public void setEbLunchToTimings(Date ebLunchToTimings) {
		this.ebLunchToTimings = ebLunchToTimings;
	}

	public Date getEbDinnerFromTimings() {
		return ebDinnerFromTimings;
	}

	public void setEbDinnerFromTimings(Date ebDinnerFromTimings) {
		this.ebDinnerFromTimings = ebDinnerFromTimings;
	}

	public Date getEbDinnerToTimings() {
		return ebDinnerToTimings;
	}

	public void setEbDinnerToTimings(Date ebDinnerToTimings) {
		this.ebDinnerToTimings = ebDinnerToTimings;
	}

	public String getEbLunchFromTimingsValue() {
		return ebLunchFromTimingsValue;
	}

	public void setEbLunchFromTimingsValue(String ebLunchFromTimingsValue) {
		this.ebLunchFromTimingsValue = ebLunchFromTimingsValue;
	}

	public String getEbLunchToTimingsValue() {
		return ebLunchToTimingsValue;
	}

	public void setEbLunchToTimingsValue(String ebLunchToTimingsValue) {
		this.ebLunchToTimingsValue = ebLunchToTimingsValue;
	}

	public String getEbDinnerFromTimingsValue() {
		return ebDinnerFromTimingsValue;
	}

	public void setEbDinnerFromTimingsValue(String ebDinnerFromTimingsValue) {
		this.ebDinnerFromTimingsValue = ebDinnerFromTimingsValue;
	}

	public String getEbDinnerToTimingsValue() {
		return ebDinnerToTimingsValue;
	}

	public void setEbDinnerToTimingsValue(String ebDinnerToTimingsValue) {
		this.ebDinnerToTimingsValue = ebDinnerToTimingsValue;
	}

	public Integer getEbLunchId() {
		return ebLunchId;
	}

	public void setEbLunchId(Integer ebLunchId) {
		this.ebLunchId = ebLunchId;
	}

	public Integer getEbDinnerId() {
		return ebDinnerId;
	}

	public void setEbDinnerId(Integer ebDinnerId) {
		this.ebDinnerId = ebDinnerId;
	}

	public ManageCategoryBean getEbLunchBean() {
		return ebLunchBean;
	}

	public void setEbLunchBean(ManageCategoryBean ebLunchBean) {
		this.ebLunchBean = ebLunchBean;
	}

	public ManageCategoryBean getEbDinnerBean() {
		return ebDinnerBean;
	}

	public void setEbDinnerBean(ManageCategoryBean ebDinnerBean) {
		this.ebDinnerBean = ebDinnerBean;
	}

	public int getTimingID() {
		return timingID;
	}

	public void setTimingID(int timingID) {
		this.timingID = timingID;
	}

	
}
