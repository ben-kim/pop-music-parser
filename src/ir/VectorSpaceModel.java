package ir;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * edited for this project by bumjink
 * @author swapneel
 *
 */
public class VectorSpaceModel {
	
	private Corpus corpus;

	// map from a document to a map of terms and weights
	private HashMap<Document, HashMap<String, Double>> tfIdfWeights;

	public VectorSpaceModel(Corpus corpus) {
		this.corpus = corpus;
		tfIdfWeights = new HashMap<Document, HashMap<String, Double>>();
		createTfIdfWeights();
	}
	
	public VectorSpaceModel(HashMap<Document, HashMap<String, Double>> map) {
		this.tfIdfWeights = map;
	}

	private void createTfIdfWeights() {
		System.out.println(LocalTime.now().toString() + ": Generating tf-idf weight vectors");
		Set<String> terms = corpus.getInvertedIndex().keySet();
		ArrayList<Document> documents = corpus.getDocuments();
		for (int i = 0; i < documents.size(); i++) {
		    Document doc = documents.get(i);
			HashMap<String, Double> weights = new HashMap<String, Double>();
			for (String term : terms) {
				double tf = doc.getTermFrequency(term);
				double idf = corpus.getInverseDocumentFrequency(term);
				weights.put(term, tf * idf);
			}
			tfIdfWeights.put(doc, weights);
		}
	}
	
	private double getMagnitude(Document document) {
		Double[] weights = {};
		tfIdfWeights.get(document).values().toArray(weights); 
		double magnitude = 0.;
		for (int i = 0; i < weights.length; i++) {
			magnitude += weights[i] * weights[i];
		}
		return Math.sqrt(magnitude);
	}
	
	private double getDotProduct(Document d1, Document d2) {
		HashMap<String, Double> weights1 = tfIdfWeights.get(d1);
		HashMap<String, Double> weights2 = tfIdfWeights.get(d2);
	    Double[] keys = {};
        weights1.values().toArray(keys); 
        double product = 0.;
		for (int i = 0; i < keys.length; i++) {
			product += weights1.get(keys[i]) * weights2.get(keys[i]);
		}
		return product;
	}
	
	/**
	 * cosine similarity ranges from 0 (not similar) to 1 (very similar)
	 * @param d1 Document 1
	 * @param d2 Document 2
	 * @return the cosine similarity
	 */
	public double cosineSimilarity(Document d1, Document d2) {
		return getDotProduct(d1, d2) / (getMagnitude(d1) * getMagnitude(d2));
	}
}