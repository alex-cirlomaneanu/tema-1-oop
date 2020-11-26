package queries;

import common.Constants;
import fileio.ActorInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActorsDescriptionQuery {
    /**
     *
     * @param query query given to execute
     * @param actors list of actors
     * @param fileWriter transforms the output in a JSONObject
     * @return JSONObject result of the query
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method does the actors query by their description. Firstly it searches the actors that
     *  have a description that matches all the words searched by query. If there are found actors
     *  that respect this criteria, proceed to the actors by their name. Return the found actors in
     *  ascending or descending order.
     */
    public JSONObject queryActorsWords(final fileio.ActionInputData query,
                                        final List<ActorInputData> actors,
                                        final fileio.Writer fileWriter) throws IOException  {

        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        //  The words to be search in actors
        List<String> allWords = query.getFilters().get(Constants.WORDS_INDEX);
        List<fileio.ActorInputData> searchActors = new ArrayList<>();

        for (fileio.ActorInputData actor : actors)  {
            //  Search actors that have all the words
            boolean hasWordInDescription = true;
            for (String word : allWords)   {
                if  (!actor.getCareerDescription().toLowerCase().contains(word))  {
                    hasWordInDescription = false;
                    break;
                }
            }
            if (hasWordInDescription)   {
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
           if (i != (listLength - 1)) {
               aux.append(", ");
           }
       }
       aux.append("]");
       message += aux.toString();
       //  Return message
       return fileWriter.writeFile(id, null, message);
    }
}
