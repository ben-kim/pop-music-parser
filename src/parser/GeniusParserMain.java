package parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import java.util.Map.Entry;

import ir.*;

public class GeniusParserMain {

	public static void main(String[] args) {

		ArrayList<Document> documents = new ArrayList<Document>();
		GeniusParser parser = new GeniusParser();

		System.out.println(LocalTime.now().toString() + ": Finding artist URLs");
		parser.getAllArtists();
		System.out.println(LocalTime.now().toString() + ": Finding lyrics for all artists");
		parser.getAllLyrics();
		
		for (Entry<String, String> entry : parser.getLyricMap().entrySet()) {
			documents.add(new Document(entry.getKey(), entry.getValue()));
		}
		Corpus corpus = new Corpus(documents);
		VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);
		TreeMap<Double, String> similarityMap = new TreeMap<>(Collections.reverseOrder());

		System.out.println(LocalTime.now().toString() + ": Building similarity map");
		for (int i = 0; i < documents.size(); i++) {
			for (int j = i + 1; j < documents.size(); j++) {
				Document doc1 = documents.get(i);
				Document doc2 = documents.get(j);
				similarityMap.put(vectorSpace.cosineSimilarity(doc1, doc2),
				        "Comparing " + doc1 + " and " + doc2 + ": ");
			}
		}
		
		System.out.println(LocalTime.now().toString() + ": Writing to file");
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
