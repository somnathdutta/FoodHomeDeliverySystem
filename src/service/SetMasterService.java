package service;

import java.sql.Connection;
import java.util.ArrayList;

import dao.SetDAO;
import dao.SetDetailsSaveDao;
import Bean.SetBean;

public class SetMasterService {

	public static int insertSetMasterData(Connection connection, String setName, String userName, SetBean setBean){
	  int i = 0;
	  return i = SetDetailsSaveDao.setDetailsSave(connection, setName, userName, setBean);
	}
	
	public static ArrayList<SetBean> fetchExistingSetDetais(Connection connection, Integer setId){
		ArrayList<SetBean> list = new ArrayList<SetBean>();
		return list = SetDAO.fetchExistingSetItems(connection, setId);
		
	}
	
	public static ArrayList<SetBean> fetchSetList(Connection connection){
		ArrayList<SetBean> list = new ArrayList<SetBean>();
		return list = SetDAO.fetchSetList(connection);
		
	}
	
	public static ArrayList<SetBean> fetchDayTypeList(Connection connection){
		ArrayList<SetBean> list = new ArrayList<SetBean>();
		return list = SetDAO.fetchDayTypeList(connection);
		
	}
	
	public static int applyDayTypeUpdate(Connection connection, Integer dayTypeId, ArrayList<SetBean> itemCodeList, int setId){
		int i = 0;
		return i = SetDAO.applyDayType(connection, dayTypeId, itemCodeList, setId);
	}
	
}
