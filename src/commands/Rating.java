package commands;

import entertainment.Season;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class Rating {
    /**
     *
     * @param rate the command given in input
     * @param users the list of users
     * @param movies the list of movies
     * @param shows the list of shows
     * @param fileWriter the tool that writes the output into JSONObject
     * @return the JSONObject result of rating the video
     * @throws IOException in case of exceptions to reading / writing
     *
     *  This method gives a rating to a video seen by user. First search the user, after search the
     *  the title of the video (movie or show) he wants to rate. When a rating is added the
     *  existing rating. For movies, the movie rating will be modified, for show the season rating
     *  will be modified.
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
                if  (!user.getHistory().containsKey(searchTitle))  {
                    message = "error -> " + searchTitle + " is not seen";
                    break;
                }   else {
                    if  (user.getRatedMovies().contains(searchTitle))   {
                        //  If the user already rated the movie error
                        message = "error -> " + searchTitle + " has been already rated";
                        break;
                    }   else    {
                        //  Add the new title to rate movies
                        user.getRatedMovies().add(searchTitle);
                        user.setRatingNumber(user.getRatingNumber() + 1);
                        //  Search for the movie
                        for (fileio.MovieInputData movie : movies) {
                            if (movie.getTitle().equals(searchTitle))  {
                                //  Change the rating of the movie
                                double ratingSum = (movie.getRating() * movie.getNumRating());
                                ratingSum +=  rate.getGrade();
                                movie.setNumRating(movie.getNumRating() + 1);
                                double newRating;
                                newRating = ratingSum / (movie.getNumRating());
                                movie.setRating(newRating);
                                //  Success message
                                message = "success -> "
                                        + searchTitle
                                        + " was rated with "
                                        + rate.getGrade()
                                        + " by "
                                        + user.getUsername();
                            }
                        }
                    }
                }
                //  End searching after adding the rating
                break;
            }
        }
        //  Searching for the user we need to add the viewed show season
        for (fileio.UserInputData user : users)    {
            if  (searchUser.equals(user.getUsername())) {
                if (!user.getHistory().containsKey(searchTitle))    {
                    message = "error -> " + searchTitle + " is not seen";
                    break;
                }   else    {
                    if  (user.getRatedShows().contains(searchTitle + rate.getSeasonNumber()))  {
                        //  If the user already rated the season
                        message = "error -> " + searchTitle + " has been already rated";
                        break;
                    }   else    {
                        //  Add the new title to rated show seasons
                        user.getRatedMovies().add(searchTitle + rate.getSeasonNumber());
                        //user.setRatingNumber(1 + user.getRatingNumber());
                        // Se prea poate sa dea eroare mai tarziu din cauza asta
                        //  Search for the show
                        for (fileio.SerialInputData show : shows) {
                            if (show.getTitle().equals(searchTitle))  {
                                //  Change the rating of the show
                                Season searchSeason;
                                searchSeason = show.getSeasons().get(rate.getSeasonNumber() - 1);
                                //  Change the rating of the movie
                                double ratingSum;
                                ratingSum = (searchSeason.getRating()
                                        * searchSeason.getNumRating());
                                ratingSum +=  rate.getGrade();
                                searchSeason.setNumRating(searchSeason.getNumRating() + 1);
                                double newRating;
                                newRating = ratingSum / (searchSeason.getNumRating());
                                searchSeason.setRating(newRating);
                                //  Success message
                                message = "success -> "
                                        + searchTitle
                                        + " was rated with "
                                        + rate.getGrade()
                                        + " by "
                                        + user.getUsername();
                            }
                        }
                    }
                }
                //  End searching after adding the rating
                break;
            }
        }
        //  Return message
        return fileWriter.writeFile(id, null, message);
    }

}
