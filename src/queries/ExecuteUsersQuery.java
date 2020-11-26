package queries;

import common.Constants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExecuteUsersQuery {
    /**
     *
     * @param query query given to execute
     * @param users list of users
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method does the users query given in input. Firstly it searches the users that have
     *  given at least one rating. If there are found users that respect this criteria, proceed
     *  to sort the users according to the number of times they rated a video. Return the found
     *  users in ascending or descending order.
     */
    public JSONObject queryUsers(final fileio.ActionInputData query,
                                 final List<fileio.UserInputData> users,
                                 final fileio.Writer fileWriter) throws IOException {

        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        List<fileio.UserInputData> activeUsers = new ArrayList<>();

        for (fileio.UserInputData user : users) {
            //  Search all users that gave at least one rating and add them to the list
           if   (user.getRatingNumber() > 0)   {
               activeUsers.add(user);
           }
        }

        //  Sort the active users by the number of ratings they gave with lambda function.
        activeUsers.sort((firstUser, secondUser) -> {
            int result = Integer.compare(firstUser.getRatingNumber(), secondUser.getRatingNumber());
            if (result == 0) {
                // Alphabetical sort in case the number of ratings given are the same.
                result = firstUser.getUsername().compareTo(secondUser.getUsername());
            }
            return result;
        });

       //  Use the minimum number of actors between the given number and the actual list length
       int listLength = Math.min(activeUsers.size(), query.getNumber());

       //   Reverse the list in order to return the list in descending order.
       if (query.getSortType().equals(Constants.DESC)) {
            Collections.reverse(activeUsers);
       }
       //   Add the found users to the message.
       for (int i = 0; i < listLength; ++i) {
           aux.append(activeUsers.get(i).getUsername());
           if (i != (activeUsers.size() - 1))    {
               aux.append(", ");
           }
       }
       aux.append("]");
       message += aux.toString();
       //  Create the json object with the data written
       return fileWriter.writeFile(id, null, message);
    }
}
