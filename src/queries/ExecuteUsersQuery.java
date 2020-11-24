package queries;

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
public class ExecuteUsersQuery {
    /**
     * query by users activity
     *
     */
    public JSONObject queryUsers(final fileio.ActionInputData query,
                                 final List<fileio.UserInputData> users,
                                 final fileio.Writer fileWriter) throws IOException {

        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  In this list I will put the users we find
        List<fileio.UserInputData> activeUsers = new ArrayList<>();

        for (fileio.UserInputData user : users) {
            //  Search all users that gave at least one rating and add them to the list
           if   (user.getRatingNumber() > 0)   {
               activeUsers.add(user);
           }
        }

        Collections.sort(activeUsers, new Comparator<>() {
            @Override
            public int compare(final fileio.UserInputData user1,
                               final fileio.UserInputData user2) {
                //  Ascending order
                int result = Integer.compare(user1.getRatingNumber(), user2.getRatingNumber());
                if (result == 0) {
                    result = user1.getUsername().compareTo(user2.getUsername());
                    //avem nevoie alfabetic
                }
                return result;
            }
        });

        int listLength = Math.min(activeUsers.size(), query.getNumber());

        if  (query.getSortType().equals(Constants.ASC)) {
            for (int i = 0; i < listLength; ++i) {
                aux.append(activeUsers.get(i).getUsername());
                if (i != (activeUsers.size() - 1))    {
                    aux.append(", ");
                }
            }
        }   else if (query.getSortType().equals(Constants.DESC)) {
            Collections.reverse(activeUsers);
            for (int i = 0; i < listLength; ++i) {
                aux.append(activeUsers.get(i).getUsername());
                if (i != (activeUsers.size() - 1))    {
                    aux.append(", ");
                }
            }
        }
        aux.append("]");
        message += aux.toString();
        //  Create the json object with the data written
        return fileWriter.writeFile(id, null, message);
    }
}
