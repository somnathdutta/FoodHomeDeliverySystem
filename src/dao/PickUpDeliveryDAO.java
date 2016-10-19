package dao;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import Bean.OrderResponseBean;

public class PickUpDeliveryDAO {

	public static void generateCSV(ArrayList<OrderResponseBean> orderResponseBeanList,
			boolean pickUp, boolean delivery){
		File f = null;
	      boolean bool = false;
	    if(orderResponseBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "pickDeliveryReport.csv";
		  		System.out.println(reportNamewithPath);
		         //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		f = new File(reportNamewithPath);
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // prints
		         System.out.println("File created: "+bool);
		         if(f.exists())
		         // deletes file from the system
		         f.delete();
		         System.out.println("delete() method is invoked");
		         // delete() is invoked
		         f.createNewFile();
		       
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // print
		         System.out.println("File created: "+bool);
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		            if(pickUp){
		            	w.write("ORDER DATE,FOOD VENDOR,ORDER NO,NOTIFICATION TIME,PICKUP TIME,DELIVERY TIME,DELAY PICKUP\n");
		            	 for(int i=0;i<orderResponseBeanList.size();i++){
				            	w.write(orderResponseBeanList.get(i).orderDateValue
				            			+","+orderResponseBeanList.get(i).kitchenName
				            			+","+orderResponseBeanList.get(i).orderNo
				            			+","+orderResponseBeanList.get(i).notifyTimeValue
				            			+","+orderResponseBeanList.get(i).pickUpTimeValue
				            			+","+orderResponseBeanList.get(i).deliveryTimeValue
				            			+","+orderResponseBeanList.get(i).delayInPickUp
				            			+"\n");
				            }
		            }else{
		            	w.write("ORDER DATE,FOOD VENDOR,ORDER NO,NOTIFICATION TIME,PICKUP TIME,DELIVERY TIME,DELAY DELIVERY\n");
		            	 for(int i=0;i<orderResponseBeanList.size();i++){
				            	w.write(orderResponseBeanList.get(i).orderDateValue
				            			+","+orderResponseBeanList.get(i).kitchenName
				            			+","+orderResponseBeanList.get(i).orderNo
				            			+","+orderResponseBeanList.get(i).notifyTimeValue
				            			+","+orderResponseBeanList.get(i).pickUpTimeValue
				            			+","+orderResponseBeanList.get(i).deliveryTimeValue
				            			+","+orderResponseBeanList.get(i).delayInDelivery
				            			+"\n");
				            }
		            }
		           
		            w.close();
		           // Desktop.getDesktop().open(f);
		            FileInputStream fis = new FileInputStream(new File(reportNamewithPath));
		    		
		    		byte[] ba1 = new byte[1024];

		    		int baLength;

		    		ByteArrayOutputStream bios = new ByteArrayOutputStream();

		    		try {

		    			try {

		    				while ((baLength = fis.read(ba1)) != -1) {

		    					bios.write(ba1, 0, baLength);

		    				}
		    			} catch (Exception e1) {

		    			} finally {

		    				fis.close();

		    			}

		    			final AMedia amedia = new AMedia("pickDeliveryReport", "csv", "application/csv", bios.toByteArray());

		    			Filedownload.save(amedia);
		    		}catch(Exception exception){
		    			
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }
	      
	}
}
