import java.io.*;
import java.util.*;

public class AuxParser {
	
	public static void parseAuxFile(String auxFileName, String outputFileName) throws Exception {
		Scanner reader = new Scanner(new File(auxFileName));
		
		String line = "";
		String branchTable = "";
		String busTable = "";
		String genTable = "";
		String loadTable = "";
		
		boolean doneBranch = false;
		boolean doneBus = false;
		boolean doneGen = false;
		boolean doneLoad = false;
		
		// in this sequence: <busNum, [busCat, genId, genMW, loadId, loadMW]>
		Map<String, String[]> busMap = new TreeMap();
		
		while (reader.hasNextLine()) {
			line = reader.nextLine();
			
			if (!doneBranch && line.contains("DATA (BRANCH")) {
				doneBranch = true;
				while (!reader.nextLine().equals("{"));
				
				line = reader.nextLine();
				while (!line.equals("}")) {
					String[] token = line.replace('\"', ' ').trim().split("\\s+");
					
					branchTable += token[0] + "\t" + token[1] + "\t" + token[2] + "\t" + token[3] + "\t" +
								   token[4] + "\t" + token[5] + "\t" + token[6] + "\t" + token[8] + "\n";
					
					line = reader.nextLine();
				}
			} 
			else if (!doneBus && line.contains("DATA (BUS")) {
				doneBus = true;
				while (!reader.nextLine().equals("{"));
				
				// where is busCat
				line = reader.nextLine();
				while (!line.equals("}")) {
					String[] token = line.replace('\"', ' ').trim().split("\\s+");
					
					String[] tmp;
					if (busMap.containsKey(token[0])) tmp = busMap.get(token[0]);
					else tmp = new String[5];
					
					if (token[9].equals("YES")) 
						tmp[0] = token[0]; // busCat
					else 
						tmp[0] = "null"; // busCat
					
					busMap.put(token[0], tmp);
					
//					busTable += token[0] + "\t";
//					if (token[9].equals("YES")) busTable += token[0] + "\n";
//					else busTable += "?" + "\n";
					
					line = reader.nextLine();
				}
			} 
			else if (!doneGen && line.contains("DATA (GEN")) {
				doneGen = true;
				while (!reader.nextLine().equals("{"));
			
				line = reader.nextLine();
				while (!line.equals("}")) {
					String[] token = line.replace('\"', ' ').trim().split("\\s+");

					String[] tmp;
					if (busMap.containsKey(token[0])) tmp = busMap.get(token[0]);
					else tmp = new String[5];
					tmp[1] = token[1]; // genId;
					tmp[2] = token[3]; // genMW;
					busMap.put(token[0], tmp);
					
//					genTable += token[0] + "\t" + token[1] + "\t" +  token[3] + "\n";
					
					line = reader.nextLine();
				}
			} 
			else if (!doneLoad && line.contains("DATA (LOAD")) {
				doneLoad = true;
				while (!reader.nextLine().equals("{"));
			
				line = reader.nextLine();
				while (!line.equals("}")) {
					String[] token = line.replace('\"', ' ').trim().split("\\s+");

					String[] tmp;
					if (busMap.containsKey(token[0])) tmp = busMap.get(token[0]);
					else tmp = new String[5];
					tmp[3] = token[1]; // loadId;
					tmp[4] = token[3]; // loadMW;
					busMap.put(token[0], tmp);
					
//					loadTable += token[0] + "\t" + token[1] + "\t" +  token[3] + "\n";
					
					line = reader.nextLine();
				}
			}
		}
		
		reader.close();
		
		PrintWriter pw = new PrintWriter(outputFileName);
		
		pw.println("Branch Table");
		pw.print(branchTable);
		
		
		pw.println("Bus Table");
		for (String s: busMap.keySet()) {
			pw.print(s);
			String[] tmp = busMap.get(s);
			for (int i = 0; i < tmp.length; ++i) {
				pw.print("\t" + tmp[i]);
			}
			pw.println();
		}
		
//		pw.println("Bus Table");
//		pw.print(busTable);
//		pw.println("Gen Table");
//		pw.print(genTable);
//		pw.println("Load Table");
//		pw.print(loadTable);
		
		pw.close();
	}
	
	public static void main(String[] args) throws Exception {
		parseAuxFile(args[0], args[1]);
//		parseAuxFile("SimAutoExampleCase12.aux", "parsedAuxFile.txt");
	}
}