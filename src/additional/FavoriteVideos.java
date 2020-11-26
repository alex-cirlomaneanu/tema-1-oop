package additional;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteVideos {
    /**
     *
     * @param users all users in db
     * @param movies all movies in db
     * @return sorted list of movies title with respect to number of times a show was marked
     * as favorite
     *
     *  This method sorts movies title by rating (descending order)
     */
    public List<String> getFavoriteMovies(final List<UserInputData> users,
                                          final List<fileio.MovieInputData> movies) {
        //  Add in a map movies and their number of being favorite
        Map<String, Integer> allMovies = new HashMap<>();
        for (fileio.MovieInputData movie : movies) {
            int movieFavoriteNum = new FindViewsNumber().findViewsMovie(movie, users);
            allMovies.put(movie.getTitle(), movieFavoriteNum);
        }
        //  Put the map in a list to sort it easily.
        List<Map.Entry<String, Integer>> sortedMovies = new ArrayList<>(allMovies.entrySet());

        //  Use lambda function to compare movies by the value (times of being favorite)
        sortedMovies.sort((firstMovie, secondMovie) -> {
            int result;
            //  Compare values and then negate the result to obtains descending order.
            result = Integer.compare(firstMovie.getValue(), secondMovie.getValue());
            result *= -1;

            if (result == 0) {
                // In case of the same rating compare their titles.
                result = firstMovie.getKey().compareTo(secondMovie.getKey());
            }
            return result;
        });

        List<String> sortedFavoriteMovies = new ArrayList<>();
        for (Map.Entry<String, Integer> movie : sortedMovies) {
            if (movie.getValue() != 0) {
                sortedFavoriteMovies.add(movie.getKey());
            }
        }
        return sortedFavoriteMovies;
    }

    /**
     *
     * @param users all users in bd
     * @param shows all shows in db
     * @return sorted list of shows title with respect to number of times a show was marked
     * as favorite.
     *
     *  This method sorts shows title by rating (descending order)
     */
    public List<String> getFavoriteShows(final List<UserInputData> users,
                                                  final List<fileio.SerialInputData> shows) {
        //  Add in a map shows and their number of being favorite
        Map<String, Integer> allShows = new HashMap<>();
        for (fileio.SerialInputData show : shows) {
            int showFavoriteNum = new FindViewsNumber().findViewsShow(show, users);
            allShows.put(show.getTitle(), showFavoriteNum);
        }
        //  Put the map in a list to sort it easily.
        List<Map.Entry<String, Integer>> sortedShows = new ArrayList<>(allShows.entrySet());

        //  Use lambda function to compare shows by the value (times of being favorite)
        sortedShows.sort((firstMovie, secondMovie) -> {
            int result;
            //  Compare values and then negate the result to obtains descending order.
            result = Integer.compare(firstMovie.getValue(), secondMovie.getValue());
            result *= -1;

            if (result == 0) {
                // In case of the same rating compare their titles.
                result = firstMovie.getKey().compareTo(secondMovie.getKey());
            }
            return result;
        });

        List<String> sortedFavoriteShows = new ArrayList<>();
        for (Map.Entry<String, Integer> show : sortedShows) {
            if (show.getValue() != 0) {
                sortedFavoriteShows.add(show.getKey());
            }
        }
        return sortedFavoriteShows;
    }
}
