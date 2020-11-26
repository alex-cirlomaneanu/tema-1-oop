package recommendations;

import additional.VideosRatingList;
import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class SearchRecommendation {
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
     *  This method executes the search recommendation, by returning all unseen videos in the given
     *  genre.
     */
    public JSONObject getSearchRecommendation(final fileio.ActionInputData recommendation,
                                                final List<MovieInputData> movies,
                                                final List<fileio.SerialInputData> shows,
                                                final List<fileio.UserInputData> users,
                                                final fileio.Writer fileWriter)
            throws IOException {
        //    id and message will be displayed in result array using fileWriter
        int id = recommendation.getActionId();
        String message;
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  The given genre for recommendation.
        String searchGenre = recommendation.getGenre();
        // The list of videos that contain the given genre, sorted in descending order by
        //  ratings.
        List<String> videosRatingList =
                new VideosRatingList().sortAllVideos(searchGenre, movies, shows);

        boolean findUser = false;
        fileio.UserInputData foundUser = new UserInputData(
                "", "", null, null);
        for (fileio.UserInputData user : users) {
            if (recommendation.getUsername().equals(user.getUsername())) {
                //  Since this recommendation is for premium users, verify if the user that will get
                //  the recommendation has a premium subscription.
                if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
                    message = "SearchRecommendation cannot be applied!";
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
            message = "SearchRecommendation cannot be applied!";
            return fileWriter.writeFile(id, null, message);
        }

        aux.append("SearchRecommendation result: [");
        for (String title : videosRatingList) {
            //  Find an unseen video for the user.
            if (!foundUser.getHistory().containsKey(title)) {
                aux.append(title);
                aux.append(", ");
            }
        }
        //  If no video was added return an error message
        if (aux.toString().equals("SearchRecommendation result: [")) {
            message = "SearchRecommendation cannot be applied!";

            return fileWriter.writeFile(id, null, message);
        }
        //  Remove the last to characters in aux (", ") and add it to the
        //  success message
        aux.setLength(aux.length() - 2);
        aux.append("]");
        message = aux.toString();
        return fileWriter.writeFile(id, null, message);

    }
}
