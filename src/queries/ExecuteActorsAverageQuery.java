package queries;

import additional.ShowRatingCalculator;
import common.Constants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Class that has methods to solve the given query
 */
public class ExecuteActorsAverageQuery {
    /**
     * query by actors
     *
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
            double newRatingSum = 0;
            int newRatingsNumber = 0;
            for (String role : actor.getFilmography())  {
                for (fileio.MovieInputData movie : movies)  {
                    if  (role.equals(movie.getTitle())) {
                        if  (movie.getNumRating() > 0)   {
                            newRatingsNumber++;
                            newRatingSum += movie.getRating();
                        }
                    }
                }
                for (fileio.SerialInputData show : shows)  {
                    if  (role.equals(show.getTitle()))  {
                        double showRating = 0;
                        for (entertainment.Season season : show.getSeasons())  {
                            if  (season.getRating() > 0) {
                                showRating += season.getRating();
                            }
                        }
                        if  (showRating > 0) {
                            newRatingSum += showRating / show.getNumberSeason();
                            newRatingsNumber++;
                        }
                    }
                }
            }
            newRatingSum += (actor.getActorRating() * actor.getRatingNum());
            newRatingsNumber += actor.getRatingNum();
            actor.setActorRating(newRatingSum / newRatingsNumber);
            actor.setRatingNum(newRatingsNumber);
            if  (actor.getActorRating() > 0) {
                searchActors.add(actor);
            }

        }

        Collections.sort(searchActors, new Comparator<>() {
            @Override
            public int compare(final fileio.ActorInputData actor1,
                               final fileio.ActorInputData actor2) {
                //  Ascending order
                int result;
                result = Double.compare(actor1.getActorRating(), actor2.getActorRating());
                if (result == 0) {
                    result = actor1.getName().compareTo(actor2.getName());

                    //avem nevoie alfabetic
                }
                return result;
            }
        });

        int listLength = Math.min(searchActors.size(), query.getNumber());

        if  (query.getSortType().equals(Constants.ASC)) {
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchActors.get(i).getName());
                if (i != (listLength - 1))    {
                    aux.append(", ");
                }
            }
        }   else if (query.getSortType().equals(Constants.DESC)) {
            Collections.reverse(searchActors);
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchActors.get(i).getName());
                if (i != (listLength - 1))    {
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
