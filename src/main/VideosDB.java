package main;

import commands.ExecuteFavorite;
import commands.ExecuteRating;
import commands.ExecuteView;
import common.Constants;
import org.json.simple.JSONObject;
import queries.*;

import java.io.IOException;
import java.util.List;

public class VideosDB {

    /**
     * execute every action
     *
     */
    public JSONObject videosDB(final fileio.ActionInputData action,
                               final List<fileio.UserInputData> inputUsers,
                               final List<fileio.ActorInputData> inputActors,
                               final List<fileio.MovieInputData> inputMovies,
                               final List<fileio.SerialInputData> inputSerials,
                               final fileio.Writer fileWriter) throws IOException {

        JSONObject actionResult = new JSONObject();
        switch (action.getActionType()) {
            //  Case actiion is a command (favorite, view, rating)
            case Constants.COMMAND:
                //  comandResult is the object that will be written in the result array
                if (action.getType().equals(Constants.FAVORITE)) {
                    actionResult = new ExecuteFavorite().addFavorite(
                            action,
                            inputUsers,
                            fileWriter);
                }

                if (action.getType().equals(Constants.VIEW)) {
                    actionResult = new ExecuteView().addView(action, inputUsers,
                            fileWriter);
                }

                if (action.getType().equals(Constants.RATING)) {
                    actionResult = new ExecuteRating().addRating(
                            action, inputUsers, inputMovies, inputSerials, fileWriter);
                }

                break;

            //  Case actiion is a query (users, actors, videos)
            case Constants.QUERY:
                //  actionResult is the object that will be written in the result array
                //  Users query
                if (action.getObjectType().equals(Constants.USERS)) {
                    actionResult = new ExecuteUsersQuery().queryUsers(
                            action, inputUsers, fileWriter);
                }

                //  Actors query by average rating
                if (action.getObjectType().equals(Constants.ACTORS)) {
                    actionResult = switch (action.getCriteria()) {
                        case Constants.AVERAGE ->
                                new ExecuteActorsAverageQuery().queryActorsAverage(
                                        action, inputActors, inputMovies, inputSerials, fileWriter);
                        case Constants.AWARDS ->
                                new ExecuteActorsAwardsQuery().queryActorsAwards(
                                        action, inputActors, fileWriter);
                        case Constants.FILTER_DESCRIPTIONS ->
                                new ExecuteActorsDescriptionQuery().queryActorsWords(
                                        action, inputActors, fileWriter);
                        default -> actionResult;
                    };
                }
                if (action.getObjectType().equals(Constants.MOVIES)) {
                    actionResult = new ExecuteMoviesQuery().queryMovies(
                            action, inputMovies, inputUsers, fileWriter);
                }
                if (action.getObjectType().equals(Constants.SHOWS)) {
                    actionResult = new ExecuteSerialsQuery().querySerials(
                            action, inputSerials, inputUsers, fileWriter);
                }
                break;
            default:
                break;
        }
        return actionResult;
    }
}
