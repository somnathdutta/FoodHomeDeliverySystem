package sql;

public class PaymentTypeSql {

	public static final String insertPaymentTypeQuery = "INSERT INTO fapp_payment_methods( "
          +"  payment_method, is_active, created_by)    VALUES (?, ?, ?)";
	
	public static final String updatePaymentTypeQuery = "UPDATE fapp_payment_methods"
			+" SET  payment_method=?, is_active=?, updated_by=?  WHERE payment_method_id=?; ";
	
	public static final String loadAllPaymentTypeQuery = "select * from fapp_payment_methods";
}
