package es.ulpgc.twitterrdf;
import static es.ulpgc.twitterrdf.TwitterHandler.showStatus;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import twitter4j.Status;
import twitter4j.User;

/**
 *
 * @author dadepe
 */
public class RDFHandler {    
    String tweetURI = "http://www.twitterRDF.fake/tweetnamespace/";
    String userURI = "http://www.twitterRDF.fake/usernamespace/";
    String langURI = "http://www.twitterRDF.fake/langnamespace/";
    String replyURI = "http://www.twitterRDF.fake/replynamespace/";
    String subjectURI = "http://www.twitterRDF.fake/subjectnamespace/";
    String termURI = "http://www.twitterRDF.fake/termnamespace/";
    
    String subject;
    String term;
    
    List<Status>tweets;
    String date;
    String txt;
    String user_loc;
    String user_name;
    String id_lang;
    String lang_label;
    long id_tweet;
    long uid;
    long inRepTo;
    Model model;
    
    Property p_text;
    Property p_tw_id;
    Property p_date;
    
    Property p_author;
    Property p_author_name;
    Property p_author_loc;
    
    Property p_lang;
    Property p_lang_id;
    Property p_lang_label;
        
    Property p_rep_to;
    Property p_rel_to;
    Property p_lab;
    
    
    public RDFHandler(List<Status> tweets, String subject, String term){
        this.tweets = tweets;
        this.model = ModelFactory.createDefaultModel();
        
        p_text = model.createProperty(tweetURI, "text");
        p_tw_id = model.createProperty(tweetURI, "tweet_id");
        p_date = model.createProperty(tweetURI, "date");
        p_author = model.createProperty(userURI, "autor");
        p_author_name = model.createProperty(userURI, "author_name");
        p_author_loc = model.createProperty(userURI, "author_location");
        p_lang = model.createProperty(langURI, "language");
        p_lang_id = model.createProperty(langURI, "language_id");
        p_lang_label = model.createProperty(langURI, "language_label");
        p_rep_to = model.createProperty(replyURI, "replay");
        p_rel_to = model.createProperty(subjectURI,"theme");
        p_lab = model.createProperty(termURI,"label");
        
        this.subject = subject;
        this.term = term;
    }
    private void  giveRdfFieldsOfTweet(Status tweet){
        date = formatDate(tweet.getCreatedAt());
        
        id_tweet =tweet.getId();
        
        txt = tweet.getText();
        
        user_loc = giveUserLocalitation(tweet);
        user_name = giveUserName(tweet);
        uid = giveUserId(tweet);
        
        id_lang = giveLangId(tweet);
        lang_label = giveLangLabel(tweet);
        
        inRepTo = tweet.getInReplyToStatusId();
        
    
    }
    
    public void generateTweetResources(){
        for(Status s : tweets){
                generateTweetResource(s, true);
            }
    }
    
    private Resource generateTweetResource(Status status, boolean justOnce){
        if(status == null)return null;
        giveRdfFieldsOfTweet(status);
        
        Resource tweet = model.createResource(tweetURI+id_tweet);
        tweet.addProperty(p_date, date);
        tweet.addProperty(p_tw_id, id_tweet+"");
        tweet.addProperty(p_text,txt);
        
        Resource user = createUserResource();
        tweet.addProperty(p_author, user);
        
        Resource language = createLangResource();
        tweet.addProperty(p_lang, language);
        
        if(inRepTo != -1 && justOnce){
            Status statusInReplayTo = showStatus(inRepTo);
            if(statusInReplayTo != null){
                tweet.addProperty(p_rep_to,generateTweetResource(statusInReplayTo,false));
            }
        }
        Resource termResource = createTermResource(subject,term);
        tweet.addProperty(p_rel_to, termResource);
        
        return tweet;
    }
    
    private String formatDate(Date date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate()
                   .format(formatter);

    }

    private String giveUserLocalitation(Status tweet) {
        return getUser(tweet).getLocation();
    }

    private String giveUserName(Status tweet) {
        return getUser(tweet).getName();
    }

    private String giveLangId(Status tweet) {
        return tweet.getLang();
    }

    private String giveLangLabel(Status tweet) {
        String res = "";
        String lang_id = giveLangId(tweet);
        res += Locale.forLanguageTag(lang_id).getDisplayLanguage();
        return res;
    }

    private User getUser(Status tweet) {
        return tweet.getUser();
    }

    private Resource createUserResource() {
        Resource user = model.createResource(userURI+uid);
        user.addProperty(p_author_name, user_name);
        user.addProperty(p_author_loc, user_loc);
        return user;
    }

    private Resource createLangResource() {
        Resource lang = model.createResource(langURI+ id_lang);
        lang.addProperty(p_lang_id,id_lang);
        lang.addProperty(p_lang_label, lang_label);
        return lang;
    }

    private long giveUserId(Status tweet) {
        User user = getUser(tweet);
        return user.getId();
    }

    private Resource createTermResource(String theme,String term) {
        Resource themeResource = model.createResource(subjectURI+theme);
        themeResource.addProperty(RDF.type, RDFS.Class);
        themeResource.addProperty(RDFS.label, theme);
        Resource termResource = model.createResource(termURI+term);
        termResource.addProperty(RDF.type, themeResource);
        termResource.addProperty(p_lab, term);
        return termResource;
        
    }
    
    public Model getModel(){
        return model;
    }
}
