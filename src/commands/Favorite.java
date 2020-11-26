package commands;

import fileio.UserInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class Favorite {
    /**
     *
     * @param favourite the command given in input
     * @param users the list of users
     * @param fileWriter the tool that writes the output into JSONObject
     * @return the JSONObject result of adding the video to "favorite"
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method searches to user that wants to add the video as favorite. In order to add the
     *  video as favorite, the user should have seen the video, if he did not see it display an
     *  error message. Another error is displayed when the user has already the video in favorites.
     *  If the conditions above are met, display a succes message.
     */
    public JSONObject addFavorite(final fileio.ActionInputData favourite,
                                  final List<UserInputData> users,
                                  final fileio.Writer fileWriter)
            throws IOException {

        //  Action fields we will need to perform adding a favorite
        String searchUser = favourite.getUsername();
        String searchTitle = favourite.getTitle();
        //  The id and message fieldWrite will add to resultArray
        int id = favourite.getActionId();
        String message = "";

        //  Searching for the user we need to add a favorite movie
        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if  (user.getFavoriteMovies().contains(searchTitle))    {
                    //  If the title we search is already marked as favorite, display this error
                    //  message
                    message = "error -> " + searchTitle + " is already in favourite list";
                }   else if (user.getHistory().containsKey(searchTitle))    {
                    //  If the title we search is marked as seen, add the title to favorite list
                    //  and display success
                    user.getFavoriteMovies().add(searchTitle);
                    message = "success -> " + searchTitle + " was added as favourite";
                }   else    {
                    //  If the title we search is not marked as seen, display this error message
                    message = "error -> " + searchTitle + " is not seen";
                }
                //  End searching after adding a view.
                break;
            }
        }

        //  Create the json object with the data written
        return fileWriter.writeFile(id, null, message);
    }

}
