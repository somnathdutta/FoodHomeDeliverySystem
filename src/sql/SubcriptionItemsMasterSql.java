package sql;

public class SubcriptionItemsMasterSql {

	//subscription Items
	
	public static final String loadItemsQry = "select items_master_item_id, item_code, item_name, item_image, item_description "
			 								+ " from fapp_subs_items_master where is_active = 'Y' order by items_master_item_id asc ";
	
	public static final String insertItemsQry = "Insert into fapp_subs_items_master (item_name,item_image,created_by,updated_by) values(?,?,?,?) ";
	
	public static final String updateItemsQry = "update fapp_subs_items_master set item_name = ?, item_image = ?, updated_by = ? where items_master_item_id = ? ";
	
	
	//package master
	
	public static final String loadPackageMasterQry = "select package_master_id, package_name, no_of_days, button_name "
													  + " from fapp_subs_package_master where is_active = 'Y' order by package_master_id asc";
	
	public static final String insertPackageMasterQry = "insert into fapp_subs_package_master (package_name,no_of_days,button_name,created_by, updated_by) values(?,?,?,?,?) ";
	
	public static final String updatePackageMasterQry = "update fapp_subs_package_master set package_name = ?, no_of_days = ?, button_name = ?, updated_by = ? where package_master_id= ? "; 
	
	//
	
}
