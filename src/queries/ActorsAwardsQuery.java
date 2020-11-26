package queries;

import common.Constants;
import fileio.ActorInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class ActorsAwardsQuery {
    /**
     *
     * @param query query given to execute
     * @param actors list of actors
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method does the actors query by their awards. Firstly it searches the actors that have
     *  all the awards searched. If there are found actors that respect this criteria, proceed to
     *  the actors by their name. Return the found actors in ascending or descending order.
     */
    public JSONObject queryActorsAwards(final fileio.ActionInputData query,
                                         final List<ActorInputData> actors,
                                         final fileio.Writer fileWriter) throws IOException {
        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  The awards to be search in actors
        List<String> allAwards = query.getFilters().get(Constants.AWARDS_INDEX);
        List<fileio.ActorInputData> searchActors = new ArrayList<>();

        for (fileio.ActorInputData actor : actors)  {
            //  Search actors that have all the awards
            boolean hasAward = true;
            for (String award : allAwards)   {
                if (actor.getAwards().containsKey(award)) {
                    continue;
                }
                hasAward = false;
                break;
            }
            if (hasAward)   {
                searchActors.add(actor);
            }
        }

        //  Sort actors by their name
        searchActors.sort((actor1, actor2) -> {
            int result;
            result = actor1.getName().compareTo(actor2.getName());
            return result;
        });

        //  Use the minimum number of actors between the given number and the actual list length
        int listLength = Math.min(searchActors.size(), query.getNumber());
        //   Reverse the list in order to return the list in descending order.
        if (query.getSortType().equals(Constants.DESC)) {
            Collections.reverse(searchActors);
        }

        //   Add the found actors to the message.
        for (int i = 0; i < listLength; ++i) {
            aux.append(searchActors.get(i).getName());
            if (i != (listLength - 1))    {
                aux.append(", ");
            }
        }
        aux.append("]");
        message += aux.toString();
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
