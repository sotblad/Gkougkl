import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.search.suggest.analyzing.FuzzySuggester;
import org.apache.lucene.search.suggest.tst.TSTLookup;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import model.Movie;
import services.HistoryService;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JSlider;

public class Frame1 {

	protected Shell shell;
	static JFrame frame;
    static JPanel panel;
	public static JTextField input;
	private StandardAnalyzer analyzer = Singleton.getInstance().analyzer;
	private Directory index = Singleton.getInstance().index;
	private static JTable table;
	private static JScrollPane scroll_table;
	private static boolean customSearch = false;
	private static ScoreDoc[] hits;
	private static IndexSearcher searcher;
	private static int rowsCount = 0;
	private static int nextRowsCount = 0;
	private static AnalyzingSuggester analyzingSuggester;
	
	public static void buildAnalyzingSuggester(Directory autocompleteDirectory, Analyzer autocompleteAnalyzer)
	        throws IOException {
	    DirectoryReader sourceReader = DirectoryReader.open(autocompleteDirectory);
	    LuceneDictionary dict = new LuceneDictionary(sourceReader, "fulltext");
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
        
        JPanel panel = new JPanel();

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        
        final DefaultTableModel model_table = new DefaultTableModel(); 
        JTable table = new JTable(model_table); 
        model_table.addColumn("Title");
        model_table.addColumn("Year"); 
        model_table.addColumn("Genre");
        model_table.addColumn("Rating"); 
        model_table.addColumn("Duration");
        model_table.addColumn("Description");
        model_table.addColumn("Directors");
        model_table.addColumn("Stars");
                        JButton button1 = new JButton("Search");
                        button1.setActionCommand("Search");
                        
                        JLabel lblNewLabel = new JLabel("gkougkl");
                        lblNewLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 25));
                        
                        
                        
                        scroll_table = new JScrollPane(table);
                        final JSlider slider = new JSlider();
                        final List<JCheckBox> btngroup = new ArrayList<>();
                        
              
                        
                        JCheckBox titleRadio = new JCheckBox("Title");
                        titleRadio.setVisible(false);
                        
                        JCheckBox genreRadio = new JCheckBox("Genre");
                        genreRadio.setVisible(false);
                        
                        JCheckBox yearRadio = new JCheckBox("Year");
                        yearRadio.setVisible(false);
                        
                        JCheckBox ratingRadio = new JCheckBox("Rating");
                        ratingRadio.setVisible(false);
                        
                        JCheckBox durationRadio = new JCheckBox("Duration");
                        durationRadio.setVisible(false);
                        
                        JCheckBox descriptionRadio = new JCheckBox("Description");
                        descriptionRadio.setVisible(false);
                        
                        JCheckBox directorsRadio = new JCheckBox("Directors");
                        directorsRadio.setVisible(false);
                        
                        JCheckBox starsRadio = new JCheckBox("Stars");
                        starsRadio.setVisible(false);
                        btngroup.add(titleRadio);
                        btngroup.add(genreRadio);
                        btngroup.add(yearRadio);
                        btngroup.add(ratingRadio);
                        btngroup.add(durationRadio);
                        btngroup.add(descriptionRadio);
                        btngroup.add(directorsRadio);
                        btngroup.add(starsRadio);
                        
                        final JButton nextButton = new JButton("Next");
                        final JButton previousButton = new JButton("Previous");
                        nextButton.setEnabled(false);
                        previousButton.setEnabled(false);
                        final JComboBox searchField = new JComboBox();
                        searchField.setEditable(true);
                        
                        final JCheckBox chckbxNewCheckBox = new JCheckBox("Custom Field Search");
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
                            	String querystr = searchField.getSelectedItem().toString();
                            	List<Lookup.LookupResult> lookup = null;
                            	if(!historyService.getHistory().contains(querystr)) {
	                            	historyService.addToHistory(querystr);
	                            	try {
										lookup = analyzingSuggester.lookup(CharBuffer.wrap(querystr), false, 2);
									} catch (IOException e) {
										e.printStackTrace();
									}

	                                for(Lookup.LookupResult suggestion : lookup) {
	                                	searchField.addItem(suggestion.key);
	                                }
	                            	//searchField.addItem(historyService.getHistory().get(historyService.getHistory().size()-1));
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
                	    	        TopDocs docs = searcher.search(q, reader.numDocs());
                	    	        hits = docs.scoreDocs;
                	    	
                	    	        // 4. display results
                	    	        if(hits.length <= 10) {
                	    	        	nextRowsCount = hits.length;
                	    	        }else {
                	    	        	nextRowsCount = 10;
                	    	        	nextButton.setEnabled(true);
                	    	        }
                	    	        
                	    	        	for(int i=0;i<nextRowsCount;++i) {
                    	    	            int docId = hits[i].doc;
                    	    	            Document d = searcher.doc(docId);
                    	    	            model_table.addRow(new Object[]{d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration"), d.get("description"), d.get("directors"), d.get("stars")});
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
                        		}

                        		for(int i=rowsCount;i<nextRowsCount;++i) {
            	    	            int docId = hits[i].doc;
            	    	            Document d = null;
									try {
										d = searcher.doc(docId);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
            	    	            model_table.addRow(new Object[]{d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration"), d.get("description"), d.get("directors"), d.get("stars")});
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
            	    	            model_table.addRow(new Object[]{d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration"), d.get("description"), d.get("directors"), d.get("stars")});
            	    	        }
                        	}
                        });
                    //    nextButton.setEnabled(false);
                        
                        
                        
                        
                        
                        JLabel lblNewLabel_2 = new JLabel("Search Sensitivity");
                        
                        
                        
                        GroupLayout gl_panel = new GroupLayout(panel);
                        gl_panel.setHorizontalGroup(
                        	gl_panel.createParallelGroup(Alignment.TRAILING)
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addGap(441)
                        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                        			.addContainerGap(472, Short.MAX_VALUE))
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addContainerGap(213, Short.MAX_VALUE)
                        			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                        				.addGroup(gl_panel.createSequentialGroup()
                        					.addComponent(titleRadio)
                        					.addGap(0)
                        					.addComponent(yearRadio)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(genreRadio)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(ratingRadio)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(durationRadio)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(descriptionRadio)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(directorsRadio)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(starsRadio)
                        					.addGap(26))
                        				.addGroup(gl_panel.createSequentialGroup()
                        					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)
                        					.addGap(18)))
                        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        				.addComponent(chckbxNewCheckBox)
                        				.addGroup(gl_panel.createSequentialGroup()
                        					.addComponent(button1)
                        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        						.addGroup(gl_panel.createSequentialGroup()
                        							.addPreferredGap(ComponentPlacement.UNRELATED)
                        							.addComponent(slider, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
                        						.addGroup(gl_panel.createSequentialGroup()
                        							.addGap(29)
                        							.addComponent(lblNewLabel_2)))))
                        			.addGap(96))
                        		.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
                        			.addGap(106)
                        			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                        				.addGroup(gl_panel.createSequentialGroup()
                        					.addComponent(previousButton)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(nextButton))
                        				.addComponent(scroll_table, GroupLayout.PREFERRED_SIZE, 877, GroupLayout.PREFERRED_SIZE))
                        			.addContainerGap(25, Short.MAX_VALUE))
                        );
                        gl_panel.setVerticalGroup(
                        	gl_panel.createParallelGroup(Alignment.LEADING)
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addContainerGap()
                        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                        			.addPreferredGap(ComponentPlacement.RELATED)
                        			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                        				.addGroup(gl_panel.createSequentialGroup()
                        					.addComponent(lblNewLabel_2)
                        					.addPreferredGap(ComponentPlacement.RELATED)
                        					.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        				.addGroup(gl_panel.createSequentialGroup()
                        					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                        						.addComponent(searchField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        						.addComponent(button1))
                        					.addGap(19)))
                        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        				.addComponent(yearRadio)
                        				.addComponent(titleRadio)
                        				.addComponent(genreRadio)
                        				.addComponent(ratingRadio)
                        				.addComponent(durationRadio)
                        				.addComponent(descriptionRadio)
                        				.addComponent(directorsRadio)
                        				.addComponent(starsRadio)
                        				.addComponent(chckbxNewCheckBox))
                        			.addGap(93)
                        			.addComponent(scroll_table, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                        			.addGap(18)
                        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        				.addComponent(nextButton)
                        				.addComponent(previousButton))
                        			.addContainerGap(249, Short.MAX_VALUE))
                        );
                        panel.setLayout(gl_panel);
                        table.setAutoCreateRowSorter(true);
                        table.setDefaultEditor(Object.class, null);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
	}
}
