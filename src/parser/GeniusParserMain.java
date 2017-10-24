package parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import java.util.Map.Entry;

import ir.*;

public class GeniusParserMain {

	public static void main(String[] args) {

		ArrayList<Document> documents = new ArrayList<Document>();
		GeniusParser parser = new GeniusParser();

		// constructing lyric map
		parser.findArtistUrls();
		parser.getAllLyrics();
		
		// constructing vector space model
		Map<String, String> lyricMap = parser.getLyricMap();
		for (Entry<String, String> entry : lyricMap.entrySet()) {
			documents.add(new Document(entry.getKey(), entry.getValue()));
		}
		Corpus corpus = new Corpus(documents);
		VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);
		TreeMap<Double, String> similarityMap = new TreeMap<>(Collections.reverseOrder());

		// building similarity map
		for (int i = 0; i < documents.size(); i++) {
			for (int j = i + 1; j < documents.size(); j++) {
				Document doc1 = documents.get(i);
				Document doc2 = documents.get(j);
				similarityMap.put(vectorSpace.cosineSimilarity(doc1, doc2),
				        "Comparing " + doc1 + " and " + doc2);
			}
		}
		
		// output results to text file
		try {
			PrintWriter writer = new PrintWriter("results.txt", "UTF-8");
			while (!similarityMap.isEmpty()) {
				Entry<Double, String> entry = similarityMap.pollFirstEntry();
				writer.println(entry.getValue() + ": " + entry.getKey());
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
		
}
