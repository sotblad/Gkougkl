import java.io.IOException;

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

public class Frame1 {

	protected Shell shell;
	private StandardAnalyzer analyzer = Singleton.getInstance().analyzer;
	private Directory index = Singleton.getInstance().index;
	
	/**
	 * Launch the application.
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new ByteBuffersDirectory();
        Singleton.initInstance(analyzer, index);
        
		try {
			Frame1 window = new Frame1();
			
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void open() throws ParseException, IOException {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	protected void createContents() throws ParseException, IOException {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
//		String querystr = "Taylor";
//
//        // the "title" arg specifies the default field to use
//        // when no field is explicitly specified in the query.
//        Query q = new QueryParser("title", analyzer).parse(querystr);
//
//        // 3. search
//        int hitsPerPage = 10;
//        IndexReader reader = DirectoryReader.open(index);
//        IndexSearcher searcher = new IndexSearcher(reader);
//        TopDocs docs = searcher.search(q, hitsPerPage);
//        ScoreDoc[] hits = docs.scoreDocs;
//
//        // 4. display results
//        System.out.println("Found " + hits.length + " hits.");
//        for(int i=0;i<hits.length;++i) {
//            int docId = hits[i].doc;
//            Document d = searcher.doc(docId);
//            System.out.println((i + 1) + ". " + d.get("stars") + "\t aaaaaaa " + d.get("title"));
//        }
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		btnNewButton.setBounds(31, 39, 75, 25);
		btnNewButton.setText("New Button");

	}
}
