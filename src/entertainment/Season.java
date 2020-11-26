package entertainment;
//
//import java.util.ArrayList;
//import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * Added number of users that rated the film
     */
    private int numRating;
    /**
     * Added average rating
     */
    private double rating;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        //this.ratings = new ArrayList<>();
    }

    public int getNumRating() {
        return numRating;
    }

    public double getRating() {
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void setNumRating(final int numRating) {
        this.numRating = numRating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}

