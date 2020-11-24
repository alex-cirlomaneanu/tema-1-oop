package additional;

import fileio.SerialInputData;

public class ShowTotalLength {
    /**
     *
     * @param show the show we want to find rating for
     * @return the rating of the show
     */
    public int findShowLength(SerialInputData show) {
        int showLength = 0;
        for (entertainment.Season season : show.getSeasons())  {
            showLength += season.getDuration();
        }
        return showLength;
    }
}
