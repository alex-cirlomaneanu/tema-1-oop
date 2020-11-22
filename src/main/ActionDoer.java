package main;

import fileio.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class ActionDoer {

    public JSONObject addFavoirte(ActionInputData givenAction, List<UserInputData> users,
                                  Writer fileWriter) throws IOException {
        //  Action fields we will need to perform adding a favorite
        String searchUser = givenAction.getUsername();
        String searchTitle = givenAction.getTitle();
        //  The id and message fieldWrite will add to resultArray
        int id = givenAction.getActionId();
        String message = "";
        //  Searching for the user we need to add a favorite movie
        for(UserInputData user : users){
            if(searchUser.equals(user.getUsername())){
                if(user.getFavoriteMovies().contains(searchTitle)){
                    message = "error -> " + searchTitle + " is already in favourite list";
                }
                else if(user.getHistory().containsKey(searchTitle)){
                    user.getFavoriteMovies().add(searchTitle);
                    message = "success -> " + searchTitle + " was added as favourite";
                }
                else{
                    message = "error -> " + searchTitle + " is not seen";
                }
            }
        }
        JSONObject jsonObject = fileWriter.writeFile(id, null, message);
        return jsonObject;
    }
}
