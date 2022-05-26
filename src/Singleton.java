import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;

class Singleton {
    private static Singleton single_instance = null;
 
    public StandardAnalyzer analyzer;
    public Directory index;
    public IndexWriterConfig config;
 
    private Singleton(StandardAnalyzer analyzer, Directory index) throws IOException, ParseException{
    	this.analyzer = analyzer;
		this.index = index;
		this.config = new IndexWriterConfig(analyzer);
    }
 
    public static Singleton initInstance(StandardAnalyzer analyzer, Directory index) throws IOException, ParseException
    {
        if (single_instance == null) {
            single_instance = new Singleton(analyzer, index);
            new Lucene();
        }
 
        return single_instance;
    }

    public static Singleton getInstance()
    {
        return single_instance;
    }
}