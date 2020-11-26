package queries;

import additional.ShowRatingCalculator;
import common.Constants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ActorsAverageQuery {
    /**
     *
     * @param query query given to execute
     * @param actors list of actors
     * @param movies list of movies
     * @param shows list of shows
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method does the actors query by their average rating. Firstly it searches the actors
     *  that have at least one rating given. If there are found actors that respect this criteria,
     *  proceed to sort the actors according to the ratings they have. Return the found actors in
     *  in ascending or descending order.
     */
    public JSONObject queryActorsAverage(final fileio.ActionInputData query,
                                         final List<fileio.ActorInputData> actors,
                                         final List<fileio.MovieInputData> movies,
                                         final List<fileio.SerialInputData> shows,
                                         final fileio.Writer fileWriter) throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        List<fileio.ActorInputData> searchActors = new ArrayList<>();

        for (fileio.ActorInputData actor : actors)  {
            //  Calculate the rating of the actor by doing an average of the videos in which the
            //  actor plays.
            double newRatingSum = 0;
            int newRatingsNumber = 0;
            for (String role : actor.getFilmography())  {
                //  Searching the videos the actor has a role first movies, then shows.
                for (fileio.MovieInputData movie : movies)  {
                    if  (role.equals(movie.getTitle())) {
                        if  (movie.getNumRating() > 0)   {
                            //  If the movies rating is not 0, the number of rating that actor has
                            //  is incremented by 1 and the sum incremented by the movie rating.
                            newRatingsNumber++;
                            newRatingSum += movie.getRating();
                        }
                    }
                }
                for (fileio.SerialInputData show : shows)  {
                    if  (role.equals(show.getTitle()))  {
                        //  If the show rating is not 0, the number of rating that actor has
                        //  is incremented by 1 and the sum incremented by the show rating.
                        //  Calculating the show rating using this additional class and method.
                        double showRating = new ShowRatingCalculator().findShowRating(show);
                        if  (showRating > 0) {
                            newRatingSum += showRating;
                            newRatingsNumber++;
                        }
                    }
                }
            }
            //  Set the new average rating, and total number of ratings.
            actor.setActorRating(newRatingSum / newRatingsNumber);
            actor.setRatingNum(newRatingsNumber);
            //  Add the actor if the rating is not 0.
            if  (actor.getActorRating() > 0) {
                searchActors.add(actor);
            }
        }

        //  Sort the list of found actors in ascending order by their rating.
        searchActors.sort((actor1, actor2) -> {
            int result;
            result = Double.compare(actor1.getActorRating(), actor2.getActorRating());
            if (result == 0) {
                result = actor1.getName().compareTo(actor2.getName());
            }
            return result;
        });

        //  Use the minimum number of actors between the given number and the actual list length
        int listLength = Math.min(searchActors.size(), query.getNumber());

        //   Reverse the list in order to return the list in descending order.
        if (query.getSortType().equals(Constants.DESC)) {
            Collections.reverse(searchActors);
        }
        //   Add the found actors to the message.
        for (int i = 0; i < listLength; ++i) {
            aux.append(searchActors.get(i).getName());
            if (i != (listLength - 1)) {
                aux.append(", ");
            }
        }
        aux.append("]");
        message += aux.toString();
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
