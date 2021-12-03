package name.stratila.vladimir.clientforunsplash.Models;

import java.util.ArrayList;

public class SearchResult {
    private ArrayList<Photo> results;

    public SearchResult(ArrayList<Photo> results) {
        this.results = results;
    }

    public ArrayList<Photo> getResults() {
        return results;
    }

    public void setResults(ArrayList<Photo> results) {
        this.results = results;
    }
}
