package sql;

public class ManageKitchenSql {
	
	public static final String LOADITEMTYPES = "select * from fapp_alacarte_item_type_master where is_active = 'Y'"; 
	
	public static final String UPDATEITEMTYPES  = "update fapp_alacarte_item_type_master set type_name = ?, updated_by = ? where item_type_id = ?";
	
	public static final String INSERTITEMTYPESSQL = "insert into fapp_alacarte_item_type_master (type_name, created_by, updated_by) values(?,?,?)";
	
	public static final String LOADIEMTYPEKITCHENSQL= "select stock_updation_id,kitchen_name,type_name,lunch_stock,dinner_stock from vw_item_stock_updation where kitchen_id = ? ";
	
	public static final String LOADITEMTYPENOTEXISTINKITCHEN = "select aitm.item_type_id, aitm.type_name, aitm.is_active from fapp_alacarte_item_type_master aitm where aitm.is_active = 'Y' and aitm.item_type_id not in(select su.item_type_id from stock_updation su where su.kitchen_id = ?)";
	
	public static final String INSERTITEMKITCHENTYPE = "insert into stock_updation (kitchen_id,item_type_id,lunch_stock,dinner_stock) values(?,?,?,?)";
	
	public static final String UPDATEITEMTYPENOTEXISTINKITCHEN = "update stock_updation set lunch_stock = ?, dinner_stock = ? where stock_updation_id = ? ";
	
	public static final String UPDATESINGLEORDER = "update fapp_kitchen set no_of_single_order_lunch = ? , no_of_single_order_dinner = ? where kitchen_id = ? ";
	
	public static final String LOADSINGLEORDERKITCHEN = "select kitchen_id, kitchen_name, no_of_single_order_lunch, no_of_single_order_dinner from fapp_kitchen where is_active = 'Y' order by kitchen_id ";
}
