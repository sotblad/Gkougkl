import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;

import controller.CSVController;
import controller.MovieController;
import model.Movie;

public class Lucene {

	private Directory index = Singleton.getInstance().index;
	private IndexWriterConfig config = Singleton.getInstance().config;
	
	public Lucene() throws IOException, ParseException {
		createDocument();
	}

	public void createDocument() throws IOException, ParseException {
		
		CSVController csvcontroller = new CSVController();
		MovieController moviecontroller = new MovieController();
		try {
			csvcontroller.parseCSV("scraped_movies.csv");
			moviecontroller.addMovie(csvcontroller.getColumns());
			ArrayList<Movie> parsedMovies = moviecontroller.getMovies();
			
			IndexWriter w = new IndexWriter(index, config);
			for (Movie movie : parsedMovies){ 
				addDoc(w, movie);
			}
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addDoc(IndexWriter w, Movie movie) throws IOException {
		  Document doc = new Document();
		  doc.add(new TextField("title", movie.getTitle(), Field.Store.YES));
		  doc.add(new TextField("year", movie.getReleaseYear(), Field.Store.YES));
		  for (String genre : movie.getGenre()){ 
			  doc.add(new TextField("genre", genre.trim(), Field.Store.YES));
		  }
		  doc.add(new TextField("rating", movie.getRating(), Field.Store.YES));
		  doc.add(new TextField("duration", movie.getDuration(), Field.Store.YES));
		  doc.add(new TextField("description", movie.getDescription(), Field.Store.YES));
		  for (String director : movie.getDirectors()){ 
			  doc.add(new TextField("directors", director.trim(), Field.Store.YES));
		  }
		  for (String star : movie.getStars()){ 
			  doc.add(new TextField("stars", star.trim(), Field.Store.YES));
		  }
		  w.addDocument(doc);
		}
}
