package Database;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DataBaseHandler {

	private Connection connection ;
	
	public static DataBaseHandler getInstance() throws Exception {
		
		return new DataBaseHandler();
		
	}

	public Connection getContextConnection()throws Exception{

		try{
			
			Context initCtx = new InitialContext();

	        Context envCtx = (Context)initCtx.lookup("java:/comp/env");

	        DataSource dataSource = (DataSource)envCtx.lookup("jdbc/foodsystem");

	        connection = dataSource.getConnection();

		}catch(Exception e){

			connection = null ;

			throw new Exception("Error in creating a connection");

		}

		return connection ;
	}
	
	
	public Connection getConnection() throws Exception{
		
		Connection localconnection = null;
		
		if(connection == null){
			
			localconnection = getContextConnection();
			
		}else{
			
			localconnection = connection;
		}
		return localconnection;	
	}
	
	public Connection createConnection(){
		
		Connection foodAppConnection = null;  
		
		try {
			
			Class.forName("org.postgresql.Driver");
			
			foodAppConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodhomedelivery", "postgres","password");			
			
			//cbizConnection.setAutoCommit(false);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return foodAppConnection ;		
		
	}
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("*********************");
		
		Connection connection$local = DataBaseHandler.getInstance().createConnection();
		
		
		System.out.println(connection$local);
		
	}
	
}