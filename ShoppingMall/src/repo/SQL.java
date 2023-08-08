package repo;

public class SQL {
	public static String consumInsert = 
			"INSERT INTO consumer VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	public static String consumSelect = 
			"SELECT * FROM consumer WHERE user_email = ?";
	public static String consumUpdate = 
			"UPDATE consumer SET user_name=? , phone_number=?, address=? WHERE user_email=?";
	public static String membershipSelect = 
			"SELECT * FROM consumer JOIN membership ON consumer.membership_id = membership.membership_id WHERE consumer_id = ?";
}
