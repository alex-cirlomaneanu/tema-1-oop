package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import org.json.simple.JSONArray;

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
        fileio.InputLoader inputLoader = new fileio.InputLoader(filePath1);
        fileio.Input input = inputLoader.readData();

        fileio.Writer fileWriter = new fileio.Writer(filePath2);
        JSONArray arrayResult = new JSONArray();
        //        Read all data from input.
        List<fileio.UserInputData> inputUsers = input.getUsers();
        List<fileio.ActionInputData> inputActions = input.getCommands();
        List<fileio.ActorInputData> inputActors = input.getActors();
        List<fileio.MovieInputData> inputMovies = input.getMovies();
        List<fileio.SerialInputData> inputShows = input.getSerials();
        //        Proceed to execute every action given one by one using an additional class.
        for (fileio.ActionInputData action : inputActions) {
            //            Execute all actions given in input by their type.
            arrayResult.add(new VideosDB().videosDB(
                    action,
                    inputUsers,
                    inputActors,
                    inputMovies,
                    inputShows,
                    fileWriter));
            }


        fileWriter.closeJSON(arrayResult);
    }
}
