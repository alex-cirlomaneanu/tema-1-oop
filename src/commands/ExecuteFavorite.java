package commands;

import fileio.UserInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class ExecuteFavorite {
    /**
     * addFavorite
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
            }
        }

        //  Create the json object with the data written
        return fileWriter.writeFile(id, null, message);
    }

}
