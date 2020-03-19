package es.ulpgc.twitterrdf;

import static es.ulpgc.twitterrdf.TwitterHandler.getTimeLine;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;

public class Main {
    public static void main (String [ ] args) {
        List<String> tweets;
        try {
            tweets = getTimeLine();
            for (String tweet : tweets){
            System.out.println(tweet); 
            }
        } catch (TwitterException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}