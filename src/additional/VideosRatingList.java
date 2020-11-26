package additional;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideosRatingList {
    /**
     *
     * @param genre a given genre
     * @param movies all movies
     * @param shows all shows
     * @return a sorted list of all videos belonging to the given genre.
     *
     *  This method searches all the videos belonging to a genre and sorts them in descending order
     *  by ratings.
     */
    public List<String> sortAllVideos(final String genre,
                                      final List<MovieInputData> movies,
                                      final List<fileio.SerialInputData> shows) {
        //  Map of video title as key and rating as value
        Map<String, Double> ratedVideos = new HashMap<>();
        // Every video that has a not null rating is out in the map.
        for (fileio.MovieInputData movie : movies) {
            if (movie.getGenres().contains(genre)) {
                if (movie.getRating() > 0) {
                    ratedVideos.put(movie.getTitle(), movie.getRating());
                }
            }
        }
        for (fileio.SerialInputData show : shows) {
            if (show.getGenres().contains(genre)) {
                double showRating = new ShowRatingCalculator().findShowRating(show);
                ratedVideos.put(show.getTitle(), showRating);
            }
        }

        //  List of map entries used for sorting by rating and by name in case of equal ratings.
        List<Map.Entry<String, Double>> allVideosSorted = new ArrayList<>(ratedVideos.entrySet());
        allVideosSorted.sort((firstGenre, secondGenre) -> {
            int result;
            result = Double.compare(firstGenre.getValue(), secondGenre.getValue());
            if (result == 0) {
                result = firstGenre.getKey().compareTo(secondGenre.getKey());
            }
            return result;
        });

        // The final list of video titles that will be returned. In this list will be added every
        // title (map key) from the previous list of map entries.
        List<String> videosRatingList = new ArrayList<>();
        for (Map.Entry<String, Double> video : allVideosSorted) {
            videosRatingList.add(video.getKey());
        }
        return videosRatingList;
    }
}
