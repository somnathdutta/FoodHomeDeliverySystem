package sql;

public class InventorySql {

	public static final String loadPackageTypeSql = "select item_pack_type_id, pack_type from fapp_item_pack_type_master where is_active = 'Y' "
													+ "and item_pack_type_id not in(select packing_type_id from fapp_kitchen_inventry where kitchen_id = ?) ";
	
	public static final String loadkitchenPackageSql= "select fki.fapp_kitchen_inventry_id, fki.packing_type_id, fki.kitchen_id, fk.kitchen_name,fiptm.pack_type " + 
													  "	from fapp_kitchen_inventry fki, fapp_item_pack_type_master fiptm, fapp_kitchen fk " +
													  " where fki.packing_type_id = fiptm.item_pack_type_id " +
													  " and fki.kitchen_id = fk.kitchen_id and fki.kitchen_id = ?";
	
	public static final String saveKitchenPackeSql = "insert into fapp_kitchen_inventry (packing_type_id, packing_type, kitchen_id, created_by, updated_by) values (?,?,?,?,?)";
	
	public static final String loadKitchenSql = "select kitchen_id,kitchen_name from vw_kitchens_details where is_delete = 'N'";
	
	
	public static final String loadinventroyDetails = "select fki.stock, sold, fki.fapp_kitchen_inventry_id, fki.packing_type_id, fki.kitchen_id, fk.kitchen_name,fiptm.pack_type " + 
														" from fapp_kitchen_inventry fki, fapp_item_pack_type_master fiptm, fapp_kitchen fk " +
														" where fki.packing_type_id = fiptm.item_pack_type_id " +
														" and fki.kitchen_id = fk.kitchen_id and fki.kitchen_id = ? ";
	
	public static final String updateInventorySql  = "update fapp_kitchen_inventry set stock = ?, updated_by = ? where fapp_kitchen_inventry_id = ? ";
	
	
	
}
