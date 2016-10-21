package sql;

public class BannerSql {
	
	public static final String insertBannerIdQuery = "INSERT INTO fapp_banner (banner_title) VALUES (?) ";
	
	public static final String selectMaxBannerIdQuery = "select max(banner_id) as m_id from fapp_banner";
	
	public static final String insertBannerListQuery = "insert into fapp_banner_details (banner_id, banner_image) values(?,?)";

}
