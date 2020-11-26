# tema-1-oop 
Cirlomaneanu Alexandru 324CD

   To solve the homework I firstly read all the input data in main. Then for 
   any given action I proceed to execute via a method that can execute any type
   of actions.
   
   This method is implemented using a switch that has cases for every action 
   (command, query, recommendation). Every type of action is executed using its
   own method located in a specific made class.
   
   1. Commands
   
   a. Favorite
   
   To add a video to favorite for a user I search the user in the list of 
   users. I check if the video is seen, else I return an error message. If
   the video is seen I check it is not already a favorite video. In case the
   video is not yet in favorite I return a success message, otherwise it is
   returned another error message.
   
   b. View
   
   I search the user that wants to see the video. After I find the user I want
   to see if he saw this video. If he did saw I increment the existing number 
   of views. If he did not I put the new video in his history with 1 view.
   
   c. Rating
   
   I search the user that wants to rate the video. When I find the user, the 
   video that he wants to rate must be in his history. If it is not I return 
   an error message. If it is I check that he did not already gave the video a
   rating. I add a new rating to the video, modify the existing one and 
   increment the number of ratigs the user gave.
   
   2. Queries
   
   a. Users query
   
   I search all users that gave at least one rating and put them in a list. 
   I sort the list by the number of given ratings (in ascending order). I 
   return the query result by this list (unmodified if ascending order is 
   requiered, or reversed for descending oreder).
   
   b. Actors query
   
   There I got 3 casee: average rating query, awards query, profile description
   query. In all cases I begin by searching the proper actors and adding them 
   to a list. For average rating I search the actors that have a rating more 
   than 0, for awards search the actors that have all asked awards and for 
   profile description the actors that match all asked words. Then I sort the 
   resulted list (in ascending order) according to the given criteria and 
   return it unmodified or reverse order (for ascending or descening oreder).
   
   c. Movies & Shows query
   
   For both types of video I use a similliar approach. Here I first have to 
   match the video with the asked year and/or genre. The suitable videos are
   added to a list. After finding all the good videos I use a switch to sort
   them according to the given criteria (ratings, longest, favorite, most 
   viewed). For ratings, I simply compare movies ratings or, I calculate (using 
   an additional class and method) and then compare shows ratings. For longest
   I use the same way as above. For favorite and most viewed I use additional
   methods to calculate the number of occurrences in favorite list, or the 
   total number of views for a video. 
   
   3. Recommendation
   
   a. Standard
   
   For this type of recommendation I firstly search the user that wants to
   receive it. Then I proceed to search the first unseen video (first in the
   movie list, the in the shows list). I returned the first unseen video I 
   found, or an error message if I find none.
   
   b. Best Unseen
   
   I build a list of the movies the user did not see and a list of the unseen
   shows. I sort them by ratings, and I return: the top rated movie (if top 
   rated movies list is not empty), the top rated show (if cannot find a top 
   rated movie), or an error message (if I cannot find a top rated show either)
   
   c. Favorite
   
   I generate the lists of the most favorite movies and most favorite shows,
   respectively (by summing how many times they appear in a favorite list). 
   Then I search the user and assure he has a premium subscription. I search 
   then a video that was not seen first in the movies list, then in the show 
   list (if I do not find an unseen movie). Return then the first unseen video
   or message of error.
      
   d. Popular
   
   I start by building a list of the most popular genres by summing all views
   a genre has in movies and shows. Then I search the user and assure he has a
   premium subscription. I put all unseen titles in a list and search the first
   title of the most popular genre. If all titles belonging to a genre are seen
   I search a title belonging to the next most popluar genre and return it or
   a message of error.
   
   e. Search
   
   I generate a list of the top rated videos (both movies and shows) that 
   contain the given genre. Then I search the user and assure he has a premium
   subscription. After that I return all unseen titles of the given genre or an
   error message if this recommendation can not be applied.  
   
