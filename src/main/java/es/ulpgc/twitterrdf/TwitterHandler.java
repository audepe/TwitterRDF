package es.ulpgc.twitterrdf;

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

    public static List<Status> searchTweets(String search) throws TwitterException {
        Twitter twitter = getTwitterinstance();
        Query query = new Query(search);
        QueryResult result = twitter.search(query);

        return result.getTweets();
    }
}