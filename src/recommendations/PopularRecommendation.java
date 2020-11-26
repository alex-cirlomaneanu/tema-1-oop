package recommendations;

import additional.PopularGenre;
import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopularRecommendation {
    /**
     *
     * @param recommendation action given to execute
     * @param movies all movies
     * @param shows all shows
     * @param users all users
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method executes the popular recommendation, by searching the most popular genres and
     *  returning the first unseen video of that genre.
     */
    public JSONObject givePopularRecommendation(final fileio.ActionInputData recommendation,
                                                final List<MovieInputData> movies,
                                                final List<fileio.SerialInputData> shows,
                                                final List<fileio.UserInputData> users,
                                                final fileio.Writer fileWriter)
            throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = recommendation.getActionId();
        String message;
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  Get a list of the most popular genres in descending order of their views
        List<String> mostPopularGenres = new PopularGenre().getPopularGenre(users, movies, shows);
        boolean findUser = false;
        fileio.UserInputData foundUser = new UserInputData(
                "", "", null, null);
        for (fileio.UserInputData user : users) {
            if (recommendation.getUsername().equals(user.getUsername())) {
                //  Since this recommendation is for premium users, verify if the user that will get
                //  the recommendation has a premium subscription.
                if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
                    message = "PopularRecommendation cannot be applied!";
                    return fileWriter.writeFile(id, null, message);
                } else {
                    findUser = true;
                    foundUser = user;
                    break;
                }
            }
        }
        if (!findUser) {
            //  If the user can not be find display error message
            message = "PopularRecommendation cannot be applied!";
            return fileWriter.writeFile(id, null, message);
        }

        //  Map that holds the video's title as key and their generes as value
        //  First add the movies, then the shows, respecting the order the videos appear in
        //  the database.
        Map<String, List<String>> videoTitles = new HashMap<>();
        for (fileio.MovieInputData movie : movies) {
            videoTitles.put(movie.getTitle(), movie.getGenres());
        }
        for (fileio.ShowInput show : shows) {
            videoTitles.put(show.getTitle(), show.getGenres());
        }

        for (String genre : mostPopularGenres) {
            for (String title : videoTitles.keySet()) {
                if (videoTitles.get(title).contains(genre)) {
                    if (!foundUser.getHistory().containsKey(title)) {
                        //  Return the first unseen title of the most popular genre. If the first
                        //  popular genre has all its videos seen, search an unseen video in the
                        //  next most popular genre.
                        aux.append("PopularRecommendation result: ");
                        aux.append(title);
                        message = aux.toString();
                        return fileWriter.writeFile(id, null, message);
                    }
                }
            }
        }
        //  If no video can be found display error message.
        message = "PopularRecommendation cannot be applied!";
        return fileWriter.writeFile(id, null, message);
    }
}
