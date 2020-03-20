package es.ulpgc.twitterrdf;

import static es.ulpgc.twitterrdf.TwitterHandler.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.TwitterException;

public class Main {
    public static void main (String [ ] args) {
        List<Status> results;
        try {
            results = searchTweets("Animal Crossing");
            for (Status status : results) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            }
        } catch (TwitterException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}