package queries;

import additional.FindFavoriteOccurrence;
import additional.FindViewsNumber;
import common.Constants;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.UserInputData;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExecuteMoviesQuery {
    /**
     * queries for movies
     *
     */
    public JSONObject queryMovies(final ActionInputData query,
                                  final List<MovieInputData> movies,
                                  final List<UserInputData> users,
                                  final Writer fileWriter) throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        List<MovieInputData> searchMovies = new ArrayList<>();

        //  Search for movies that respect both the year and the generes from query
        for (MovieInputData movie : movies) {
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
            if (correctYear && correctGenre)    {
                searchMovies.add(movie);
            }
        }

        searchMovies.sort((firstMovie, secondMovie) -> {
            int result;
            //  Switch cases for the criteria the query uses for movies
            //  All sorting methods will sort the movies in ascending order with respect to the
            //  selected criteria.
            switch (query.getCriteria()) {
                case Constants.RATINGS -> {
                    //  In case query is by rating
                    result = Double.compare(firstMovie.getRating(), secondMovie.getRating());
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                        //  Alphabetical order in case the films have the same rating
                    }
                }
                case Constants.LONGEST -> {
                    //  In case query is by longest movie
                    result = Integer.compare(firstMovie.getDuration(),
                            secondMovie.getDuration());
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                        //  Alphabetical order in case the films have the same rating
                    }
                }
                case Constants.FAVORITE -> {
                    //  In case query is by favorite number
                    FindFavoriteOccurrence favoriteOccurrence = new FindFavoriteOccurrence();
                    int firstMovieFavNum =
                            favoriteOccurrence.MovieFavOccurrence(firstMovie, users);
                    int secondMovieFavNum =
                            favoriteOccurrence.MovieFavOccurrence(secondMovie, users);
                    result = Integer.compare(firstMovieFavNum,
                            secondMovieFavNum);
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                        //  Alphabetical order in case the films have the same rating
                    }
                }
                case Constants.MOST_VIEWED -> {
                    //  In case query is by views
                    FindViewsNumber viewsTimes = new FindViewsNumber();
                    int firstMovieViewsNum =
                            viewsTimes.findViewsMovie(firstMovie, users);
                    int secondMovieViewsNum =
                            viewsTimes.findViewsMovie(secondMovie, users);
                    result = Integer.compare(firstMovieViewsNum,
                            secondMovieViewsNum);
                    if (result == 0) {
                        result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                        //  Alphabetical order in case the films have the same rating
                    }
                }
                default -> throw new IllegalStateException(
                        "Unexpected value: " + query.getCriteria());
            }

            return result;
        });

        if (query.getCriteria().equals(Constants.MOST_VIEWED)) {
            //  Using an iterator to avoid ConcurrentModificationException which happen while I
            //  try to remove a movie from the list at the same time when iterating through
            //  the list
            Iterator<MovieInputData> iter = searchMovies.iterator();
            while (iter.hasNext()) {
                FindViewsNumber viewsTimes = new FindViewsNumber();
                MovieInputData movie = iter.next();
                int numViews = viewsTimes.findViewsMovie(movie, users);
                // If nobody has seen the movie, it will not be returned
                if  (numViews == 0) {
                    iter.remove();
                }
            }
        }

        //  Return the minimum number of movies between the given number and the actual size of
        //  the list
        int listLength = Math.min(searchMovies.size(), query.getNumber());

        //  Returning the movie list in ascending order
        if  (query.getSortType().equals(Constants.ASC)) {
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchMovies.get(i).getTitle());
                if (i != (searchMovies.size() - 1))    {
                    aux.append(", ");
                }
            }
        }   else if (query.getSortType().equals(Constants.DESC)) {
            //  Revert the list in order to return the movie list in descending order
            Collections.reverse(searchMovies);
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchMovies.get(i).getTitle());
                if (i != (searchMovies.size() - 1))    {
                    aux.append(", ");
                }
            }
        }
        aux.append("]");
        message += aux.toString();
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
