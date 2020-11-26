package additional;


import java.util.List;

public class FindFavoriteOccurrence {
    /**
     * @param movie the movie we want to find how many times was put in favorite
     * @param users the list of users
     * @return how many times the movie was chosen as favorite
     *
     *  Function to calculate how many times a movie was selected as favorite.
     *
     */
    public int movieFavOccurrence(final fileio.MovieInputData movie,
                                  final List<fileio.UserInputData> users) {
        int occurrenceNum = 0;
        for (fileio.UserInputData user : users) {
            if (user.getFavoriteMovies().contains(movie.getTitle())) {
                occurrenceNum++;
            }
        }
        return occurrenceNum;
    }

    /**
     * @param show the show we want to find how many times was put in favorite
     * @param users the list of users
     * @return how many times the show was chosen as favorite
     *
     *  Function to calculate how many times a show was selected as favorite.
     *
     */
    public int showFavOccurrence(final fileio.SerialInputData show,
                                 final List<fileio.UserInputData> users) {
        int occurrenceNum = 0;
        for (fileio.UserInputData user : users) {
            if (user.getFavoriteMovies().contains(show.getTitle())) {
                occurrenceNum++;
            }
        }
        return occurrenceNum;
    }
}
