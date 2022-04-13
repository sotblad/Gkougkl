package controller;

import java.util.ArrayList;

import model.CSV.CSVParser;

public class CSVController {
	
	private CSVParser parser;
	
	public CSVController() {
		this.parser = new CSVParser();
	}
	
	public void parseCSV(String path) throws Exception {
		parser.parseCSV(path);
	}
	
	public ArrayList<String[]> getColumns(){
		return parser.getColumns();
	}
}
