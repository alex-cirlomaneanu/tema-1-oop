package fileio;

import java.util.ArrayList;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;
    /**
     * Number of users that rated the film
     */
    private int numRating;
    /**
     * Average rating
     */
    private double rating;

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.numRating = 0;
        this.rating = 0;

    }

    public int getDuration() {

        return duration;
    }

    public int getNumRating() {
        return numRating;
    }

    public double getRating() {
        return rating;
    }

    public void setNumRating(final int numRating) {
        this.numRating = numRating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }
}
