package es.ulpgc.twitterrdf;

import java.util.ArrayList;
import java.util.List;
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

    public static Status showStatus(long id) {
        Twitter twitter = getTwitterinstance();
        try {
            return twitter.showStatus(id);
        } catch (TwitterException tw) {
            System.out.println("Exception: " + tw);
        }
        return null;
    }

    public static List<Status> searchTweets(String term, int count) throws TwitterException {
        Twitter twitter = getTwitterinstance();
        List<Status> res = new ArrayList();
        Query query = new Query(term);
        try {
            while (count > 0) {
                query.count(count > 100 ? 100 : count);
                QueryResult result = twitter.search(query);
                res.addAll(result.getTweets());
                if (result.hasNext()) {
                    query = result.nextQuery();
                }
                count -= result.getCount();
            }
        } catch (TwitterException te) {
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        return res;
    }
}
