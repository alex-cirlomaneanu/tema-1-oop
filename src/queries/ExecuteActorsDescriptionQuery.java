package queries;

import common.Constants;
import fileio.ActorInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExecuteActorsDescriptionQuery {
    /**
     * query by actors
     *
     */
    public JSONObject queryActorsWords(final fileio.ActionInputData query,
                                        final List<ActorInputData> actors,
                                        final fileio.Writer fileWriter) throws IOException  {

        //  id and message will be displayed in result array using fileWriter
        int id = query.getActionId();
        String message = "Query result: [";
        //  aux will help building the message
        StringBuilder aux = new StringBuilder();
        List<fileio.ActorInputData> searchActors = new ArrayList<>();

        for (fileio.ActorInputData actor : actors)  {
            boolean hasWordInDescription = true;
            for (String word : query.getFilters().get(Constants.WORDS_INDEX))   {
                if  (!actor.getCareerDescription().toLowerCase().contains(word))  {
                    hasWordInDescription = false;
                    break;
                }
            }
            if (hasWordInDescription)   {
                searchActors.add(actor);
            }
        }
        Collections.sort(searchActors, new Comparator<>() {
            @Override
            public int compare(final fileio.ActorInputData actor1,
                               final fileio.ActorInputData actor2) {
                //  Ascending order
                int result;
                result = actor1.getName().compareTo(actor2.getName());
                return result;
            }
        });

        int listLength = Math.min(searchActors.size(), query.getNumber());

        if  (query.getSortType().equals(Constants.ASC)) {
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchActors.get(i).getName());
                if (i != (listLength - 1))    {
                    aux.append(", ");
                }
            }
        }   else if (query.getSortType().equals(Constants.DESC)) {
            Collections.reverse(searchActors);
            for (int i = 0; i < listLength; ++i) {
                aux.append(searchActors.get(i).getName());
                if (i != (listLength - 1))    {
                    aux.append(", ");
                }
            }
        }
        aux.append("]");
        message += aux.toString();
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }
}
