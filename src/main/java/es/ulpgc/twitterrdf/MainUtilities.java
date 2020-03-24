package es.ulpgc.twitterrdf;

import static es.ulpgc.twitterrdf.TwitterHandler.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import twitter4j.Status;
import twitter4j.TwitterException;

public class MainUtilities {
    
    public static List<Status> getTweets(String search_term, int n_tweets){
        
        try {
            return searchTweets(search_term, n_tweets);
            
        } catch (TwitterException ex) {
            Logger.getLogger(MainUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String getFormattedTweets(List<Status> results){
        String res = "";
        for (Status status : results) {
            res += "@" + status.getUser().getScreenName() + ":" + status.getText() + "\n";
            res += "*****************\n";
        }
        return res;
    }
    
    public static boolean writeToFile(File file, Model model, String format){
        try{
           FileWriter writer;
           if("Turtle".equals(format)){
               writer = new FileWriter(file + ".ttl");
               RDFDataMgr.write(writer, model, RDFFormat.TURTLE);
               writer.close();
               return true;
           } else if("XML/RDF".equals(format)){
               writer = new FileWriter(file + ".rdf");
               RDFDataMgr.write(writer, model, RDFFormat.RDFXML);
               writer.close();
               return true;
           }
           return false;
        }catch(IOException e){
            return false;
        }
    }
}