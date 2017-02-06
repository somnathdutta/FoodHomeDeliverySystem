package validator;

import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.ItemTypeBean;

public class ManageKitchenValidator {

	public static boolean applyStockValidation(ArrayList<ItemTypeBean> list){
		boolean flage = false;
		for(ItemTypeBean bean : list){
			if(bean.getLunchStock() == null){
				Messagebox.show("LUNCH CAN'T BE BLANK!!","ALERT:",Messagebox.OK, Messagebox.QUESTION);
				return false;
			}else if(bean.getDinnerStok() == null){
				Messagebox.show("DINNER CAN'T BE BLANK!!","ALERT:",Messagebox.OK, Messagebox.QUESTION);
				return false;
			}else {
				flage = true;
			}
		}
		return flage;
	} // end applyStockValidation()
	
}
