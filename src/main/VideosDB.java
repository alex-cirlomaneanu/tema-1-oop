package main;

import commands.ExecuteFavorite;
import commands.ExecuteRating;
import commands.ExecuteView;
import common.Constants;
import org.json.simple.JSONObject;
import queries.ExecuteActorsAwardsQuery;
import queries.ExecuteActorsAverageQuery;
import queries.ExecuteActorsDescriptionQuery;
import queries.ExecuteMoviesQuery;
import queries.ExecuteShowsQuery;
import queries.ExecuteUsersQuery;
import recommendations.BestUnseen;
import recommendations.SearchRecommendation;
import recommendations.FavoriteRecommendation;
import recommendations.PopularRecommendation;
import recommendations.StandardRecommendation;

import java.io.IOException;
import java.util.List;

public class VideosDB {

    /**
     *
     * @param action the action that will be executed
     * @param inputUsers the list of users given in input
     * @param inputActors the list of actors given in input
     * @param inputMovies the list of movies given in input
     * @param inputShows the list of shows given in input
     * @param fileWriter transforms the output in a JSONObject
     * @return actionResult the JSONObject that will be added in resultArray
     * @throws IOException in case of exceptions to reading / writing
     *
     *  The videosDB method will perform any action a user can do: commands (favorite, view,
     *  rating), queries (users, actors, videos) and recommendations (for standard and premium
     *  users). The program switches options according to the given action type.
     */
    public JSONObject videosDB(final fileio.ActionInputData action,
                               final List<fileio.UserInputData> inputUsers,
                               final List<fileio.ActorInputData> inputActors,
                               final List<fileio.MovieInputData> inputMovies,
                               final List<fileio.SerialInputData> inputShows,
                               final fileio.Writer fileWriter) throws IOException {

        JSONObject actionResult = new JSONObject();
        switch (action.getActionType()) {
            //  Case action is a command (favorite, view, rating)
            case Constants.COMMAND -> {
                //  actionResult is the object that will be written in the result array
                actionResult = switch (action.getType()) {
                    case Constants.FAVORITE ->
                            new ExecuteFavorite().addFavorite(action, inputUsers, fileWriter);
                    case Constants.VIEW -> new ExecuteView().addView(action, inputUsers,
                            fileWriter);
                    case Constants.RATING ->  new ExecuteRating().addRating(
                            action, inputUsers, inputMovies, inputShows, fileWriter);
                    default -> actionResult;
                };
            }

            //  Case action is a query (users, actors, videos)
            case Constants.QUERY -> {
                //  Users query
                if (action.getObjectType().equals(Constants.USERS)) {
                    actionResult = new ExecuteUsersQuery().queryUsers(
                            action, inputUsers, fileWriter);
                }

                //  Actors query by average rating, awards and description.
                if (action.getObjectType().equals(Constants.ACTORS)) {
                    actionResult = switch (action.getCriteria()) {
                        case Constants.AVERAGE ->
                                new ExecuteActorsAverageQuery().queryActorsAverage(
                                        action, inputActors, inputMovies, inputShows, fileWriter);
                        case Constants.AWARDS ->
                                new ExecuteActorsAwardsQuery().queryActorsAwards(
                                        action, inputActors, fileWriter);
                        case Constants.FILTER_DESCRIPTIONS ->
                                new ExecuteActorsDescriptionQuery().queryActorsWords(
                                        action, inputActors, fileWriter);
                        default -> actionResult;
                    };
                }
                //  Movies query (the type will be discussed in queryMovies() method)
                if (action.getObjectType().equals(Constants.MOVIES)) {
                    actionResult = new ExecuteMoviesQuery().queryMovies(
                            action, inputMovies, inputUsers, fileWriter);
                }
                //  Shows query (the type will be discussed in queryShows() method)
                if (action.getObjectType().equals(Constants.SHOWS)) {
                    actionResult = new ExecuteShowsQuery().queryShows(
                            action, inputShows, inputUsers, fileWriter);
                }
            }
            case Constants.RECOMMENDATION -> {
                //  Recommendations by type: standard, best unseen, populat, favorite and search.
                actionResult = switch (action.getType()) {
                    case Constants.STANDARD ->
                            new StandardRecommendation().giveStandardRecommendation(
                            action, inputMovies, inputShows, inputUsers, fileWriter);
                    case Constants.BEST_UNSEEN ->
                            new BestUnseen().giveBestUnseenRecommendation(
                            action, inputMovies, inputShows, inputUsers, fileWriter);
                    case Constants.POPULAR ->
                            new PopularRecommendation().givePopularRecommendation(
                            action, inputMovies, inputShows, inputUsers, fileWriter);
                    case Constants.FAVORITE -> new
                            FavoriteRecommendation().getFavoriteRecommendation(
                            action, inputMovies, inputShows, inputUsers, fileWriter);
                    case Constants.SEARCH ->
                            new SearchRecommendation().getSearchRecommendation(
                            action, inputMovies, inputShows, inputUsers, fileWriter);
                    default -> actionResult;
                };
            }
            default ->
                    //  Default case, if the action given is unknown.
                    throw new IllegalStateException("Unexpected value: " + action.getActionType());
        }
        //  Return the JSONObject created by action.
        return actionResult;
    }
}
