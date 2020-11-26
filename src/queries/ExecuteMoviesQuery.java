package queries;

import additional.FindFavoriteOccurrence;
import additional.FindViewsNumber;
import common.Constants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExecuteMoviesQuery {
    /**
     *
     * @param query the query to execute
     * @param movies list of movies
     * @param users list of users
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method does the movie query given in input. Firstly it searches the movies that have
     *  the year and/or genre asked in query. If there are found movies that respect the year and
     *  genre, proceed to sort the movies according to the criteria (favorite, most viewed,
     *  longest, ratings). Return the found movies in ascending or descending order.
     */
    public JSONObject queryMovies(final fileio.ActionInputData query,
                                  final List<fileio.MovieInputData> movies,
                                  final List<fileio.UserInputData> users,
                                  final fileio.Writer fileWriter) throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  List of movies that have the correct year and/or genre.
        List<fileio.MovieInputData> searchMovies = new ArrayList<>();

        //  Search for movies that respect both the year and the generes from query
        for (fileio.MovieInputData movie : movies) {
            boolean correctYear = true;
            boolean correctGenre = true;
            // Use this string to compare the movie's year and query's year
            String movieYear = "" + movie.getYear();
            if  (!movieYear.equals(query.getFilters().get(Constants.YEAR_INDEX).get(0))) {
                correctYear = false;
            }
            for (String genre : query.getFilters().get(Constants.GENRE_INDEX)) {
                if (!movie.getGenres().contains(genre)) {
                    correctGenre = false;
                    break;
                }
            }
            //  If the query offers no genre or year add any movie, hoping the other criteria is
            //  offered.
            if (query.getFilters().get(Constants.GENRE_INDEX) == null) {
                correctGenre = true;
            }
            if (query.getFilters().get(Constants.YEAR_INDEX).get(0) == null) {
                correctYear = true;
            }
            if (correctYear && correctGenre)    {
                searchMovies.add(movie);
            }
        }

        //  Sort movies according to the query criteria
        searchMovies.sort((firstMovie, secondMovie) -> {
            int result;
            //  Switch cases for the criteria the query uses for movies
            //  All sorting methods will sort the movies in ascending order with respect to the
            //  selected criteria.
            switch (query.getCriteria()) {
                case Constants.RATINGS -> {
                    result = Double.compare(firstMovie.getRating(), secondMovie.getRating());
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                        //  Alphabetical order in case the films have the same rating
                    }
                }
                case Constants.LONGEST -> {
                    result = Integer.compare(firstMovie.getDuration(),
                            secondMovie.getDuration());
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                    }
                }
                case Constants.FAVORITE -> {
                    //  Use additional class and method to sort movies by favorite criteria.
                    FindFavoriteOccurrence favoriteOccurrence = new FindFavoriteOccurrence();
                    int firstMovieFavNum =
                            favoriteOccurrence.movieFavOccurrence(firstMovie, users);
                    int secondMovieFavNum =
                            favoriteOccurrence.movieFavOccurrence(secondMovie, users);
                    result = Integer.compare(firstMovieFavNum,
                            secondMovieFavNum);
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                    }
                }
                case Constants.MOST_VIEWED -> {
                    //  Use additional class and method to sort movies by view criteria.
                    FindViewsNumber viewsTimes = new FindViewsNumber();
                    int firstMovieViewsNum =
                            viewsTimes.findViewsMovie(firstMovie, users);
                    int secondMovieViewsNum =
                            viewsTimes.findViewsMovie(secondMovie, users);
                    result = Integer.compare(firstMovieViewsNum,
                            secondMovieViewsNum);
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                    }
                }
                //  Default case if the given criteria is unknown.
                default -> throw new IllegalStateException(
                        "Unexpected value: " + query.getCriteria());
            }

            return result;
        });

        //  Using an iterator to avoid ConcurrentModificationException which happen while I try to
        //  remove a improper movie from the list at the same time when iterating through the list
        Iterator<fileio.MovieInputData> iter = searchMovies.iterator();
        while (iter.hasNext()) {
            // A improper movie is one with 0 views or 0 rating or 0 favorite
            boolean improper = false;
            fileio.MovieInputData movie = iter.next();
            switch (query.getCriteria()) {
                case Constants.MOST_VIEWED -> {
                    int numViews = new FindViewsNumber().findViewsMovie(movie, users);
                    improper = (numViews == 0);
                }
                case Constants.RATINGS -> {
                    double rating = movie.getRating();
                    improper = (rating == 0);
                }
                case Constants.FAVORITE -> {
                    int numFavorite =
                            new FindFavoriteOccurrence().movieFavOccurrence(movie, users);
                    improper = (numFavorite == 0);
                }
                case Constants.LONGEST -> {
                    //  A movie cannot have duration 0, but use this case not to throw an error.
                }
                default ->
                        throw new IllegalStateException(
                                "Unexpected value: " + query.getCriteria());
                }
                if  (improper) {
                    iter.remove();
                }
            }
        //  Use the minimum number of movies between the given number and the actual list length
        int listLength = Math.min(searchMovies.size(), query.getNumber());

       if (query.getSortType().equals(Constants.DESC)) {
            //  Revert the list in order to return the movie list in descending order
            Collections.reverse(searchMovies);
       }
       for (int i = 0; i < listLength; ++i) {
           aux.append(searchMovies.get(i).getTitle());
           if (i != (searchMovies.size() - 1)) {
               aux.append(", ");
           }
       }
       aux.append("]");
       message += aux.toString();
       //  Return message
       return fileWriter.writeFile(id, null, message);
    }
}
