package controller;

import java.util.ArrayList;

import model.Movie;

public class MovieController {
	private ArrayList<Movie> movies;
	
	public MovieController() {
		this.movies = new ArrayList<Movie>();
	}
	
	public void addMovie(ArrayList<String[]> data) {
		for (String[] movie : data){ 
			this.movies.add(new Movie(movie));
		}
	}
	
	public ArrayList<Movie> getMovies(){
		return this.movies;
	}
}
