package service;

import java.sql.Connection;
import java.util.ArrayList;

import dao.SlotMasterDao;
import dao.TimeSlotDAO;
import Bean.TimeSlot;

public class SlotMasterService {

	public static ArrayList<TimeSlot> loadTimSlot(Connection connection){
		ArrayList<TimeSlot> list = new ArrayList<TimeSlot>();
		list = SlotMasterDao.loadTimeSlot(connection);
		return list;
	}
	
	public static int updatetimeSlot(Connection connection, TimeSlot bean){
		int i = 0;
		i = SlotMasterDao.slotUpdate(connection, bean);
		return i;
	}
	
}
