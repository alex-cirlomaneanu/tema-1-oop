package main;

import entertainment.Season;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public final class ActionDoer {

    /**
     * addFavorite
     */
    public JSONObject addFavourite(final fileio.ActionInputData favourite,
                                   final List<fileio.UserInputData> users,
                                   final fileio.Writer fileWriter)
            throws IOException  {
        //  Action fields we will need to perform adding a favorite
        String searchUser = favourite.getUsername();
        String searchTitle = favourite.getTitle();
        //  The id and message fieldWrite will add to resultArray
        int id = favourite.getActionId();
        String message = "";
        //  Searching for the user we need to add a favorite movie
        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if  (user.getFavoriteMovies().contains(searchTitle))    {
                    //  If the title we search is already marked as favorite, display this error
                    //  message
                    message = "error -> " + searchTitle + " is already in favourite list";
                }   else if (user.getHistory().containsKey(searchTitle))    {
                    //  If the title we search is marked as seen, add the title to favorite list
                    //  and display success
                    user.getFavoriteMovies().add(searchTitle);
                    message = "success -> " + searchTitle + " was added as favourite";
                }   else    {
                    //  If the title we search is not marked as seen, display this error message
                    message = "error -> " + searchTitle + " is not seen";
                }
            }
        }
        //  Create the json object with the data written
        JSONObject jsonObject = fileWriter.writeFile(id, null, message);
        return jsonObject;
    }

    /**
     * addView
     */
    public JSONObject addView(final fileio.ActionInputData view,
                              final List<fileio.UserInputData> users,
                              final fileio.Writer fileWriter) throws IOException   {
        //  Action fields we will need to perform adding a view
        String searchUser = view.getUsername();
        String searchTitle = view.getTitle();
        //  The id and message fieldWrite will add to resultArray
        int id = view.getActionId();
        String message = "";
        int numViews = 0;
        //  Searching for the user we need to add the viewed movie
        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if  (!user.getHistory().containsKey(searchTitle))   {
                    //  If the user didn't see the title, add it in history with 1 view
                    user.getHistory().put(searchTitle, 1);
                    numViews = 1;
                }   else    {
                    //  If the user saw the title increment num of views
                    numViews = user.getHistory().get(searchTitle) + 1;
                    user.getHistory().put(searchTitle, numViews);
                }
            }
        }
        //  Return message
        message = "success -> " + searchTitle + " was viewed with total views of " + numViews;
        JSONObject jsonObject = fileWriter.writeFile(id, null, message);
        return jsonObject;
    }

    /**
     * addRating
     */
    public JSONObject addRating(final fileio.ActionInputData rate,
                              final List<fileio.UserInputData> users,
                              final List<fileio.MovieInputData> movies,
                              final List<fileio.SerialInputData> shows,
                              final fileio.Writer fileWriter) throws IOException   {
        //  Action fields we will need to perform adding a view
        String searchUser = rate.getUsername();
        String searchTitle = rate.getTitle();
        //  The id and message fieldWrite will add to resultArray
        int id = rate.getActionId();
        String message = "";
        //  Searching for the user we need to add the viewed movie
        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if  (user.getRatedMovies().contains(searchTitle))   {
                    //  If the user already rated the movie error
                    message = "error -> " + searchTitle + " is already rated";
                }   else    {
                    //  Add the new title to rate movies
                    user.getRatedMovies().add(searchTitle);
                    //  Search for the movie
                    for (fileio.MovieInputData movie : movies) {
                        if (movie.getTitle().equals(searchTitle))  {
                            //  Change the rating of the movie
                            float ratingSum = (movie.getRating() * movie.getNumRating());
                            ratingSum +=  rate.getGrade();
                            movie.setNumRating(movie.getNumRating() + 1);
                            float newRating;
                            newRating = ratingSum / (movie.getNumRating());
                            movie.setRating(newRating);
                            //  Success message
                            message = "success -> "
                                    + searchTitle
                                    + " was rated with "
                                    + rate.getGrade()
                                    + " by "
                                    + user.getUsername();
                            JSONObject jsonObject = fileWriter.writeFile(id, null, message);
                            return jsonObject;
                        }
                    }
                }
            }
        }
        //  Searching for the user we need to add the viewed show season
        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if  (user.getRatedShows().contains(searchTitle + rate.getSeasonNumber()))  {
                    //  If the user already rated the season
                    message = "error -> " + searchTitle + " is already rated";
                }   else    {
                    //  Add the new title to rated show seasons
                    user.getRatedMovies().add(searchTitle + rate.getSeasonNumber());
                    //  Search for the show
                    for (fileio.SerialInputData show : shows) {
                        if (show.getTitle().equals(searchTitle))  {
                            //  Change the rating of the show
                            Season searchSeason;
                            searchSeason = show.getSeasons().get(rate.getSeasonNumber() - 1);
                            //  Change the rating of the movie
                            float ratingSum;
                            ratingSum = (searchSeason.getRating() * searchSeason.getNumRating());
                            ratingSum +=  rate.getGrade();
                            searchSeason.setNumRating(searchSeason.getNumRating() + 1);
                            float newRating;
                            newRating = ratingSum / (searchSeason.getNumRating());
                            searchSeason.setRating(newRating);
                            //  Success message
                            message = "success -> "
                                    + searchTitle
                                    + " was rated with "
                                    + rate.getGrade()
                                    + " by "
                                    + user.getUsername();
                            JSONObject jsonObject = fileWriter.writeFile(id, null, message);
                            return jsonObject;
                        }
                    }
                }
            }
        }
        //  Return message
        JSONObject jsonObject = fileWriter.writeFile(id, null, message);
        return jsonObject;
    }

}
