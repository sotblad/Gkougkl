package model;

public class Movie {
	private String imageUrl;
	private String title;
	private String releaseYear;
	private String genre;
	private String rating;
	private String duration;
	private String description;
	private String directors;
	private String stars;
	
	public Movie(String[] movieColumns) {
		this.imageUrl = movieColumns[0];
		this.title = movieColumns[1];
		this.releaseYear = movieColumns[2];
		this.genre = movieColumns[3].replaceAll("[\"\\'\\[\\]]", "");
		this.rating = movieColumns[4];
		this.duration = movieColumns[5];
		this.description = movieColumns[6];
		this.directors = movieColumns[7].replaceAll("[\"\\'\\[\\]]", "");
		this.stars = movieColumns[8].replaceAll("[\"\\'\\[\\]]", "");
	}

	public Movie(String imageUrl, String title, String releaseYear, String genre, String rating, String duration, String description,
			String directors, String stars) {
		super();
		this.imageUrl = imageUrl;
		this.title = title;
		this.releaseYear = releaseYear;
		this.genre = genre;
		this.rating = rating;
		this.duration = duration;
		this.description = description;
		this.directors = directors;
		this.stars = stars;
	}

	public Movie() {
		// TODO Auto-generated constructor stub
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public String getFulltext() {
		return title + " " + releaseYear + " " + genre + " " + rating
				+ " " + duration + " " + description + " " + directors + " "
				+ stars;
	}
	
	@Override
	public String toString() {
		return "Movie [title=" + title + ", releaseYear=" + releaseYear + ", genre=" + genre + ", rating=" + rating
				+ ", duration=" + duration + ", description=" + description + ", directors=" + directors + ", stars="
				+ stars + "]";
	}

}
