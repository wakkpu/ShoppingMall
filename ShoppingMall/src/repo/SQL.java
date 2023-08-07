package repo;

public class SQL {
	public static String consumInsert = 
			"INSERT INTO consumer VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	public static String consumSelect = 
			"SELECT *, membership.grade FROM consumer WHERE useremail = ? JOIN membership ON consumer.membership_id = membership.membership_id";
}
