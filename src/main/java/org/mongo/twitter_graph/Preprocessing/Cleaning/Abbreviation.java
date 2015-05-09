package org.mongo.twitter_graph.Preprocessing.Cleaning;

/**
 * @author Lassoued Anouer
 *
 */
public class Abbreviation {
    private String abbreviation;
    private String originalForm;
    
    public Abbreviation(String abbreviation, String originalForm){
        this.abbreviation = abbreviation;
        this.originalForm = originalForm;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setOriginalForm(String originalForm) {
        this.originalForm = originalForm;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getOriginalForm() {
        return originalForm;
    }
    
    
}
