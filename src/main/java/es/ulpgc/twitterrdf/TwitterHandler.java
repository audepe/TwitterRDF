package es.ulpgc.twitterrdf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
/**
 *
 * @author dadepe
 */
public class TwitterHandler {


    public static Twitter getTwitterinstance() {
        Twitter twitter = TwitterFactory.getSingleton();
	return twitter;
    }

    public static String createTweet(String tweet) throws TwitterException {
            Twitter twitter = getTwitterinstance();
            Status status = twitter.updateStatus(tweet);
            return status.getText();
    }

    public static List<String> getTimeLine() throws TwitterException {
            Twitter twitter = getTwitterinstance();
            List<Status> statuses = twitter.getHomeTimeline();
            return statuses.stream().map(
                            item -> item.getText()).collect(
                                            Collectors.toList());
    }

    public static String sendDirectMessage(String recipientName, String msg) throws TwitterException {
            Twitter twitter = getTwitterinstance();
            DirectMessage message = twitter.sendDirectMessage(recipientName, msg);
            return message.getText();
    }

    public static List<Status> searchTweets(String term,int count) throws TwitterException{
        Twitter twitter = getTwitterinstance();
        List<Status> res = new ArrayList();
        long lastSearchID = Long.MAX_VALUE;
        int remainingTweets = count;
        Query query = new Query(term);
        try{
            while(remainingTweets > 0){
              remainingTweets = count - res.size();
              if(remainingTweets > 100){
                query.count(100);
              }else{
               query.count(remainingTweets); 
              }
              QueryResult result = twitter.search(query);
              res.addAll(result.getTweets());
              Status s = res.get(res.size()-1);
              long firstQueryID = s.getId();
              query.setMaxId(firstQueryID);
              remainingTweets = count - res.size();
            }
        }
        catch(TwitterException te)
        {
          System.out.println("Failed to search tweets: " + te.getMessage());
          System.exit(-1);
        }
         return res;
    }
}