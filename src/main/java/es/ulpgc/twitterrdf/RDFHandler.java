package es.ulpgc.twitterrdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author dadepe
 */
public class RDFHandler {
    public Model createModel(){
        return ModelFactory.createDefaultModel();
    }
    
    public Resource createResource(Model model, String URI){
        return model.createResource(URI);
    }
    
    public void addProperty(Resource resource, Property property, String value){
        resource.addProperty(property,value);
    }
}
