package commands;

import fileio.UserInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class ExecuteView  {

    /**
     * addView
     */
    public JSONObject addView(final fileio.ActionInputData view,
                              final List<UserInputData> users,
                              final fileio.Writer fileWriter) throws IOException {

        //  Action fields we will need to perform adding a view
        String searchUser = view.getUsername();
        String searchTitle = view.getTitle();
        //  The id and message fieldWrite will add to resultArray
        int id = view.getActionId();
        String message;
        int numViews = 0;
        //  Searching for the user we need to add the viewed movie

        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if  (!user.getHistory().containsKey(searchTitle))   {
                    //  If the user didn't see the title, add it in history with 1 view
                    user.getHistory().put(searchTitle, 1);
                    numViews = 1;
                }   else    {
                    //  If the user saw the title increment num of views
                    numViews = user.getHistory().get(searchTitle) + 1;
                    user.getHistory().put(searchTitle, numViews);
                }
            }
        }
        //  Return message
        message = "success -> " + searchTitle + " was viewed with total views of " + numViews;
        return fileWriter.writeFile(id, null, message);
    }



}
