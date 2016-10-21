package service;

import java.sql.Connection;
import java.util.ArrayList;

import dao.BannerDao;
import Bean.BannerBean;
import Bean.UrlBean;

public class BannerService {

	public static int insertBannerDetails(Connection connection, BannerBean bean, ArrayList<UrlBean> urlList){
		int i= 0;
		return i = BannerDao.insertBanner(connection, bean, urlList);
	}
	
}
