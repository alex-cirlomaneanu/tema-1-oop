package additional;

import java.util.List;

public class FindViewsNumber {
    /**
     *
     * @param movie we want to calculate total views number
     * @param users the list of users
     * @return total views number
     *
     *  Function used to help comparing movies by how many times a movie was marked as "viewed"
     */
    public int findViewsMovie(final fileio.MovieInputData movie,
                              final List<fileio.UserInputData> users) {
        int viewsNum = 0;
        for (fileio.UserInputData user : users)  {
            if  (user.getHistory().containsKey(movie.getTitle())) {
                viewsNum += user.getHistory().get(movie.getTitle());
            }
        }
        return viewsNum;
    }

    /**
     *
     * @param show we want to calculate total views number
     * @param users the list of users
     * @return total views number
     *
     *  Function used to help comparing shows by how many times a show was marked as "viewed"
     */
    public int findViewsShow(final fileio.SerialInputData show,
                             final List<fileio.UserInputData> users) {
        int viewsNum = 0;
        for (fileio.UserInputData user : users)  {
            if  (user.getHistory().containsKey(show.getTitle())) {
                viewsNum += user.getHistory().get(show.getTitle());
            }
        }
        return viewsNum;
    }
}
