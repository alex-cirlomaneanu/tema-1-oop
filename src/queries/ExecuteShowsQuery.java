package queries;

import additional.FindFavoriteOccurrence;
import additional.FindViewsNumber;
import additional.ShowRatingCalculator;
import additional.ShowTotalLength;
import common.Constants;
import fileio.ActionInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExecuteShowsQuery {
    /**
     * @param query the query to execute
     * @param shows list of shows
     * @param users list of users
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  Similarly to movie query, this method does the a video query given in input. Firstly it
     *  searches the shows that have the year and/or genre asked in query. If there are found shows
     *  that respect the year and genre, proceed to sort the shows according to the criteria
     *  (favorite, most viewed, longest, ratings). Return the found shows in ascending or
     *  descending order.
     */
    public JSONObject queryShows(final ActionInputData query,
                                 final List<SerialInputData> shows,
                                 final List<UserInputData> users,
                                 final Writer fileWriter) throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  List of shows that have the correct year and/or genre.
        List<SerialInputData> searchShows = new ArrayList<>();

        //  Search for movies that respect both the year and the generes from query
        for (SerialInputData show : shows) {
            boolean correctYear = true;
            boolean correctGenre = true;
            // Use this string to compare the movie's year and query's year
            String movieYear = "" + show.getYear();
            if  (!movieYear.equals(query.getFilters().get(Constants.YEAR_INDEX).get(0))) {
                correctYear = false;
            }
            for (String genre : query.getFilters().get(Constants.GENRE_INDEX)) {
                if (!show.getGenres().contains(genre)) {
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
                searchShows.add(show);
            }
        }

        searchShows.sort((firstShow, secondShow) -> {
                int result;
                //  Switch cases to sort shows according to the given criteria.
                //  All sorting methods will sort the shows in ascending order with respect to the
                //  selected criteria.
                switch (query.getCriteria()) {
                    case Constants.RATINGS -> {
                        double firstShowRating =
                                new ShowRatingCalculator().findShowRating(firstShow);
                        double secondShowRating =
                                new ShowRatingCalculator().findShowRating(secondShow);
                        result = Double.compare(firstShowRating, secondShowRating);
                        if (result == 0) {
                            result = firstShow.getTitle().compareTo(secondShow.getTitle());
                            //  Alphabetical order in case the shows have the same rating
                        }
                    }
                    case Constants.MOST_VIEWED -> {
                        //  Use additional class and method to sort shows by view criteria.
                        FindViewsNumber viewsTimes = new FindViewsNumber();
                        int firstShowViewsNum =
                                viewsTimes.findViewsShow(firstShow, users);
                        int secondShowViewsNum =
                                viewsTimes.findViewsShow(secondShow, users);
                        result = Integer.compare(firstShowViewsNum,
                                secondShowViewsNum);
                        if (result == 0) {
                            result = firstShow.getTitle().compareTo(secondShow.getTitle());
                        }
                    }
                    case Constants.LONGEST -> {
                        ShowTotalLength showDuration = new ShowTotalLength();
                        int firstShowDuration =
                                showDuration.findShowLength(firstShow);
                        int secondShowDuration =
                                showDuration.findShowLength(secondShow);
                        result = Integer.compare(firstShowDuration,
                                secondShowDuration);
                        if (result == 0) {
                            result = firstShow.getTitle().compareTo(secondShow.getTitle());
                        }
                    }
                    case Constants.FAVORITE -> {
                        //  Use additional class and method to sort shows by favorite criteria.
                        FindFavoriteOccurrence favoriteOccurrence = new FindFavoriteOccurrence();
                        int firstShowFavNum =
                                favoriteOccurrence.showFavOccurrence(firstShow, users);
                        int secondShowFavNum =
                                favoriteOccurrence.showFavOccurrence(secondShow, users);
                        result = Integer.compare(firstShowFavNum,
                                secondShowFavNum);
                        if (result == 0) {
                            result = firstShow.getTitle().compareTo(secondShow.getTitle());
                        }
                    }
                    default -> throw new IllegalStateException(
                            "Unexpected value: " + query.getCriteria());
                }
                return result;
                });

        //  Using an iterator to avoid ConcurrentModificationException which happen while I
        //  try to remove a movie from the list at the same time when iterating through
        //  the list
        Iterator<SerialInputData> iter = searchShows.iterator();
        while (iter.hasNext()) {
            // A improper show is one with 0 views or 0 rating or 0 favorite
            boolean improper = false;
            SerialInputData show = iter.next();
            switch (query.getCriteria()) {
                case Constants.MOST_VIEWED -> {
                    int numViews = new FindViewsNumber().findViewsShow(show, users);
                    improper = (numViews == 0);
                }
                case Constants.RATINGS -> {
                    double rating = new ShowRatingCalculator().findShowRating(show);
                    improper = (rating == 0);
                }
                case Constants.FAVORITE -> {
                    int numFavorite = new FindFavoriteOccurrence().showFavOccurrence(show, users);
                    improper = (numFavorite == 0);
                }
                case Constants.LONGEST -> {
                    //  A show cannot have duration 0, but use this case not to throw an error.
                }
                default ->
                        throw new IllegalStateException("Unexpected value: " + query.getCriteria());
            }
            if  (improper) {
                    iter.remove();
                }
            }

        //  Use the minimum number of shows between the given number and the actual list length
        int listLength = Math.min(searchShows.size(), query.getNumber());


        if (query.getSortType().equals(Constants.DESC)) {
            //  Revert the list in order to return the show list in descending order
            Collections.reverse(searchShows);
        }
        for (int i = 0; i < listLength; ++i) {
            aux.append(searchShows.get(i).getTitle());
            if (i != (searchShows.size() - 1))    {
                aux.append(", ");
            }
        }
        aux.append("]");
        message += aux.toString();
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
