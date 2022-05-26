import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.HighFrequencyDictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.eclipse.swt.widgets.Shell;

import services.HistoryService;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JSlider;

public class Frame1 {

	protected Shell shell;
	static JFrame frame;
    static JPanel panel;
	public static JTextField input;
	private static JScrollPane scroll_table;
	private static JScrollPane scroll_list;
	private static boolean customSearch = false;
	private static ScoreDoc[] hits;
	private static IndexSearcher searcher;
	private static int rowsCount = 0;
	private static int nextRowsCount = 0;
	private static AnalyzingSuggester analyzingSuggester;
	
	public static void buildAnalyzingSuggester(Directory autocompleteDirectory, Analyzer autocompleteAnalyzer)
	        throws IOException {
	    DirectoryReader sourceReader = DirectoryReader.open(autocompleteDirectory);
	    HighFrequencyDictionary dict = new HighFrequencyDictionary(sourceReader, "imageUrl", 0);
	    analyzingSuggester = new AnalyzingSuggester(autocompleteDirectory, "suggest",
	            autocompleteAnalyzer);
	    analyzingSuggester.build(dict);
	}

	public static void main(String[] args) throws IOException, ParseException {
		final StandardAnalyzer analyzer = new StandardAnalyzer();
        final Directory index = new ByteBuffersDirectory();
        final HistoryService historyService = new HistoryService();
        Singleton.initInstance(analyzer, index);
        
        buildAnalyzingSuggester(index, analyzer);
        
        
        final JFrame frame = new JFrame();
        frame.getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1264, 922);

        frame.getContentPane().add(panel);
        
        final DefaultTableModel model_table = new DefaultTableModel(){
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        }; 
        JTable table = new JTable(model_table);
        table.setAlignmentY(Component.TOP_ALIGNMENT);
        table.setAlignmentX(Component.LEFT_ALIGNMENT);
        model_table.addColumn("Image");
        model_table.addColumn("Title");
        model_table.addColumn("Year"); 
        model_table.addColumn("Genre");
        model_table.addColumn("Rating"); 
        model_table.addColumn("Duration");
        
        model_table.addColumn("Directors");
        model_table.addColumn("Stars");
        model_table.addColumn("Description");
        table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(new WordWrapCellRenderer());
                        JButton button1 = new JButton("Search");
                        button1.setBounds(861, 135, 85, 23);
                        button1.setActionCommand("Search");
                        
                        JLabel lblNewLabel = new JLabel("gkougkl");
                        lblNewLabel.setBounds(539, 46, 95, 54);
                        lblNewLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 25));
                        
                        
                        scroll_table = new JScrollPane(table);
                        scroll_table.setBounds(10, 242, 1244, 364);
                        final JSlider slider = new JSlider();
                        slider.setBounds(961, 138, 128, 26);
                        final List<JCheckBox> btngroup = new ArrayList<>();
                        
                        final JCheckBox loadImages = new JCheckBox("Load images");
                        loadImages.setBounds(6, 217, 123, 23);
                        loadImages.setSelected(true);
                        
                        JCheckBox titleRadio = new JCheckBox("Title");
                        titleRadio.setBounds(445, 183, 76, 23);
                        titleRadio.setVisible(false);
                        
                        JCheckBox genreRadio = new JCheckBox("Genre");
                        genreRadio.setBounds(523, 183, 85, 23);
                        genreRadio.setVisible(false);
                        
                        JCheckBox yearRadio = new JCheckBox("Year");
                        yearRadio.setBounds(445, 212, 64, 23);
                        yearRadio.setVisible(false);
                        
                        JCheckBox ratingRadio = new JCheckBox("Rating");
                        ratingRadio.setBounds(523, 212, 85, 23);
                        ratingRadio.setVisible(false);
                        
                        JCheckBox durationRadio = new JCheckBox("Duration");
                        durationRadio.setBounds(610, 183, 104, 23);
                        durationRadio.setVisible(false);
                        
                        JCheckBox descriptionRadio = new JCheckBox("Description");
                        descriptionRadio.setBounds(610, 212, 104, 23);
                        descriptionRadio.setVisible(false);
                        
                        JCheckBox directorsRadio = new JCheckBox("Directors");
                        directorsRadio.setBounds(716, 183, 85, 23);
                        directorsRadio.setVisible(false);
                        
                        JCheckBox starsRadio = new JCheckBox("Stars");
                        starsRadio.setBounds(716, 212, 69, 23);
                        starsRadio.setVisible(false);
                        btngroup.add(titleRadio);
                        btngroup.add(genreRadio);
                        btngroup.add(yearRadio);
                        btngroup.add(ratingRadio);
                        btngroup.add(durationRadio);
                        btngroup.add(descriptionRadio);
                        btngroup.add(directorsRadio);
                        btngroup.add(starsRadio);
                       
                        final JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Descending");
                        final DefaultListModel historyList = new DefaultListModel();
                        final JList list = new JList(historyList);
                        
                        String [] items = {"None", "Year", "Rating", "Duration"};
                        final JComboBox comboBox = new JComboBox(items);
                        comboBox.setBounds(978, 66, 85, 26);
                        panel.add(comboBox);
                        
                        final JButton nextButton = new JButton("Next");
                        nextButton.setBounds(1159, 624, 95, 23);
                        final JButton previousButton = new JButton("Previous");
                        previousButton.setBounds(1042, 624, 95, 23);
                        nextButton.setEnabled(false);
                        previousButton.setEnabled(false);
                        final JComboBox searchField = new JComboBox();
                        searchField.setBounds(365, 128, 478, 36);
                        
                        searchField.setEditable(true);
                        
                        final JCheckBox chckbxNewCheckBox = new JCheckBox("Custom Field Search");
                        chckbxNewCheckBox.setBounds(861, 183, 175, 23);
                        chckbxNewCheckBox.addItemListener(new ItemListener() {
                        	  @Override
                        	  public void itemStateChanged(ItemEvent e) {
                        	    if(e.getStateChange() == ItemEvent.SELECTED){
                        	    	customSearch = true;
                        	    	for (AbstractButton button : btngroup) {
                        	    		button.setVisible(true);
                        	    	}
                        	    }else {
                        	    	customSearch = false;
                        	    	for (AbstractButton button : btngroup) {
                        	    		button.setVisible(false);
                        	    	}
                        	    }
                        	  }
                        	 });
                        
                        button1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent arg0) {
                            	nextRowsCount = 0;
                            	rowsCount = 0;
                            	nextButton.setEnabled(false);
                            	previousButton.setEnabled(false);
                            	String querystr = searchField.getSelectedItem().toString();
                            	
                            	WordWrapCellRenderer.setVariable(querystr);
                            	if(!historyService.getHistory().contains(querystr)) {
	                            	historyService.addToHistory(querystr);

	                                historyList.clear();
	                                for(String item : historyService.getHistory()) {
	                                	historyList.addElement(item);
	                                }
                            	}

                            	model_table.setRowCount(0);
                            	try {
                	    	        Query q;
                	    	        if(!customSearch) {
                	    	        	q = new QueryParser("fulltext", analyzer).parse(querystr + "~" + slider.getValue()/100);
                	    	        }else {
                	    	        	List<String> fields = new ArrayList<String>();
                	    	        	for (AbstractButton button : btngroup) {
        	                            	if (button.isSelected()) {
        	                            		
        	                                    fields.add(button.getText().toLowerCase());
        	                                }
                                    	}
                	    	        	q = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer).parse(querystr + "~" + slider.getValue()/100);
                	    	        }
                	    	
                	    	        // 3. search
                	    	        IndexReader reader = DirectoryReader.open(index);
                	    	        searcher = new IndexSearcher(reader);
                	    	        TopDocs docs = null;
                	    	        if(comboBox.getSelectedItem() != "None") { 
                	    	        	Sort sort = new Sort();
                	    	        	
                	    	        	sort.setSort(new SortField(comboBox.getSelectedItem().toString().toLowerCase() +"Sort", SortField.Type.STRING, chckbxNewCheckBox_1.isSelected()));  
                	    	        	docs = searcher.search(q, reader.numDocs(), sort);
                	    	        }else {
                	    	        	docs = searcher.search(q, reader.numDocs());
                	    	        }
                	    	        
                	    	        hits = docs.scoreDocs;
                	    	        nextButton.setEnabled(false);
                                	previousButton.setEnabled(false);
                                	
                                	SpellChecker phraseRecommender = new SpellChecker(index);
                                    phraseRecommender.setAccuracy(0.3f);
                                    IndexWriterConfig config = new IndexWriterConfig(analyzer);
                                    phraseRecommender.indexDictionary(new LuceneDictionary(reader, "titleString"), config, true);
                                    
                                    
                                    String[] suggestions = phraseRecommender.suggestSimilar(historyService.getHistoryAsString(), 5);
                                    searchField.removeAllItems();
                                    for(String sug : suggestions) {
                                    	searchField.addItem(sug);
                                    }
                                    searchField.setSelectedItem(null);
                                    phraseRecommender.close();
                                	
                	    	        // 4. display results
                	    	        if(hits.length <= 10) {
                	    	        	nextRowsCount = hits.length;
                	    	        	nextButton.setEnabled(false);
                	    	        	
                	    	        }else {
                	    	        	nextRowsCount = 10;
                	    	        	nextButton.setEnabled(true);
                	    	        }
                	    	        
                	    	        	for(int i=0;i<nextRowsCount;++i) {
                    	    	            int docId = hits[i].doc;
                    	    	            Document d = searcher.doc(docId);
                    	    	            ImageIcon image = new ImageIcon("emptyMovie.png");
    										if(loadImages.isSelected()) {
    											image = new ImageIcon(new URL(d.get("imageUrl")));
    										}
    										model_table.addRow(new Object[]{image, d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration"), d.get("directors"), d.get("stars"), d.get("description")});
                    	    	        }
                	    	        
                            	} catch (Exception ParseException) {}
                            }
                        });
                        
                        
                        
                        nextButton.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		model_table.setRowCount(0);
                        		if(nextRowsCount+10 < hits.length) {
                        			nextRowsCount += 10;
                        			rowsCount = nextRowsCount - 10;
                        			previousButton.setEnabled(true);
                        		}else {
                        			rowsCount += 10;
                        			nextRowsCount = hits.length;
                        			
                        			nextButton.setEnabled(false);
                        			previousButton.setEnabled(true);
                        		}

                        		for(int i=rowsCount;i<nextRowsCount;++i) {
            	    	            int docId = hits[i].doc;
            	    	            Document d = null;
									try {
										d = searcher.doc(docId);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									try {
										ImageIcon image = new ImageIcon("emptyMovie.png");
										if(loadImages.isSelected()) {
											image = new ImageIcon(new URL(d.get("imageUrl")));
										}
										model_table.addRow(new Object[]{image, d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration"), d.get("directors"), d.get("stars"), d.get("description")});
									} catch (MalformedURLException e1) {
										e1.printStackTrace();
									}
            	    	        }
                        	}
                        });
                        
                        previousButton.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		model_table.setRowCount(0);
                        		if(nextRowsCount % 10 > 0) {
                        			nextRowsCount -= nextRowsCount % 10;
                        		}else if(nextRowsCount >= 10) {
                        			nextRowsCount -= 10;
                        		}
                        		if(rowsCount >= 10) {
                        			rowsCount -= 10;
                        			nextButton.setEnabled(true);
                        		}
                        		if(rowsCount == 0) {
                        			previousButton.setEnabled(false);
                        		}
                        		
                        		for(int i=rowsCount;i<nextRowsCount;++i) {
            	    	            int docId = hits[i].doc;
            	    	            Document d = null;
									try {
										d = searcher.doc(docId);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									try {
										ImageIcon image = new ImageIcon("emptyMovie.png");
										if(loadImages.isSelected()) {
											image = new ImageIcon(new URL(d.get("imageUrl")));
										}
										model_table.addRow(new Object[]{image, d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration"), d.get("directors"), d.get("stars"), d.get("description")});
									} catch (MalformedURLException e1) {
										e1.printStackTrace();
									}
            	    	        }
                        	}
                        });    
                        
                        JLabel lblNewLabel_2 = new JLabel("Search Sensitivity");
                        lblNewLabel_2.setBounds(961, 113, 113, 14);
                        
                        JButton btnNewButton = new JButton("Clear History");
                        btnNewButton.setBounds(203, 183, 123, 23);
                        scroll_list = new JScrollPane(list);
                        scroll_list.setBounds(203, 32, 152, 141);
                        btnNewButton.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		historyService.clearHistory();
                        		historyList.clear();
                        	}
                        });
                        panel.setLayout(null);
                        panel.add(btnNewButton);
                        panel.add(scroll_list);
                        panel.add(titleRadio);
                        panel.add(yearRadio);
                        panel.add(genreRadio);
                        panel.add(ratingRadio);
                        panel.add(durationRadio);
                        panel.add(descriptionRadio);
                        panel.add(directorsRadio);
                        panel.add(starsRadio);
                        panel.add(searchField);
                        panel.add(chckbxNewCheckBox);
                        panel.add(button1);
                        panel.add(slider);
                        panel.add(lblNewLabel_2);
                        panel.add(lblNewLabel);
                        panel.add(scroll_table);
                        panel.add(previousButton);
                        panel.add(nextButton);
                        panel.add(loadImages);
                        
                        JLabel lblNewLabel_1 = new JLabel("Sort by");
                        lblNewLabel_1.setBounds(978, 46, 46, 14);
                        panel.add(lblNewLabel_1);
                        
                        chckbxNewCheckBox_1.setBounds(1069, 68, 97, 23);
                        panel.add(chckbxNewCheckBox_1);
                        table.setDefaultEditor(Object.class, null);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(1280, 768));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
	}
}
