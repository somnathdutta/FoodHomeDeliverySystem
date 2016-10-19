package pdfHandler;

import java.io.FileOutputStream;
import java.util.ArrayList;

import Bean.OrderInvoiceBean;
import Bean.OrderItemDetailsBean;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfDesignerHandler {
	
	private String fileName;
	
	private Document document = null;
	
	private PdfWriter writer = null;
	
	ParagraphBorder border = new ParagraphBorder();
	
	private static int index = 1;
	private static double total;
	private static String tab = "\t\t\t\t\t\t\t\t";
	
	private static Font catFont = new Font(Font.getFamily("TIMES NEW ROMAN"), 16, Font.BOLD);
	private static Font headfont = new Font(Font.getFamily("TIMES NEW ROMAN"), 13, Font.NORMAL);
	
	
	public void getDetails(String filepath, OrderInvoiceBean orderInvoiceBean, ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList) throws Exception{
		
		fileName = filepath;
		
		Rectangle pagesize = new Rectangle(216f, 720f);
		
		document = new Document(PageSize.A4, 2, 2, 60, 40);
		
		document.setMargins(40,40, 60, 10);

		 document.setMarginMirroring(true);
		 
		writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));

		writer.setBoxSize("art", new com.itextpdf.text.Rectangle(36, 54, 559, 788));

		document.open();
		
		createPDFHeader(orderInvoiceBean, orderItemDetailsBeanList);
		
		printPdf();
		
		document.close();

		}
	
	void createPDFHeader(OrderInvoiceBean bean, ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList) throws Exception {

		Paragraph p = new Paragraph();
		p.add(new Paragraph("EAZE LYF", catFont));
		p.add(new Paragraph("A Pal At Hunger Time", new Font(Font.getFamily("TIMES NEW ROMAN"), 12, Font.BOLD)));
		p.setAlignment(Element.ALIGN_CENTER);
		String line = "--------------------------------------------------------------------------------------------------------------------------------";
		p.add(new Paragraph(line));
		document.add(p);
		String space1 = String.format("%170s", "");
		String spaces = String.format("%150s", "");
		String space2 = String.format("%120s", "");
		String space3 = String.format("%60s", "");
		String text = "Admn.Office :" + spaces
				+ "\t\t\nStreet Address: Ruby" + space2
				+ "\t\t\nDist: North 24 Paragana " + space2
			    + "\t\t\nKolkata,West Bengal, 700 016";

		Paragraph para = new Paragraph(text, new Font(Font.getFamily("HELVETICA"), 8f));
		document.add(para);
		
		addEmptyLine(para, 2);
		Paragraph pa = new Paragraph("INVOICE BILL NOTE NO. :: INV/"+bean.newOrderBean.orderNo, headfont);
		pa.setAlignment(Element.ALIGN_CENTER);
		addEmptyLine(pa, 1);
		document.add(pa);
		
		String contactDetails = "Customer Details :" + space3
				//+String.format("%20s", "")+"Delivery Address:"
				+"\t\t\nCustomer Name : "+bean.orderBy+space3
				//+bean.flatNo+","+bean.streetName
				+"\t\t\nContact Number : "+bean.contactNumber+ space3
				//+bean.landMark+","+bean.location
				+"\t\t\nEmail Id : "+bean.emailId+ space3;
				//+bean.city+","+bean.pincode;
		
		String deliveryDetails = "Delivery Details :" +space3
				+ "\t\t\nStreet Name :"+ bean.streetName
				+ "\t\t\nFlat No : " + bean.flatNo
				+ "\t\t\nLandmark : " + bean.landMark
			    + "\t\t\nLocation :"+ bean.location
				+ "\t\t\nCity :"+ bean.city
				+ "\t\t\nPincode :"+ bean.pincode;
		
		Paragraph details = new Paragraph(contactDetails, new Font(Font.getFamily("VERDANA"), 10f));
		addEmptyLine(details, 1);
		Paragraph deliverydetails = new Paragraph(deliveryDetails , new Font(Font.getFamily("VERDANA"),10f));
		addEmptyLine(deliverydetails, 2);
		document.add(details);
		document.add(deliverydetails);
	
		document.add(createTable(document,orderItemDetailsBeanList));
		
		
		double amount = total;
		String we = "\n\n\n\n\n\n\n\n\n\n\n"+space1+tab+"Total Amount : " + amount;
		int lesstds = (int) Math.round((amount / 10) * 10.0 / 10.0);
		String we2 = space1+tab+"Add Service Tax  : " + (double) lesstds;
		String we3 = space1+tab+"Add VAT : "+total;
		String we4 = space1+tab+"Net Amount : " + (amount - lesstds);
		Paragraph pa8 = new Paragraph(we, new Font(Font.getFamily("HELVETICA"), 8f));
		Paragraph pa9 = new Paragraph(we2, new Font(Font.getFamily("HELVETICA"), 8f));
		Paragraph pa7 = new Paragraph(we3, new Font(Font.getFamily("HELVETICA"), 8f));
		Paragraph pa6 = new Paragraph(we4, new Font(Font.getFamily("HELVETICA"), 8f,Font.BOLD));
		Paragraph pa10 = new Paragraph(line);
		document.add(pa8);
		document.add(pa9);
		document.add(pa7);
		document.add(pa10);
		document.add(pa6);
		document.add(pa10);
		String line1 = "\n\nFrom EAZE LYF \n\n";
		String line2 = "Prepared By : " +bean.kitchenName + space2
				+ "Customer signature\n\n";
		Paragraph pa11 = new Paragraph(line1, new Font(Font.getFamily("HELVETICA"), 8f,Font.BOLD));
		Paragraph pa12 = new Paragraph(line2, new Font(Font.getFamily("HELVETICA"), 8f,Font.BOLD));
		Paragraph pa13 = new Paragraph(line);
		document.add(pa11);
		document.add(pa12);
		document.add(pa13);
		
		
	}
	
	void printPdf() throws DocumentException{
		
		/*PdfPTable dataTable = new PdfPTable(1); 

		PdfPCell cell1 = new PdfPCell(new Paragraph("Delivery Address"));
		
		dataTable.setWidthPercentage(50f);
		
		dataTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		dataTable.setSpacingBefore(10f);

		dataTable.setSpacingAfter(5f);
		
		dataTable.addCell(cell1);
		
		document.add(dataTable);*/
		
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	public static PdfPTable createTable(Document document,ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList)
			throws DocumentException {
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		PdfPCell cell;

		cell = new PdfPCell(new Phrase());
		table.addCell(createLabelCell("Serial no.".trim()));
		table.addCell(createLabelCell("Cuisine Name".trim()));
		table.addCell(createLabelCell("Category Name".trim()));
		table.addCell(createLabelCell("Quantity".trim()));
		table.addCell(createLabelCell("Amount(Rs.)".trim()));
		
		for (OrderItemDetailsBean bean : orderItemDetailsBeanList) {
				table.addCell(createValueCell(String.valueOf(index)));
				table.addCell(createValueCell(bean.cuisineName+ ""));
				table.addCell(createValueCell(bean.categoryName + ""));
				table.addCell(createValueCell(bean.quantity+ ""));
				table.addCell(createValueCell(bean.price + ""));
				total = total + bean.price; 
				index++;
		}
		
		return table;
	}
	
	private static PdfPCell createLabelCell(String text) {
		Font font = new Font(Font.getFamily("HELVETICA"), 8, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		return cell;
	}

	private static PdfPCell createValueCell(String text) {
		Font font = new Font(Font.getFamily("HELVETICA"), 8, Font.NORMAL);
		PdfPCell cell = new PdfPCell(new Phrase(text, font));

		return cell;
	}
}
