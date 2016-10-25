package sql;

public class CmsMasterSql {
	
	/******************************************************* Share And Earn **********************************************************/
	
	public static final String inSertShareAndEarnDetailsSql = "insert into fapp_share_and_earn (app_msg, inviting_text, amount, created_by, updated_by, img_url ) values(?,?,?,?,?,?) ";
	
	public static final String loadShareAndEarnDetailsSql = "select fapp_share_and_earn_id, app_msg, inviting_text, amount, img_url from fapp_share_and_earn where is_delete = 'N' ";
	
	public static final String updateShareAndEarnDetailsSql = "update fapp_share_and_earn set app_msg = ?, inviting_text = ? , amount =?, updated_by = ?, img_url = ? where fapp_share_and_earn_id = ? ";
	
	public static final String deleteShareAndEarnDetailsSql = "update fapp_share_and_earn set is_delete = 'Y', updated_by = ? where fapp_share_and_earn_id = ? ";
	
	
	
	/******************************************************* Contact Us **************************************************************/
	
	public static final String insertContactUsSql = "insert into fapp_contact_us (contct_no, created_by, updated_by) values (?, ?, ?)";
	
	public static final String loadContactUsSql = "select fapp_contact_us_id, contct_no from fapp_contact_us where is_delete = 'N' ";
	
	public static final String updateContactUsSql = "update fapp_contact_us set contct_no = ?, updated_by = ? where fapp_contact_us_id = ? ";
	
	public static final String deleteContactUsSql = "update fapp_contact_us set is_delete = 'Y', updated_by = ? where fapp_contact_us_id = ? ";
	
	
	
	/******************************************************** About Us ***********************************************************/
	
	public static final String insertAboutUsSql = "insert into fapp_about_us (about_us, created_by, updated_by) values (?, ? , ?)";
	
	public static final String loadAboutUsSql = "select fapp_about_us_id, about_us from fapp_about_us where is_delete = 'N' ";
	
	public static final String updateAboutUsSql = "update fapp_about_us set about_us = ?, updated_by = ? where fapp_about_us_id= ? ";
	
	public static final String deleteAboutUsSql= "update fapp_about_us set is_delete = 'Y', updated_by = ? where fapp_about_us_id = ? ";
	
	
	/********************************************************* Terms and condition ***********************************************/
	
	public static final String inSertTermsAndC = "insert into fapp_terms_and_condition (terms_and_condition, created_by, updated_by) values (?, ? , ?)";
	
	public static final String loadTermsAndCSql = "select fapp_terms_and_condition_id, terms_and_condition from fapp_terms_and_condition where is_delete = 'N' ";
	
	public static final String updateTermsAndCSql = "update fapp_terms_and_condition set terms_and_condition = ?, updated_by = ? where fapp_terms_and_condition_id = ? ";
	
	public static final String deleteTermsAndCSql= "update fapp_terms_and_condition set is_delete = 'Y', updated_by = ? where fapp_terms_and_condition_id = ? ";
	
	
	
	/******************************************************* Privacy Policy ******************************************************/
	
	public static final String insertPrivacyPolicySql = "insert into fapp_privacy_policy (privacy_policy, created_by, updated_by) values (?, ? , ?)";
	
	public static final String loadPrivacyPolicySql = "select fapp_privacy_policy_id, privacy_policy from fapp_privacy_policy where is_delete = 'N' ";
	
	public static final String updatePrivacyPolicySql = "update fapp_privacy_policy set privacy_policy = ?, updated_by = ? where fapp_privacy_policy_id = ? ";
	
	public static final String deletePrivacyPolicySql = "update fapp_privacy_policy set is_delete = 'Y', updated_by = ? where fapp_privacy_policy_id = ? ";
	
	
}
