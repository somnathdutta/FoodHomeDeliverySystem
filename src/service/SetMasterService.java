package service;

import java.sql.Connection;

import dao.SetDetailsSaveDao;
import Bean.SetBean;

public class SetMasterService {

	public static int insertSetMasterData(Connection connection, String setName, String userName, SetBean setBean){
	  int i = 0;
	  return i = SetDetailsSaveDao.setDetailsSave(connection, setName, userName, setBean);
	}
	
}
