package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import Bean.ItemBean;
import Bean.SetBean;
import sql.SetMasterSql;
import utility.FappPstm;

public class SetDetailsSaveDao {

	public static int setDetailsSave(Connection connection, String setName, String userName, SetBean setBean){
		int i = 0;
		int j = 0;
		PreparedStatement preparedStatement = null;
		try {
			int generatedKey= 0;
			//preparedStatement = connection.prepareStatement(SetMasterSql.insertSetMasterQuery, java.sql.Statement.RETURN_GENERATED_KEYS);
			preparedStatement = connection.prepareStatement(SetMasterSql.insertSetMasterQuery);
			
			preparedStatement.setString(1, setName);
			preparedStatement.setString(2, userName);
			preparedStatement.setString(3, userName);
			System.out.println("prep q " + preparedStatement);
			j = preparedStatement.executeUpdate();
			//ResultSet resultSet = preparedStatement.getGeneratedKeys();
			/*if(resultSet.next()){
				generatedKey = resultSet.getInt(1);
				System.out.println("generated key " + generatedKey);
			}*/
			
			subSql:{
				if(j>0){
				PreparedStatement preparedStatement2 = null;
				try {
					preparedStatement = FappPstm.createQuery(connection, SetMasterSql.selectMaxSetId, null);
					
					
					ResultSet resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						generatedKey = resultSet.getInt(1);
					}
					
					
				} finally {
					if(preparedStatement2 != null){
						preparedStatement2.close();
					}
				}
				}
			}
			
			
			if(generatedKey>0){
				PreparedStatement pstm = null;
				for(ItemBean setbn : setBean.getItemList()){
				
				    pstm = FappPstm.createQuery(connection, SetMasterSql.insertSetItemQuery, Arrays.asList(generatedKey, setbn.getItemCode(), userName, userName ));
				
				    i = pstm.executeUpdate();
				
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return i;
		
		
	}
	
	
	
}
