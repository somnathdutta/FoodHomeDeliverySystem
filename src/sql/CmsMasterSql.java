package sql;

public class CmsMasterSql {

	public static final String inSertShareAndEarnDetailsSql = "insert into fapp_share_and_earn (app_msg, inviting_text, amount, created_by, updated_by, img_url ) values(?,?,?,?,?,?) ";
	
	public static final String loadShareAndEarnDetailsSql = "select fapp_share_and_earn_id, app_msg, inviting_text, amount, img_url from fapp_share_and_earn where is_delete = 'N' ";
	
	public static final String updateShareAndEarnDetailsSql = "update fapp_share_and_earn set app_msg = ?, inviting_text = ? , amount =?, updated_by = ?, img_url = ? where fapp_share_and_earn_id = ? ";
	
	public static final String deleteShareAndEarnDetailsSql = "update fapp_share_and_earn set is_delete = 'Y', updated_by = ? where fapp_share_and_earn_id = ? ";
}
