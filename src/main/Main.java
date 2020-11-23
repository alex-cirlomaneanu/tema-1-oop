package main;

import checker.Checkstyle;
import checker.Checker;
import commands.ExecuteFavorite;
import commands.ExecuteRating;
import commands.ExecuteView;
import common.Constants;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import queries.ExecuteActorsAverageQuery;
import queries.ExecuteActorsAwardsQuery;
import queries.ExecuteActorsDescriptionQuery;
import queries.ExecuteUsersQuery;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        List<UserInputData> inputUsers = input.getUsers();
        List<ActionInputData> inputActions = input.getCommands();
        List<ActorInputData> inputActors = input.getActors();
        List<MovieInputData> inputMovies = input.getMovies();
        List<SerialInputData> inputSerials = input.getSerials();
        for (ActionInputData action : inputActions) {
            /**
             * Execute all actions given in input by their type
             * */
            switch (action.getActionType()) {
                //  Case actiion is a command (favorite, view, rating)
                case Constants.COMMAND:
                    //  comandResult is the object that will be written in the result array
                    JSONObject commandResult = new JSONObject();
                    if (action.getType().equals(Constants.FAVORITE)) {
                        commandResult = new ExecuteFavorite().addFavorite(
                                action,
                                inputUsers,
                                fileWriter);
                    }

                    if  (action.getType().equals(Constants.VIEW)) {
                        commandResult = new ExecuteView().addView(action, inputUsers, fileWriter);
                    }

                    if (action.getType().equals(Constants.RATING))  {
                        commandResult = new ExecuteRating().addRating(
                                action, inputUsers, inputMovies, inputSerials, fileWriter);
                    }

                    arrayResult.add(commandResult);
                    break;

                //  Case actiion is a query (users, actors, videos)
                case Constants.QUERY:
                    //  queryResult is the object that will be written in the result array
                    JSONObject queryResult = new JSONObject();
                    //  Users query
                    if (action.getObjectType().equals(Constants.USERS)) {
                        queryResult = new ExecuteUsersQuery().queryUsers(action,
                                inputUsers,
                                fileWriter);
                    }

                    //  Actors query by average rating
                    if (action.getObjectType().equals(Constants.ACTORS)
                            && (action.getCriteria().equals(Constants.AVERAGE)))    {
                        queryResult = new ExecuteActorsAverageQuery().queryActorsAverage(action,
                                inputActors,
                                inputMovies,
                                inputSerials,
                                fileWriter);
                    }
                    if (action.getObjectType().equals(Constants.ACTORS)
                            && (action.getCriteria().equals(Constants.AWARDS)))    {
                        queryResult =  new ExecuteActorsAwardsQuery().queryActorsAwards(action,
                                inputActors,
                                fileWriter);
                    }
                    if (action.getObjectType().equals(Constants.ACTORS)
                            && (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)))    {
                        queryResult =  new ExecuteActorsDescriptionQuery().queryActorsWords(action,
                                inputActors,
                                fileWriter);
                    }
                    arrayResult.add(queryResult);
                    break;
                case Constants.RECOMMENDATION:
                    break;
                default:
                    break;
            }

        }


        fileWriter.closeJSON(arrayResult);
    }
}
