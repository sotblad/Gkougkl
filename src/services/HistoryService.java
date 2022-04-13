package services;

import java.util.ArrayList;

public class HistoryService {
	private ArrayList<String> history;
	
	public HistoryService() {
		this.history = new ArrayList<String>();
	}
	
	public void addToHistory(String query) {
		history.add(query);
	}
	
	public ArrayList<String> getHistory(){
		return history;
	}
	
	public void clearHistory() {
		history = new ArrayList<String>();
	}

}
