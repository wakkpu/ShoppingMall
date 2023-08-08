package repo;

public class SQL {
	public static String consumInsert =
			"INSERT INTO consumer(membership_id, user_email, password, phone_number, address, user_name, is_admin) VALUES(?, ?, ?, ?, ?, ?, ?)";
	public static String consumSelect = 
			"SELECT * FROM consumer WHERE user_email = ?";
	public static String consumUpdate = 
			"UPDATE consumer SET user_name=? , phone_number=?, address=? WHERE user_email=?";
	public static String consumDelete = 
			"DELETE FROM consumer WHERE consumer_id=?";
	public static String membershipSelect = 
			"SELECT * FROM consumer JOIN membership ON consumer.membership_id = membership.membership_id WHERE consumer_id = ?";
}
