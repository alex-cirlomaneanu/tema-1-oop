package additional;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;





public class PopularGenre {
    /**
     *
     * @param users all users
     * @param movies all movies
     * @param shows all shows
     * @return a sorted list of the most popular genres
     *
     *  This method is used to help getting the popular recommendation. It sorts all the genres by
     *  the number of views a genre gets (both a movie and a show genre).
     */
    public List<String> getPopularGenre(final List<fileio.UserInputData> users,
                                        final List<fileio.MovieInputData> movies,
                                        final List<fileio.SerialInputData> shows) {
        //  Use maps to get the videos and their corresponding number of views
        Map<fileio.MovieInputData, Integer> allMovieViews = new HashMap<>();
        Map<fileio.SerialInputData, Integer> allShowViews = new HashMap<>();
        for (fileio.MovieInputData movie : movies) {
            int movieViews = new FindViewsNumber().findViewsMovie(movie, users);
            allMovieViews.put(movie, movieViews);
        }
        for (fileio.SerialInputData show : shows) {
            int showViews = new FindViewsNumber().findViewsShow(show, users);
            allShowViews.put(show, showViews);
        }

        //  Create a map of genre keys and view values
        Map<String, Integer> genreViews = new HashMap<>();
        for (fileio.MovieInputData movie : allMovieViews.keySet()) {
            for (String genre : movie.getGenres()) {
                Integer value = genreViews.get(genre);
                if (value == null) {
                    // Put the genre in the map if it is not in yet.
                    genreViews.put(genre, allMovieViews.get(movie));
                } else {
                    // Increment the number of views of the genre
                    genreViews.put(genre, value + allMovieViews.get(movie));

                }
            }
        }
        // Use the same approach as above to add shows genre views
        for (fileio.SerialInputData show : allShowViews.keySet()) {
            for (String genre : show.getGenres()) {
                Integer value = genreViews.get(genre);
                if (value == null) {
                    genreViews.put(genre, allShowViews.get(show));
                } else {
                    genreViews.put(genre, value + allShowViews.get(show));

                }
            }
        }

        //  Use a list of map entries to for a easier way to sort the genre map.
        List<Map.Entry<String, Integer>> sortedGenres = new ArrayList<>(genreViews.entrySet());
        //  Use a lambda function to sort the list by views in descending order
        sortedGenres.sort((firstGenre, secondGenre) -> {
            int result;
            result = Integer.compare(firstGenre.getValue(), secondGenre.getValue());
            result *= -1;

            if (result == 0) {
                result = firstGenre.getKey().compareTo(secondGenre.getKey());
            }
            return result;
        });

        //  A list of the genre without the actual values of views.
        List<String> popularGenres = new ArrayList<>();
        for (Map.Entry<String, Integer> genre : sortedGenres) {
            if (genre.getValue() != 0) {
                popularGenres.add(genre.getKey());
            }
        }
        //  Return the sorted list of the most popular genres.
        return popularGenres;
    }
}
