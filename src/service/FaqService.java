package service;

import org.zkoss.zul.Messagebox;

import Bean.FaqBean;

public class FaqService {

	public static boolean isValidFields(FaqBean faqBean){
		if(faqBean.getFaqQuestion()!=null){
			if(faqBean.getFaqAnswer()!=null){
				if(faqBean.getStatus()!=null){
					return true;
				}else{
					Messagebox.show("Status required!","Required details",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Answer required!","Required details",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Question required!","Required details",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public static void clear(FaqBean faqBean){
		faqBean.setFaqQuestion(null);
		faqBean.setFaqAnswer(null);
		faqBean.setStatus(null);
	}
}
