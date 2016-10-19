package pdfHandler;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class PDFViewer {

	/*public static void showReport(String fileName,String extension) throws Exception{*/
	public static void showReport(String fileName) throws Exception{
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
