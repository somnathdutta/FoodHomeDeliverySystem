package sql;

public class BannerSql {
	
	public static final String insertBannerIdQuery = "INSERT INTO fapp_banner (banner_title) VALUES (?) ";
	
	public static final String selectMaxBannerIdQuery = "select max(banner_id) as m_id from fapp_banner";
	
	public static final String insertBannerListQuery = "insert into fapp_banner_details (banner_id, banner_image, image_name) values(?,?,?)";
	
	public static final String loadBannerNameQuery = "select banner_id, banner_title from fapp_banner order by banner_id";
	
	public static final String loadBannerUrlListQuery = "select fbd.banner_details_id, fbd.banner_image, fb.banner_id, fb.banner_title, fbd.is_active, fbd.image_name from fapp_banner fb, "
														 + "fapp_banner_details fbd where fb.banner_id = fbd.banner_id and fb.banner_id = ? order by fbd.banner_details_id";
	
	public static final String updateBannerUrl = "update fapp_banner_details set banner_image = ?, image_name = ? where banner_details_id = ? ";
	
	public static final String inActiveBannerUrl = "update fapp_banner_details set is_active = ? where banner_details_id = ? ";
	

}
