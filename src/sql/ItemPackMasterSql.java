package sql;

public class ItemPackMasterSql {
	
	public static final String insertItemPackQuery = "INSERT INTO fapp_item_pack_type_master "
			+ " (pack_type,is_active,created_by) VALUES (?,?,?) ";
	
	public static final String updateItemPackQuery = "UPDATE fapp_item_pack_type_master "
			+ " set pack_type=?,is_active=?,updated_by=? where item_pack_type_id = ? ";
	
	public static final String loadAllItemPackQuery = "SELECT * from fapp_item_pack_type_master where is_delete='N' ORDER BY item_pack_type_id";
	
}
