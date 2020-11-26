package additional;

import fileio.SerialInputData;


public class ShowRatingCalculator {
    /**
     *
     * @param show the show we want to find rating for
     * @return the rating of the show
     *
     *  Method to calculate the show rating, using every season rating.
     */
    public double findShowRating(final SerialInputData show) {
        double showRating = 0;
        for (entertainment.Season season : show.getSeasons())  {
            if  (season.getRating() > 0) {
                showRating += season.getRating();
            }
        }
        showRating = showRating / show.getNumberSeason();
        return showRating;
    }
}
