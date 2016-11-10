package Bean;

import java.sql.Date;
import java.util.ArrayList;

public class PromoCodeMasterBean {

	private Integer promoCodeId;
	private Integer promocodeDetailsId;
	private String promoCode;
	
	private Date fromDateSql;
	private java.util.Date fromDateUtil;
	private String fromDateStr;
	
	private Date toDateSql;
	private java.util.Date toDateUtil;
	private String toDateStr;
	
	private Integer promoTypeId;
	private String promoType;
	
	private Integer promocodeApplyTypeId;
	private String promoCodeApplyType;
	
	private String status;
	private String promoCodeApplied;
	
	private boolean promoApplyDivVis;
	
	private Double promoValue;
	private boolean promoVauleVis;
	
	private String user;
	
	private Integer volumeQuantity;
	private boolean volumeQuantityDis;
	
	
	private PromoCodeTypeBean promoTypeBean = new PromoCodeTypeBean();
	private PromoCodeTypeBean promoApplyBean = new PromoCodeTypeBean();
	
	private ArrayList<PromoCodeTypeBean> promoTypeBeanList;
	private ArrayList<PromoCodeTypeBean> applyBeanList;
	
	public Integer getPromoCodeId() {
		return promoCodeId;
	}
	public void setPromoCodeId(Integer promoCodeId) {
		this.promoCodeId = promoCodeId;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public Date getFromDateSql() {
		return fromDateSql;
	}
	public void setFromDateSql(Date fromDateSql) {
		this.fromDateSql = fromDateSql;
	}
	public java.util.Date getFromDateUtil() {
		return fromDateUtil;
	}
	public void setFromDateUtil(java.util.Date fromDateUtil) {
		this.fromDateUtil = fromDateUtil;
	}
	public String getFromDateStr() {
		return fromDateStr;
	}
	public void setFromDateStr(String fromDateStr) {
		this.fromDateStr = fromDateStr;
	}
	public Date getToDateSql() {
		return toDateSql;
	}
	public void setToDateSql(Date toDateSql) {
		this.toDateSql = toDateSql;
	}
	public java.util.Date getToDateUtil() {
		return toDateUtil;
	}
	public void setToDateUtil(java.util.Date toDateUtil) {
		this.toDateUtil = toDateUtil;
	}
	public String getToDateStr() {
		return toDateStr;
	}
	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}
	public Integer getPromoTypeId() {
		return promoTypeId;
	}
	public void setPromoTypeId(Integer promoTypeId) {
		this.promoTypeId = promoTypeId;
	}
	public String getPromoType() {
		return promoType;
	}
	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}
	public Integer getPromocodeApplyTypeId() {
		return promocodeApplyTypeId;
	}
	public void setPromocodeApplyTypeId(Integer promocodeApplyTypeId) {
		this.promocodeApplyTypeId = promocodeApplyTypeId;
	}
	public String getPromoCodeApplyType() {
		return promoCodeApplyType;
	}
	public void setPromoCodeApplyType(String promoCodeApplyType) {
		this.promoCodeApplyType = promoCodeApplyType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPromoCodeApplied() {
		return promoCodeApplied;
	}
	public void setPromoCodeApplied(String promoCodeApplied) {
		this.promoCodeApplied = promoCodeApplied;
	}
	
	public Integer getPromocodeDetailsId() {
		return promocodeDetailsId;
	}
	public void setPromocodeDetailsId(Integer promocodeDetailsId) {
		this.promocodeDetailsId = promocodeDetailsId;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public boolean isPromoApplyDivVis() {
		return promoApplyDivVis;
	}
	public void setPromoApplyDivVis(boolean promoApplyDivVis) {
		this.promoApplyDivVis = promoApplyDivVis;
	}
	
	public boolean isPromoVauleVis() {
		return promoVauleVis;
	}
	public void setPromoVauleVis(boolean promoVauleVis) {
		this.promoVauleVis = promoVauleVis;
	}
	public Double getPromoValue() {
		return promoValue;
	}
	public void setPromoValue(Double promoValue) {
		this.promoValue = promoValue;
	}
	public PromoCodeTypeBean getPromoTypeBean() {
		return promoTypeBean;
	}
	public void setPromoTypeBean(PromoCodeTypeBean promoTypeBean) {
		this.promoTypeBean = promoTypeBean;
	}
	public PromoCodeTypeBean getPromoApplyBean() {
		return promoApplyBean;
	}
	public void setPromoApplyBean(PromoCodeTypeBean promoApplyBean) {
		this.promoApplyBean = promoApplyBean;
	}
	public ArrayList<PromoCodeTypeBean> getPromoTypeBeanList() {
		return promoTypeBeanList;
	}
	public void setPromoTypeBeanList(ArrayList<PromoCodeTypeBean> promoTypeBeanList) {
		this.promoTypeBeanList = promoTypeBeanList;
	}
	public ArrayList<PromoCodeTypeBean> getApplyBeanList() {
		return applyBeanList;
	}
	public void setApplyBeanList(ArrayList<PromoCodeTypeBean> applyBeanList) {
		this.applyBeanList = applyBeanList;
	}
	public Integer getVolumeQuantity() {
		return volumeQuantity;
	}
	public void setVolumeQuantity(Integer volumeQuantity) {
		this.volumeQuantity = volumeQuantity;
	}
	public boolean isVolumeQuantityDis() {
		return volumeQuantityDis;
	}
	public void setVolumeQuantityDis(boolean volumeQuantityDis) {
		this.volumeQuantityDis = volumeQuantityDis;
	}
	
	
}
