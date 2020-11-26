package recommendations;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class StandardRecommendation {
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
     *  This method executes the standard recommendation, by returning the first unseen video.
     */
    public JSONObject giveStandardRecommendation(final fileio.ActionInputData recommendation,
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
        aux.append("StandardRecommendation result: ");
        String recommendedTitle = "";
        for (fileio.UserInputData user : users) {
            //  Search the user that needs the recommendation.
            if  (user.getUsername().equals(recommendation.getUsername())) {
                for (fileio.MovieInputData movie : movies) {
                    //  If he did not see the current film, add its title as recommended title
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        recommendedTitle = movie.getTitle();
                    }
                }
                if (recommendedTitle.isEmpty()) {
                    //  If the user has seen all the movies, search the first show he did not see.
                    for (fileio.SerialInputData show : shows) {
                        if (!user.getHistory().containsKey(show.getTitle())) {
                            recommendedTitle = show.getTitle();
                        }
                    }
                }
            }
        }
        aux.append(recommendedTitle);
        //  If the recommended title is still empty create error message.
        if (recommendedTitle.isEmpty()) {
            message = "StandardRecommendation cannot be applied!";
        } else {
            // Else create the success message
            message = aux.toString();
        }
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
