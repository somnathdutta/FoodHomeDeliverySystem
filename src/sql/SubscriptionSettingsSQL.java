package sql;

public class SubscriptionSettingsSQL {

	public static String insertPackQuery = "INSERT INTO fapp_subscription_packs "
								           +" (pack_type_id, flavour_type_id,pack_price) "
								           +" VALUES (?, ?, ?)";
	
	public static String insertPackDetailsQuery = "INSERT INTO fapp_subscription_pack_details "
	           +" (item_code,item_price,meal_type_id,subscription_pack_id,day_id,item_qty) "
	           +" VALUES (?, ?, ?, ?, ?, ? )";
	
	public static String getMaxPackIdQuery = "SELECT MAX(subscription_pack_id)AS id from fapp_subscription_packs";
	
	public static String existingPackQuery = "SELECT * from vw_fapp_saved_packs";
}
