package parser;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeniusParser {

	final String artistIndexBase = "https://genius.com/artists-index/";	
	
	private HashMap<String, ArrayList<String>> artistUrlMap;
	private HashMap<String, String> lyricMap;

	public GeniusParser() {
		artistUrlMap = new HashMap<>();
		lyricMap = new HashMap<>();
	}
	
	/**
	 * in the artist index, there are two songs listed for each popular artist
	 */
	public void findArtistUrls() {
	    for (int c = 97; c <= 122; c++) {
	        try {
	            Document document = Jsoup.connect(artistIndexBase + (char) c).get();
	            Elements artists = document.select(".artists_index_list-artist_name");
	            Elements songs = document.select(".popular_song");
	            for (int i = 0; i < songs.size() - 1; i += 2) {
	                ArrayList<String> urls = new ArrayList<>();
	                urls.add(songs.get(i).select("a").first().attr("href"));
	                urls.add(songs.get(i+1).select("a").first().attr("href"));     
	                String artistName = URLDecoder.decode(artists.get(i/2).text(), "UTF-8");
	                artistUrlMap.put(artistName, urls);
	            }
            } catch (IOException e) {
                e.printStackTrace();
            }
	    }
	    try {
	        Document document = Jsoup.connect(artistIndexBase + "0").get();
            Elements artists = document.select(".artists_index_list-artist_name");
            Elements songs = document.select(".popular_song");
            for (int i = 0; i < songs.size(); i += 2) {
                ArrayList<String> urls = new ArrayList<>();
                urls.add(songs.get(i).select("a").first().attr("href"));
                urls.add(songs.get(i+1).select("a").first().attr("href"));     
                String artistName = URLDecoder.decode(artists.get(i/2).text(), "UTF-8");
                artistUrlMap.put(artistName, urls);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * connects to each URL and gets the raw text of the lyrics
	 * @param artist the name of the artist to find lyrics for
	 * @return a string containing the lyrics of the two songs
	 */
	public String getArtistLyrics(String artist) {
		if (!artistUrlMap.containsKey(artist)) {
			throw new IllegalArgumentException("Artist not found");
		}
		String allLyrics = "";
		ArrayList<String> urls = artistUrlMap.get(artist);
		for (int i = 0; i < urls.size(); i++) {
			try {
				Document document = Jsoup.connect(urls.get(i)).get();
				Element lyricBlock = document.getElementsByClass("lyrics").first();
				allLyrics += lyricBlock.text() + " ";
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
                e.printStackTrace();
            }
		}
		return allLyrics;
	}
	
	/**
	 * generates the lyric map
	 */
    public void getAllLyrics() {
        for (String artist : artistUrlMap.keySet()) {
            String lyrics = getArtistLyrics(artist);
            lyricMap.put(artist, lyrics);
        }
    }

	public Map<String, String> getLyricMap() {
		return Collections.unmodifiableMap(lyricMap);
	}
}
