package additional;

import fileio.SerialInputData;

public class ShowTotalLength {
    /**
     *
     * @param show the show we want to find rating for
     * @return the rating of the show
     *
     *  Find the show length by summing all season's duration.
     */
    public int findShowLength(final SerialInputData show) {
        int showLength = 0;
        for (entertainment.Season season : show.getSeasons())  {
            showLength += season.getDuration();
        }
        return showLength;
    }
}
