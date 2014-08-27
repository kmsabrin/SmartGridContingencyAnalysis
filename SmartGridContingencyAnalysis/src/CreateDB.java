import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDB {
	public static void main(String args[]) {
		Connection c = null;
		Statement stmt = null;

		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:smart_grid.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE BUS "
					+ " (TIME_STAMP INT NOT NULL, "
					+ " STATE_ID INT NOT NULL, "
					+ " BUS_NUM INT NOT NULL, "
					+ " BUS_CAT INT, "
					+ " GEN_ID  INT, "
					+ " GEN_MW  REAL, " 
					+ " LOAD_ID INT, "
					+ " LOAD_MW REAL, "
					+ " PRIMARY KEY (TIME_STAMP, STATE_ID, BUS_NUM))";
			stmt.executeUpdate(sql);
			System.out.println("Bus table created successfully");
			
			sql = "CREATE TABLE BRANCH "
					+ " (TIME_STAMP INT NOT NULL, "
					+ " STATE_ID INT NOT NULL, "
					+ " BUS_NUM_1 INT NOT NULL, "
					+ " BUS_NUM_2 INT NOT NULL, "
					+ " LINE_CIRCUIT  INT, "
					+ " LINE_STATUS CHAR(10), "
					+ " LINE_R REAL, "
					+ " LINE_X REAL, "
					+ " LINE_C REAL, "
					+ " LINE_AMVA  REAL, " 
					+ " PRIMARY KEY (TIME_STAMP, STATE_ID, BUS_NUM_1, BUS_NUM_2, LINE_CIRCUIT))";
			stmt.executeUpdate(sql);
			System.out.println("Branch table created successfully");
			
			sql = "CREATE TABLE STATE "
					+ " (TIME_STAMP CHAR(10) NOT NULL, "
					+ " STATE_ID CHAR(10) NOT NULL, "
					+ " PARENT_STATE_ID CHAR(10), "
					+ " CONTINGENCY_ACTION TEXT, "
					+ " CONTROL_ACTION TEXT, "
					+ " PRIMARY KEY (TIME_STAMP, STATE_ID))";
			stmt.executeUpdate(sql);
			System.out.println("State table created successfully");
			
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}
