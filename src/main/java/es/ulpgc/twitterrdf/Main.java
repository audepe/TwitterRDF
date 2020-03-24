package es.ulpgc.twitterrdf;

import static es.ulpgc.twitterrdf.TwitterHandler.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.TwitterException;

public class Main {
    public static void main (String args[]) {
    }
    
    public static String getFormattedTweets(String search_term, int n_tweets){
        String res = "";
        try {
            List<Status> results;
            results = searchTweets(search_term, n_tweets);
            for (Status status : results) {
                res += "@" + status.getUser().getScreenName() + ":" + status.getText() + "\n";
                res += "*****************\n";
            }
        } catch (TwitterException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
}