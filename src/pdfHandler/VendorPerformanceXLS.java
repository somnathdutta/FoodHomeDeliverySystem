package pdfHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import Bean.VendorMisBean;

public class VendorPerformanceXLS {

	private int CELL_01 = 1;

	private int CELL_02 = 2;

	private int CELL_03 = 3;

	private int CELL_04 = 4;

	private int CELL_05 = 5;

	private int CELL_06 = 6;

	private int CELL_07 = 7;

	private int CELL_08 = 8;

		
	private HSSFWorkbook workbook ;
	private HSSFSheet sheet ;
	private HSSFCellStyle headerStyle,subTotalFigStyle,figureStyle ;
	private HSSFFont headerFont,subTotalFigFont,figureFont;
	private int numRows = 0 ;
	private String filename= "C:/Users/somnathd/Desktop/performance" ;     //"/FoodHomeDeliverySystem/xlsreports/vendors_performance_details";
	private String mExcelFile = filename+".xls" ;
	private static String reportname="Vendor's Performance Details";
	private Date currDate = new java.util.Date();
	private static String date;
	
	
	
	
	public void addExcelHeader(ArrayList<VendorMisBean> vendorMisBeanList){
		workbook = new HSSFWorkbook();	
			System.out.println("My excel--"+mExcelFile);
			
		workbook.createDataFormat();		
		headerStyle = workbook.createCellStyle();
		headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    headerStyle.setFont(headerFont);
	    subTotalFigStyle = workbook.createCellStyle();
	    subTotalFigFont = workbook.createFont();
	    subTotalFigFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    subTotalFigStyle.setFont(subTotalFigFont);
	    subTotalFigStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	    subTotalFigStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    figureStyle = workbook.createCellStyle();
	    figureFont = workbook.createFont();
	    figureStyle.setFont(figureFont);
	    figureStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	    
	    sheet = workbook.createSheet("dispatch_statement_details");
		sheet.setDisplayGridlines(true);
		
		HSSFRow companyRow = createNextHSSFRow();	
			cell_1 :{
				HSSFCell cell0 = companyRow.createCell(CELL_01);
				cell0.setCellValue("EAZE LYF");
				cell0.setCellStyle(headerStyle);
			}
			
			HSSFRow branchHeaderRow = createNextHSSFRow();	
				
			cell_1 :{
				HSSFCell cell0 = branchHeaderRow.createCell(CELL_01);
				cell0.setCellValue("Martin Burn,Lalbazar " );   
				cell0.setCellValue("KOLKATA" );  
				cell0.setCellStyle(headerStyle);
			}
			
			
			HSSFRow reportHeaderRow = createNextHSSFRow();	
		
			cell_1 :{
				HSSFCell cell0 = reportHeaderRow.createCell(CELL_01);
				cell0.setCellValue("REPORT: "+reportname);
				cell0.setCellStyle(headerStyle);
			}
			
			HSSFRow reportPeriodRow = createNextHSSFRow();	
			cell_1 :{
				HSSFCell cell0 = reportPeriodRow.createCell(CELL_01);
				SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
				   try{
					date = sdfr.format( currDate );
				   }catch (Exception ex ){
					System.out.println(ex);
				   }
				cell0.setCellValue("REPORTING DATE : " + date);
				cell0.setCellStyle(headerStyle);
			}
			
			createNextHSSFRow();
			
			createNextHSSFRow();		
			
			HSSFRow currentRow = createNextHSSFRow();		
		
			cell_1 :{
				HSSFCell cell0 = currentRow.createCell(CELL_01);
				cell0.setCellValue("VENDOR NAME");
				cell0.setCellStyle(headerStyle);
			}
			
			cell_2 :{
				HSSFCell cell0 = currentRow.createCell(CELL_02);
				cell0.setCellValue("ORDER NO");
				cell0.setCellStyle(headerStyle);
			}
			cell_3 :{
				HSSFCell cell0 = currentRow.createCell(CELL_03);
				cell0.setCellValue("ORDER ASSIGNMENT TIME");
				cell0.setCellStyle(headerStyle);
			}
			cell_4 :{
				HSSFCell cell0 = currentRow.createCell(CELL_04);
				cell0.setCellValue("ACCEPTANCE TIME");
				cell0.setCellStyle(headerStyle);
			}
			
			cell_5 :{
				HSSFCell cell0 = currentRow.createCell(CELL_05);
				cell0.setCellValue("NOTIFY LOGISTICS TIME");
				cell0.setCellStyle(headerStyle);
			}
			
			cell_6 :{
				HSSFCell cell0 = currentRow.createCell(CELL_06);
				cell0.setCellValue("DELAY IN ACCEPTANCE");
				cell0.setCellStyle(headerStyle);
			}
			
			cell_7 :{
				HSSFCell cell0 = currentRow.createCell(CELL_07);
				cell0.setCellValue("DELAY IN PREPARATION OF FOOD");
				cell0.setCellStyle(headerStyle);
			}
			
			/*cell_8 :{
				HSSFCell cell0 = currentRow.createCell(CELL_08);
				cell0.setCellValue("Quantity");
				cell0.setCellStyle(headerStyle);
			}*/
			
			writeDateValuesInFile(vendorMisBeanList);
			writeExcelFile();
			
		}


	public void writeDateValuesInFile(ArrayList<VendorMisBean> vendorMisBeanList) {
		cell_0:{
			for(VendorMisBean bean: vendorMisBeanList){						
			
			HSSFRow datavlaueRow = createNextHSSFRow();	
			
			cell_1 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_01);
				cell1.setCellValue(bean.kitchenName);  
			}	
			cell_2 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_02);
				cell1.setCellValue(bean.orderNo); 
			}
			cell_3 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_03);
				cell1.setCellValue(bean.orderAssignTimeValue);
			}
			cell_4 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_04);
				cell1.setCellValue(bean.acceptanceTimeValue);  
			}
			
			cell_5 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_05);
				cell1.setCellValue(bean.notifyTimeValue); 
			}
			
			cell_6 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_06);
				cell1.setCellValue(bean.delayInReceive); 
			}
			
			cell_7 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_07);
				cell1.setCellValue(bean.delayInDelivery);
				}
			
			/*cell_8 :{
				HSSFCell cell1 = datavlaueRow.createCell(CELL_08);
				cell1.setCellValue(bean.getQuantity()); 
			}*/
			
			}
		}
	}
	   public HSSFRow createNextHSSFRow(){
			HSSFRow currentRow = sheet.createRow((++numRows));
			return currentRow ;
		}


	   public void writeExcelFile(){
		
		/*FileOutputStream out = null ;
		
		try{
			 out = new FileOutputStream(new File(mExcelFile));
		     workbook.write(out);
			String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
			String xlsNamewithPath = realPath + "Writesheet.xls";
			System.out.println("Path:"+xlsNamewithPath);
		
			 out = new FileOutputStream( 
				      new File(xlsNamewithPath) );
				      workbook.write(out);
				      out.close();
		   
				      System.out.println( 
				      "Writesheet.xlsx written successfully" );
				     
				      PDFViewer.showReport("Writesheet","xls");
				  	Messagebox.show("Xlz file created at "+xlsNamewithPath+" successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
		    }catch(Exception e){
		
			e.printStackTrace();	
			
		}finally{
			
			try {
				out.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}						
		}*/

			FileOutputStream out = null ;
			
			try{
				 out = new FileOutputStream(new File(mExcelFile));
			     workbook.write(out);
			     PDFViewer.showReport(mExcelFile);
			    }catch(Exception e){
			
				e.printStackTrace();	
				
			}finally{
				
				try {
					out.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}						
			}
		
	}
	   
	public void createXLS(String todate,String title) throws Exception {
		reportname=title;
		date=todate;
		//new DispatchStatementDetailsDao().getData(date);
		//new DispatchStatementDetailsXLS().addExcelHeader();
		//PDFViewer.showReport(filename,"xls");
	}

}
