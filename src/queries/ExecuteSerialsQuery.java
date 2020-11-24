package queries;

import additional.FindFavoriteOccurrence;
import additional.FindViewsNumber;
import additional.ShowRatingCalculator;
import additional.ShowTotalLength;
import common.Constants;
import fileio.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExecuteSerialsQuery {
    /**
     *  queries for serials
     * @return
     */
    public JSONObject querySerials(final ActionInputData query,
                                   final List<SerialInputData> shows,
                                   final List<UserInputData> users,
                                   final Writer fileWriter) throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        List<SerialInputData> searchShows = new ArrayList<>();
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
            if (correctYear && correctGenre)    {
                searchShows.add(show);
            }
        }

        searchShows.sort((firstSerial, secondSerial) -> {
                int result = 0;
                switch (query.getCriteria()) {
                    case Constants.RATINGS -> {
                        double firstSerialRating =
                                new ShowRatingCalculator().findSerialRating(firstSerial);
                        double secondSerialRating =
                                new ShowRatingCalculator().findSerialRating(secondSerial);
                        result = Double.compare(firstSerialRating, secondSerialRating);
                        if (result == 0) {
                            result = firstSerial.getTitle().compareTo(secondSerial.getTitle());
                            //  Alphabetical order in case the films have the same rating
                        }
                    }
                    case Constants.MOST_VIEWED -> {
                        FindViewsNumber viewsTimes = new FindViewsNumber();
                        int firstSerialViewsNum =
                                viewsTimes.findViewsShow(firstSerial, users);
                        int secondSerialViewsNum =
                                viewsTimes.findViewsShow(secondSerial, users);
                        result = Integer.compare(firstSerialViewsNum,
                                secondSerialViewsNum);
                        if (result == 0) {
                            result = firstSerial.getTitle().compareTo(secondSerial.getTitle());
                            //  Alphabetical order in case the films have the same rating
                        }
                    }
                    case Constants.LONGEST -> {
                        ShowTotalLength showDuration = new ShowTotalLength();
                        int firstSerialDuration =
                                showDuration.findShowLength(firstSerial);
                        int secondSerialDuration =
                                showDuration.findShowLength(secondSerial);
                        result = Integer.compare(firstSerialDuration,
                                secondSerialDuration);
                        if (result == 0) {
                            result = firstSerial.getTitle().compareTo(secondSerial.getTitle());
                            //  Alphabetical order in case the films have the same rating
                        }
                    }
                    case Constants.FAVORITE -> {
                        //  In case query is by favorite number
                        FindFavoriteOccurrence favoriteOccurrence = new FindFavoriteOccurrence();
                        int firstSerialFavNum =
                                favoriteOccurrence.ShowFavOccurrence(firstSerial, users);
                        int secondSerialFavNum =
                                favoriteOccurrence.ShowFavOccurrence(secondSerial, users);
                        result = Integer.compare(firstSerialFavNum,
                                secondSerialFavNum);
                        if (result == 0) {
                            result = firstSerial.getTitle().compareTo(secondSerial.getTitle());
                            //  Alphabetical order in case the films have the same rating
                        }
                    }
                    //default -> throw new IllegalStateException("Unexpected value: " + query.getCriteria());
                }
                return result;
                });

        //  Using an iterator to avoid ConcurrentModificationException which happen while I
        //  try to remove a movie from the list at the same time when iterating through
        //  the list
        Iterator<SerialInputData> iter = searchShows.iterator();
        while (iter.hasNext()) {
            // A noncoform show is one with 0 views or 0 rating or 0 favorite
            boolean nonconform = false;
            SerialInputData show = iter.next();
            if (query.getCriteria().equals(Constants.MOST_VIEWED)) {
                int numViews = new FindViewsNumber().findViewsShow(show, users);
                nonconform = (numViews == 0);
            } else if (query.getCriteria().equals(Constants.RATINGS)){
                double rating = new ShowRatingCalculator().findSerialRating(show);
                nonconform = (rating == 0);
            } else if (query.getCriteria().equals(Constants.FAVORITE)) {
                int numFavorite = new FindFavoriteOccurrence().ShowFavOccurrence(show, users);
                nonconform = (numFavorite == 0);
            }
            // If nobody has seen the movie, it will not be returned
            if  (nonconform) {
                    iter.remove();
                }
            }

        //  Return the minimum number of serials between the given number and the actual size of
        //  the list
        int listLength = Math.min(searchShows.size(), query.getNumber());


        //  Returning the serial list in ascending order
        if  (query.getSortType().equals(Constants.ASC)) {
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchShows.get(i).getTitle());
                if (i != (searchShows.size() - 1))    {
                    aux.append(", ");
                }
            }
        }   else if (query.getSortType().equals(Constants.DESC)) {
            //  Revert the list in order to return the serial list in descending order
            Collections.reverse(searchShows);
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchShows.get(i).getTitle());
                if (i != (searchShows.size() - 1))    {
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
