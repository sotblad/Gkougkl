package model.CSV;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CSVParser {
	
	private ArrayList<String[]> columns;
	
	public CSVParser() {
		this.columns = new ArrayList<String[]>();
	}
	
	public boolean isEncoded(String text){

	    Charset charset = Charset.forName("US-ASCII");
	    String checked=new String(text.getBytes(charset),charset);
	    return !checked.equals(text);
	}
	
	public void parseCSV(String path) throws Exception {
		Path CSVFile = Paths.get(path);
		int cnt = 1;
		
		try(BufferedReader br = Files.newBufferedReader(CSVFile)){
			
			String line = br.readLine();
			if(cnt == 1)
				line = br.readLine();
			
			while (line != null) {
			
				String[] columns = line.split("\\|");
				if(columns.length != 8) {
					System.out.println(cnt + " " + columns.length);
				}
				
				if (!isEncoded(columns[0])) { // don't add greek named movies
					this.columns.add(columns);
					cnt+=1;
				}
				
				line = br.readLine();
				
			}
		}
	}
	
	public ArrayList<String[]> getColumns(){
		return this.columns;
	}
}
