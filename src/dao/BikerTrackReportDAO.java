package dao;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import Bean.BikerTrack;

public class BikerTrackReportDAO {

	public static ArrayList<BikerTrack> fetchBikerTrackReport(Connection connection,
			Date startDate, Date endDate){
		ArrayList<BikerTrack> bikerTrackReport = new ArrayList<BikerTrack>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql ="";
							
					try {
						if(startDate== null && endDate==null){
							sql = "select * from vw_biker_track_report ";
							preparedStatement = connection.prepareStatement(sql);
						}else{
							sql = "select * from vw_biker_track_report where delivery_date >= ? and delivery_date <= ? ";
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
						}
						
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							BikerTrack bikerTrack = new BikerTrack();
							bikerTrack.setOrderNo(resultSet.getString("order_no"));
							bikerTrack.setKitchenName(resultSet.getString("kitchen_name"));
							bikerTrack.setBikerName(resultSet.getString("driver_name"));
							bikerTrack.setStartTrip(resultSet.getString("start_trip"));
							String bikerAddress = resultSet.getString("delivery_boy_address");
							String lat = resultSet.getString("latitude");
							String lng = resultSet.getString("longitude");
							
							if(bikerAddress!=null){
								bikerAddress = bikerAddress.replace(",", " ");
								bikerTrack.setBikerAddress(bikerAddress);
							}else{
								bikerTrack.setBikerAddress("NA");
							}
							if(lat!=null){
								bikerTrack.setLatitude(lat);
							}else{
								bikerTrack.setLatitude("NA");
							}
							if(lng!=null){
								bikerTrack.setLongitude(lng);
							}else{
								bikerTrack.setLongitude("NA");
							}
							bikerTrackReport.add(bikerTrack);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bikerTrackReport;
	}
	
	public static void generateExcel(ArrayList<BikerTrack> bikerTrackList){
		 File f = null;
	      boolean bool = false;
	    if(bikerTrackList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "report.csv";
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
		            w.write("ORDER NO,VENDOR NAME,BIKER NAME,TRIP STARTED,FINAL ADDRESS,LATITUDE,LONGITUDE\n");
		            for(int i=0;i<bikerTrackList.size();i++){
		            	w.write(bikerTrackList.get(i).getOrderNo()
		            			+","+bikerTrackList.get(i).getKitchenName()
		            			+","+bikerTrackList.get(i).getBikerName()
		            			+","+bikerTrackList.get(i).getStartTrip()
		            			+","+bikerTrackList.get(i).getBikerAddress()
		            			+","+bikerTrackList.get(i).getLatitude()
		            			+","+bikerTrackList.get(i).getLongitude()
		            			+"\n");
		            			
		            }
		           
		            w.close();
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

		    			final AMedia amedia = new AMedia("bikerTrackReport", "csv", "application/csv", bios.toByteArray());

		    			Filedownload.save(amedia);
		    			openPdf(reportNamewithPath);
		    		}catch(Exception exception){
		    			
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }   
	}
	
	public static void openPdf(String fileName) throws IOException{
		if (Desktop.isDesktopSupported()) {
			try {
		        File myFile = new File(fileName );
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		       ex.printStackTrace();
		    }
		}
	}
}
