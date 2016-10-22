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
	
	public static ArrayList<UrlBean> loadBannerList(Connection connection){
		ArrayList<UrlBean> list = new ArrayList<UrlBean>();
		list = BannerDao.loadBannerList(connection);
		return list;
	}
	
	public static ArrayList<UrlBean> loadUrlList(Connection connection, int bannerId){
		ArrayList<UrlBean> list = new ArrayList<UrlBean>();
		list = BannerDao.loadUrlList(connection, bannerId);
		return list;
	}
	
	public static int upDateBannerUrl(Connection connection, int bannerDetailsId ){
		int i =0;
		i = BannerDao.bannerEdit(connection, bannerDetailsId);
		return i;
	}
	
	public static int inActiveBannerUrl(Connection connection, int bannerDetailsId, String status ){
		int i =0;
		i = BannerDao.inActivebanner(connection, bannerDetailsId, status);
		return i;
	}
	
	public static boolean saveAll(Connection connection, int bannerId, ArrayList<UrlBean> list){
		boolean status = false;
		status = BannerDao.saveAll(connection, bannerId, list);
		return status;
	}
	
	
	
}
