package additional;


import java.util.List;

public class FindFavoriteOccurrence {
    /**
     *  Function used to compare movies by how many times a movie was marked as "favorite"
     */
    public int MovieFavOccurrence(final fileio.MovieInputData movie,
                                  final List<fileio.UserInputData> users) {
        int occurrenceNum = 0;
        for (fileio.UserInputData user : users) {
            if (user.getFavoriteMovies().contains(movie.getTitle())) {
                occurrenceNum++;
            }
        }
        return occurrenceNum;
    }
    public int ShowFavOccurrence(final fileio.SerialInputData show,
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
