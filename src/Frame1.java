import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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

public class Frame1 {

	protected Shell shell;
	static JFrame frame;
    static JPanel panel;
	public static JTextField input;
	private StandardAnalyzer analyzer = Singleton.getInstance().analyzer;
	private Directory index = Singleton.getInstance().index;
	private static JTextField searchField;
	private static JTable table;
	private static JScrollPane scroll_table;
	

	public static void main(String[] args) throws IOException, ParseException {
		final StandardAnalyzer analyzer = new StandardAnalyzer();
        final Directory index = new ByteBuffersDirectory();
        Singleton.initInstance(analyzer, index);
        
        final JFrame frame = new JFrame();
        
        JPanel panel = new JPanel();

        frame.getContentPane().add(panel);
        
        final DefaultTableModel model_table = new DefaultTableModel(); 
        JTable table = new JTable(model_table); 
        model_table.addColumn("Title");
        model_table.addColumn("Year"); 
        model_table.addColumn("Genre");
        model_table.addColumn("Rating"); 
        model_table.addColumn("Duration");


        searchField = new JTextField();
        
        searchField.setColumns(10);
                        JButton button1 = new JButton("Search");
                        button1.setActionCommand("Search");
                        button1.addActionListener(new ActionListener() {
                            
                            public void actionPerformed(ActionEvent arg0) {
                            	String querystr = searchField.getText();
                            	model_table.setRowCount(0);
                            	try {
                	    	        Query q = new QueryParser("title", analyzer).parse(querystr);
                	    	
                	    	        // 3. search
                	    	        
                	    	        IndexReader reader = DirectoryReader.open(index);
                	    	        IndexSearcher searcher = new IndexSearcher(reader);
                	    	        TopDocs docs = searcher.search(q, reader.numDocs());
                	    	        ScoreDoc[] hits = docs.scoreDocs;
                	    	
                	    	        // 4. display results
                	    	        for(int i=0;i<hits.length;++i) {
                	    	            int docId = hits[i].doc;
                	    	            Document d = searcher.doc(docId);
                	    	            model_table.addRow(new Object[]{d.get("title"), d.get("year"), d.get("genre"), d.get("rating"), d.get("duration")});
                	    	        }
                            	} catch (Exception ParseException) {}
                            }
                        });
                        
                        JLabel lblNewLabel = new JLabel("gkougkl");
                        lblNewLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 25));
                        
                        
                        
                        scroll_table = new JScrollPane(table);
                        
                        
                        GroupLayout gl_panel = new GroupLayout(panel);
                        gl_panel.setHorizontalGroup(
                        	gl_panel.createParallelGroup(Alignment.TRAILING)
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addContainerGap(241, Short.MAX_VALUE)
                        			.addComponent(searchField, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)
                        			.addGap(28)
                        			.addComponent(button1)
                        			.addGap(196))
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addGap(406)
                        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                        			.addContainerGap(507, Short.MAX_VALUE))
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addContainerGap(194, Short.MAX_VALUE)
                        			.addComponent(scroll_table, GroupLayout.PREFERRED_SIZE, 627, GroupLayout.PREFERRED_SIZE)
                        			.addGap(187))
                        );
                        gl_panel.setVerticalGroup(
                        	gl_panel.createParallelGroup(Alignment.LEADING)
                        		.addGroup(gl_panel.createSequentialGroup()
                        			.addContainerGap()
                        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                        			.addGap(40)
                        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        				.addComponent(searchField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                        				.addComponent(button1))
                        			.addGap(79)
                        			.addComponent(scroll_table, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
                        			.addContainerGap(236, Short.MAX_VALUE))
                        );
                        panel.setLayout(gl_panel);
                        table.setDefaultEditor(Object.class, null);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
	}
}
