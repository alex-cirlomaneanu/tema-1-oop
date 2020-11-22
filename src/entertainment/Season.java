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
//    /**
//     * List of ratings for each season
//     */
//    private List<Double> ratings;

    /**
     * Number of users that rated the film
     */
    private static int numRating;
    /**
     * Average rating
     */
    private static float rating;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        //this.ratings = new ArrayList<>();
    }

    public int getNumRating() {
        return numRating;
    }

    public float getRating() {
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }
//
//    public List<Double> getRatings() {
//        return ratings;
//    }

    //  Setters for rating and number of rating
//    public void setRatings(final List<Double> ratings) {
//        this.ratings = ratings;
//    }

    public void setNumRating(int numRating) {
        this.numRating = numRating;
    }

    public void setRating(float rating) {
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

