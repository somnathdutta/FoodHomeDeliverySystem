package sql;

public class PromoCodeMasterSql {

	public static final String inSertPromoCodeSql = "INSERT INTO fapp_promo_code_master(promo_code, created_by, updated_by) VALUES (?, ?, ?)";
	
	public static final String loadPromoCodeDetailsSql = "select fapp_promo_code_details_id, promo_code_id, promo_code, from_date, to_date, promo_type_id, promo_code_type,"
														  + "promo_code_application_type_id , promo_code_application_type, promo_code_is_active, promo_value,volume_quantity,is_reusable,description from vw_promo_code_details ";
	
	public static final String loadPromocodeTypeSql = "select fapp_promo_code_type_master_id, promo_code_type from fapp_promo_code_type_master ";
	
	public static final String loadPromocodeApplicationTypeSql = "select fapp_promo_code_application_type_master_id, promo_code_application_type from fapp_promo_code_application_type_master ";
	
	public static final String insertPromoCodeDetailsSql = "INSERT INTO fapp_promo_code_details(promo_code_id, promo_code, from_date,to_date, promo_type_id, promo_code_application_type_id,created_by, updated_by, is_active, promo_value) "
														   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	public static final String loadPromoCodeList = "select fapp_promo_code_master_id, promo_code from fapp_promo_code_master where is_applied = 'N' ";
	
	public static final String upDatePromoCodeDetailsSql = "update fapp_promo_code_details set description = ?, is_reusable = ?, from_date = ?, to_date = ?, promo_type_id = ?, promo_code_application_type_id= ?, "
															+ " updated_by = ?, is_active = ?, promo_value = ?, volume_quantity = ? where fapp_promo_code_details_id = ?  ";
	
	
	
	
	public static final String updatePromoCodeSql = "update fapp_promo_code_master set is_applied = 'Y' where fapp_promo_code_master_id = ? "; 
	
	
	
}
