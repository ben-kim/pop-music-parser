package ir;

import java.util.HashMap;
import java.util.Set;

/**
 * edited for this project by bumjink
 * @author swapneel
 *
 */
public class Document implements Comparable<Document> {
	
	private HashMap<String, Integer> termFrequency;
	private String artistName;

	/**
	 * 
	 * @param artist name of the artist
	 * @param lyrics a single string of all lyrics
	 */
	public Document(String artist, String lyrics) {
		artistName = artist;
		termFrequency = new HashMap<String, Integer>();
		String[] tokens = lyrics.split("\\s+");
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		}
		preprocess(tokens);
	}
	
	/**
	 * generates the term frequency by counting all instances
	 * @param tokens array of all words
	 */
	private void preprocess(String[] tokens) {
		for (int i = 0; i < tokens.length; i++) {
		    String token = tokens[i];
		    if (termFrequency.containsKey(token)) {
		        int n = termFrequency.get(token);
		        termFrequency.put(token, ++n);
		    } else {
		        termFrequency.put(token, 1);
		    }
		}
		termFrequency.remove("");
	}
	
	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}

	public Set<String> getTermList() {
		return termFrequency.keySet();
	}

	@Override
	public String toString() {
		return artistName;
	}

	@Override
	public int compareTo(Document other) {
		return toString().compareTo(other.toString());
	}

}
