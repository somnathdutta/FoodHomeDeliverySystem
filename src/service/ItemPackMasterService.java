package service;

import org.zkoss.zhtml.Messagebox;

import Bean.ItemPackMaster;

public class ItemPackMasterService {

	public static boolean isValid(ItemPackMaster itemPackMaster){
		if(itemPackMaster.getItemPackName()!=null){
			if(itemPackMaster.getStatus()!=null){
				return true;
			}else{
				Messagebox.show("Item Pack status required!","Alert Information",Messagebox.OK,Messagebox.EXCLAMATION);
				
				return false;
			}
		}else{
			Messagebox.show("Item Pack name required!","Alert Information",Messagebox.OK,Messagebox.EXCLAMATION);
			
			return false;
		}
	}
}
