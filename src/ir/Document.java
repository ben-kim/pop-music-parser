package ir;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Set;

/**
 * Edited for this project by bumjink
 * @author swapneel
 *
 */
public class Document implements Comparable<Document> {
	
	private HashMap<String, Integer> termFrequency;
	private String artistName;

	public Document(String artist, String lyrics) {
		artistName = artist;
		termFrequency = new HashMap<String, Integer>();
		String[] tokens = lyrics.split("\\s+");
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		}
		preprocess(tokens);
	}
	
	private void preprocess(String[] tokens) {
		System.out.println(LocalTime.now().toString() + ": Preprocessing " + artistName);
		for (String token : tokens) {
			if (!(token.equalsIgnoreCase(""))) {
				if (termFrequency.containsKey(token)) {
					int oldCount = termFrequency.get(token);
					termFrequency.put(token, ++oldCount);
				} else {
					termFrequency.put(token, 1);
				}
			}
		}
	}
	
	/**
	 * This method will return the term frequency for a given word.
	 * If this document doesn't contain the word, it will return 0
	 * @param word The word to look for
	 * @return the term frequency for this word in this document
	 */
	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}
	
	/**
	 * This method will return a set of all the terms which occur in this document.
	 * @return a set of all terms in this document
	 */
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