package ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * edited for this project by bumjink
 * @author swapneel
 *
 */
public class Corpus {
	
	private ArrayList<Document> documents;
	private HashMap<String, Set<Document>> invertedIndex;
	
    /**
     * generates the inverted index
     * 
     * @param documents the list of documents
     */
    public Corpus(ArrayList<Document> documents) {
        this.documents = documents;
        invertedIndex = new HashMap<String, Set<Document>>();
        createInvertedIndex();
    }
	
    /**
     * maps a term to a set of documents containing that term
     */
	private void createInvertedIndex() {
		for (int i = 0; i < documents.size(); i++) {
		    Document doc = documents.get(i);
			Set<String> terms = doc.getTermList();
			for (String term : terms) {
				if (invertedIndex.containsKey(term)) {
					Set<Document> list = invertedIndex.get(term);
					list.add(doc);
					invertedIndex.put(term, list);
				} else {
					Set<Document> list = new TreeSet<Document>();
					list.add(doc);
					invertedIndex.put(term, list);
				}
			}
		}
	}
	
	/**
	 * returns the idf for a given term.
	 * @param term a term in a document
	 * @return the idf for the term
	 */
	public double getInverseDocumentFrequency(String term) {
		if (invertedIndex.containsKey(term)) {
			double size = documents.size();
			double documentFrequency = invertedIndex.get(term).size();
			return Math.log(size / documentFrequency);
		} else {
			return 0.;
		}
	}

	public ArrayList<Document> getDocuments() {
		return documents;
	}

	public HashMap<String, Set<Document>> getInvertedIndex() {
		return invertedIndex;
	}
	
}
