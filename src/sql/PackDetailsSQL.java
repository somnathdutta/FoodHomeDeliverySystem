package sql;

public class PackDetailsSQL {

	public static String loadExistingPackQuery = "select * from vw_pack_details where subscription_pack_id = ?";
	
	public static String loadExistingItemQuery = "select * from vw_food_item_details";

	public static String removeItemQuery = "DELETE FROM fapp_subscription_pack_details "
											+" WHERE subscription_pack_details_id =?;";
	
	public static String updateItemQuery = "UPDATE fapp_subscription_pack_details " 
										   +" SET item_price=?, meal_type_id=?, "
										   +" day_id=?, item_qty=? "
										   +" WHERE subscription_pack_details_id=?;";
	
	public static String totalPriceQuery = " select sum(item_price) as total_price from fapp_subscription_pack_details "
										   +" where subscription_pack_id = ? ";
	
	public static String updateFinalPriceQuery = " UPDATE fapp_subscription_packs "
											   +" SET pack_price = ? "
											   +" WHERE subscription_pack_id = ?; ";

}
