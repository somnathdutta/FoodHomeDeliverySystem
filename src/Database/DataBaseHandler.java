package Database;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DataBaseHandler {

	private  Connection connection = null;
	private static DataBaseHandler databaseHandler = null;
	
	private DataBaseHandler(){}
	
	public static DataBaseHandler getInstance() throws Exception {
		if(databaseHandler == null){
			databaseHandler = new DataBaseHandler();
		}
		return databaseHandler;
	}

/*	public Connection getContextConnection()throws Exception{

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
	}*/
	
	public Connection createConnection(){

			try {
				
				Class.forName("org.postgresql.Driver");
				
				connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodhomedelivery", "postgres","password");			
                System.out.println("New db connection ctreated ...."+connection);
	
			} catch (Exception e) {e.printStackTrace();}	

		return connection ;		
		
	}
	
/*	public void closeConnection(){
		try {
			if(connection != null){
				System.out.println("Close db connection ....");
				connection.close();
			}
		} catch (Exception e) {e.printStackTrace();}	
	}*/
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("*********************");
		
		Connection connection$local = DataBaseHandler.getInstance().createConnection();
		
		System.out.println(connection$local);
		
	}
	
}