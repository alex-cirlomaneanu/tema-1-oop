package recommendations;

import additional.FavoriteVideos;
import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;


public class FavoriteRecommendation {
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
     *  This method executes the favorite recommendation, by searching all the videos the user did
     *  not see and returning the video with the most appearances in the favorite list in the order
     *  they appear in database.
     */
    public JSONObject getFavoriteRecommendation(final fileio.ActionInputData recommendation,
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
        // Get the movies and shows sorted by how many time they appear in favorite list
        List<String> favoriteMovies =
                new FavoriteVideos().getFavoriteMovies(users, movies);
        List<String> favoriteShows =
                new FavoriteVideos().getFavoriteShows(users, shows);
        boolean findUser = false;
        fileio.UserInputData foundUser = new UserInputData(
                "", "", null, null);
        for (fileio.UserInputData user : users) {
            //  Since this recommendation is for premium users, verify if the user that will get
            //  the recommendation has a premium subscription.
            if (recommendation.getUsername().equals(user.getUsername())) {
                if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
                    message = "FavoriteRecommendation cannot be applied!";
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
            message = "FavoriteRecommendation cannot be applied!";
            return fileWriter.writeFile(id, null, message);
        }
        String recommendedTitle;
        for (String movieTitle : favoriteMovies) {
            //  Return the first movie the user did not see from the most favorite movies list.
            if (!foundUser.getHistory().containsKey(movieTitle)) {
                recommendedTitle = movieTitle;
                aux.append("FavoriteRecommendation result: ");
                aux.append(recommendedTitle);
                message = aux.toString();
                return fileWriter.writeFile(id, null, message);
            }
        }
        for (String showTitle : favoriteShows) {
            //  If the user has seen all the movies, return the first show he did not see
            if (!foundUser.getHistory().containsKey(showTitle)) {
                recommendedTitle = showTitle;
                aux.append("FavoriteRecommendation result: ");
                aux.append(recommendedTitle);
                message = aux.toString();
                return fileWriter.writeFile(id, null, message);
            }
        }
        //  If the user saw all the videos display error message.
        message = "FavoriteRecommendation cannot be applied!";
        return fileWriter.writeFile(id, null, message);
    }
}
