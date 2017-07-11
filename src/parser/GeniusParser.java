package parser;

import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalTime;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeniusParser {

	final String artistIndexBase = "https://genius.com/artists-index/";
	final String billboardBase = "http://www.billboard.com/artists/";
	
	private HashMap<String, String> artistUrlMap;
	private HashMap<String, String> lyricMap;
	private HashSet<String> popularArtists;

	public GeniusParser() {
		artistUrlMap = new HashMap<>();
		lyricMap = new HashMap<>();
		popularArtists = new HashSet<>();
	}

	// Create a set of the most popular artists by letter on Billboard
	private void buildSetFromBillboard() {
		for (int i = 97; i <= 122; i++) {
			try {
				Document doc = Jsoup.connect(billboardBase + (char) i).get();
				Elements artists = doc.select("img[alt]");
				for (Element artist : artists) {
					popularArtists.add(artist.attr("alt"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Document doc = Jsoup.connect(billboardBase + "number").get();
			Elements artists = doc.select("img[alt]");
			for (Element artist : artists) {
				popularArtists.add(artist.attr("alt"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Find the Genius page for each artist
	public void getAllArtists() {
		System.out.println(LocalTime.now().toString() + ": Accessing Billboard pages");
		buildSetFromBillboard();
		System.out.println(LocalTime.now().toString() + ": Found popular artists on Billboard");
		for (int i = 97; i <= 122; i++) {
			getArtistURLsByLetter((char) i);
		}
		getArtistURLsByLetter((char) 48);
	}

	// Select the top 20 most popular artists starting with specified letter
	private void getArtistURLsByLetter(char letter) {
		try {
			Document doc = Jsoup.connect(artistIndexBase + letter).get();
			Elements names = doc.getElementsByClass("artists_index_list-artist_name");
			for (Element name : names) {
				String artist = URLDecoder.decode(name.text(), "UTF-8");
				String URL = URLDecoder.decode(name.attr("abs:href"), "UTF-8");
				if (popularArtists.contains(artist)) {
					artistUrlMap.put(artist, URL);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getAllLyrics() {
		for (String artist : artistUrlMap.keySet()) {
			String lyrics = getArtistLyrics(artist);
			if (!lyrics.isEmpty()) {
				lyricMap.put(artist, lyrics);
			}
		}
	}

	public String getArtistLyrics(String artist) {
		if (!artistUrlMap.containsKey(artist)) {
			throw new IllegalArgumentException("Artist not found");
		}
		System.out.println(LocalTime.now().toString() + ": Finding lyrics for " + artist);
		// Get the URLs of the artist's most popular songs
		Set<String> songURLs = new HashSet<>();
		try {
			songURLs = new HashSet<>();
			Document doc = Jsoup.connect(artistUrlMap.get(artist)).get();
			Elements cards = doc.getElementsByClass("mini_card");
			for (Element card : cards) {
				String URL = URLDecoder.decode(card.attr("abs:href"), "UTF-8");
				songURLs.add(URL);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Get the lyrics from each of the artist's most popular songs
		String allLyrics = "";
		for (String url : songURLs) {
			try {
				Document doc = Jsoup.connect(url).get();
				Element lyricBlock = doc.getElementsByClass("lyrics").first();
				allLyrics += lyricBlock.text() + " ";
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allLyrics;
	}


	public Map<String, String> getLyricMap() {
		return Collections.unmodifiableMap(lyricMap);
	}
}
