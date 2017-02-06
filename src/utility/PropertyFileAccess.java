package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileAccess {

	
	
	public String getPropValues(String key , String fileName) throws IOException{
		String sqlQuery = "";
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
			
			if(inputStream != null){
				prop.load(inputStream);
			}else {
				throw new FileNotFoundException("This "+fileName+" properiy file not found in classpath!!");
			}
			
			sqlQuery = prop.getProperty(key);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			inputStream.close();
		}
		return sqlQuery;
	} // end getPropValues()
	
	
	
	
	public static PropertyFileAccess getPropertyObject(){
		
		return new PropertyFileAccess();
	} // end getPropertyObject()
	
}
