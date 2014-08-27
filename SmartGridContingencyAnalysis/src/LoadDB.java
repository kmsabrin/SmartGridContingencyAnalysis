import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class LoadDB {

	public static void main(String args[]) {
		Connection c = null;
		Statement stmt = null;

		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:smart_grid.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			
			long time = System.currentTimeMillis();
			
			Scanner reader = new Scanner(new File(args[0]));
//			Scanner reader = new Scanner(new File("parsedAuxFile.txt"));
			// read in the branch table
			reader.nextLine(); // first line containing table name
			while (true) {
				
				String s = reader.next();
				if (s.equals("Bus")) break;
				
				String values[] = new String[8];
				values[0] = s;
				for (int i = 1; i < 8; ++i) {
					values[i] = reader.next();
				}
				
				String sql = "INSERT INTO BRANCH "
						+ "VALUES ( "+time+", 0, " + Integer.parseInt(values[0]) + ", " + Integer.parseInt(values[1]) + ", "   
						+ Integer.parseInt(values[2]) + ", '" + values[3] + "', " 
						+ Double.parseDouble(values[4]) + ", " + Double.parseDouble(values[5]) + ", " 
						+ Double.parseDouble(values[6]) + ", " + Double.parseDouble(values[7]) + ");";
				stmt.executeUpdate(sql);						
			}
			System.out.println("Data loaded to branch table successfully");
			
			// read in the bus table
			reader.next(); // remaining of the first line containing table name
			while (reader.hasNext()) {
				String values[] = new String[6];
				for (int i = 0; i < 6; ++i) {
					values[i] = reader.next();
				}
				
				String sql = "INSERT INTO BUS " + "VALUES ( "+time+", 0, " + Integer.parseInt(values[0]) + ", ";
				if (values[1].equals("null")) sql += "'null'";
				else sql += Integer.parseInt(values[1]); 
						
				for (int i = 2; i <= 4; i += 2) {		
					if (values[i].equals("null")) sql += ", 'null'";
					else sql += ", " + Integer.parseInt(values[i]); 
					
					if (values[i + 1].equals("null")) sql += ", 'null'";
					else sql += ", " + Double.parseDouble(values[i + 1]);
				}
				
				sql += ");";
				stmt.executeUpdate(sql);						
			}
			System.out.println("Data loaded to bus table successfully");
			
			// populate the state table
			String sql = "INSERT INTO STATE VALUES ("+time+", 0, -1, 'null', 'null');";
			stmt.executeUpdate(sql);
			System.out.println("Data loaded to state table successfully");
			
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
}
