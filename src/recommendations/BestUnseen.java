package recommendations;

import additional.ShowRatingCalculator;
import common.Constants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BestUnseen {
    /**
     *
     * @param recommendation action given to execute
     * @param movies the list of movies
     * @param shows the list of shows
     * @param users the list of users
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method executes the best unseen recommendation, by searching all the videos the
     *  user did not see and returning the video with the highest rating in the order they appear
     *  in database.
     */
    public JSONObject giveBestUnseenRecommendation(final fileio.ActionInputData recommendation,
                                                 final List<fileio.MovieInputData> movies,
                                                 final List<fileio.SerialInputData> shows,
                                                 final List<fileio.UserInputData> users,
                                                 final fileio.Writer fileWriter)
            throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = recommendation.getActionId();
        String message;
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        aux.append("BestRatedUnseenRecommendation result: ");
        String recommendedTitle = "";
        List<fileio.MovieInputData> bestMovies = new ArrayList<>();
        List<fileio.SerialInputData> bestShows = new ArrayList<>();
        for (fileio.UserInputData user : users) {
            if (user.getUsername().equals(recommendation.getUsername())) {
                for (fileio.MovieInputData movie : movies) {
                    //  Add the movie to the list if the user did not see it.
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        bestMovies.add(movie);
                    }
                }
                for (fileio.SerialInputData show : shows) {
                    //  Add the show to the list if the user did not see it.
                    if (!user.getHistory().containsKey(show.getTitle())) {
                        bestShows.add(show);
                    }
                }
            }
        }

        bestMovies.sort((firstMovie, secondMovie) -> {
            //  Sort the movies by rating.
            int result;
            result = Double.compare(firstMovie.getRating(), secondMovie.getRating());
            if (result == 0) {
                result = firstMovie.getTitle().compareTo(secondMovie.getTitle());
                //  Alphabetical order in case the films have the same rating
            }
            return result;
        });
        //  Reverse the list to obtain descending order.
        Collections.reverse(bestMovies);
        bestShows.sort((firstShow, secondShow) -> {
            // Sort shows by rating
            int result;
            double firstSohwRating =
                    new ShowRatingCalculator().findShowRating(firstShow);
            double secondShowRating =
                    new ShowRatingCalculator().findShowRating(secondShow);
            result = Double.compare(firstSohwRating, secondShowRating);
            if (result == 0) {
                result = firstShow.getTitle().compareTo(secondShow.getTitle());
                //  Alphabetical order in case the films have the same rating
            }
            return result;
        });
        //  Reverse the list to obtain descending order.
        Collections.reverse(bestShows);
        if (!bestMovies.isEmpty()) {
            //  If the the best movies list is not empty return the movie with the highest rating.
            recommendedTitle = bestMovies.get(Constants.ZERO).getTitle();
            aux.append(recommendedTitle);
            message = aux.toString();
        } else if (bestMovies.isEmpty() && !(bestShows.isEmpty())) {
            //  If the best movies list is empty and the best shows is not, return the show with
            //  the highest rating.
            recommendedTitle = bestShows.get(Constants.ZERO).getTitle();
            aux.append(recommendedTitle);
            message = aux.toString();
        } else {
            //  Return error message
            message = "BestRatedUnseenRecommendation cannot be applied!";
        }
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
