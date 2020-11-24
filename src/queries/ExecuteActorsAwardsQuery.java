package queries;

import additional.AwardSum;
import common.Constants;
import fileio.ActorInputData;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class that has methods to solve the given query
 */
public class ExecuteActorsAwardsQuery {
    /**
     * query by actors
     *
     */
    public JSONObject queryActorsAwards(final fileio.ActionInputData query,
                                         final List<ActorInputData> actors,
                                         final fileio.Writer fileWriter) throws IOException {
        int id = query.getActionId();
        String message = "Query result: [";
        StringBuilder aux = new StringBuilder();
        List<fileio.ActorInputData> searchActors = new ArrayList<>();

        for (fileio.ActorInputData actor : actors)  {
            boolean hasAward = true;
            for (String award : query.getFilters().get(Constants.AWARDS_INDEX))   {
                if  (!actor.getAwards().containsKey(award))  {
                    hasAward = false;
                    break;
                }
            }
            if (hasAward)   {
                searchActors.add(actor);
            }
        }
        Collections.sort(searchActors, new Comparator<>() {
            @Override
            public int compare(final fileio.ActorInputData actor1,
                               final fileio.ActorInputData actor2) {
                //  Ascending order
                int result;
                int firstActorAwards = new AwardSum().awardSum(actor1);
                int secondActorAwards = new AwardSum().awardSum(actor2);
                result = Integer.compare(firstActorAwards, secondActorAwards);
                if (result == 0) {
                    //  Alphabetical order
                    result = actor1.getName().compareTo(actor2.getName());
                }
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
