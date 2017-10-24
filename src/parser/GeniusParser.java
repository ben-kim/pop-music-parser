package parser;

import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalTime;
import java.util.Set;
import java.util.ArrayList;
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
	
	private HashMap<String, ArrayList<String>> artistUrlMap;
	private HashMap<String, String> lyricMap;

	public GeniusParser() {
		artistUrlMap = new HashMap<>();
		lyricMap = new HashMap<>();
	}
	
	/**
	 * 
	 */
	public void findArtistUrls() {
	    System.out.println(LocalTime.now().toString() + ": Accessing Genius artist index");
	    /*for (int c = 97; c <= 122; c++) {
	        try {
                Document document = Jsoup.connect(artistIndexBase + (char) c).get();
                Elements artists = document.select(".artists_index_list-artist_name");
                for (int i = 0; i < artists.size(); i++) {
                    String artistName = URLDecoder.decode(artists.get(i).text(), "UTF-8");
                    Elements songs = artists.get(i).getElementsByClass("popular_song");
                    ArrayList<String> urls = new ArrayList<>();
                    for (int j = 0; j < songs.size(); j++) {
                        urls.add(URLDecoder.decode(songs.get(i).attr("abs:href"), "UTF-8"));
                    }
                    artistUrlMap.put(artistName, urls);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
	    }*/
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
	    System.out.println(LocalTime.now().toString() + ": Found popular artists on Genius");
	}

	/**
	 * 
	 * @param artist
	 * @return
	 */
	public String getArtistLyrics(String artist) {
		if (!artistUrlMap.containsKey(artist)) {
			throw new IllegalArgumentException("Artist not found");
		}
		String allLyrics = "";
		ArrayList<String> urls = artistUrlMap.get(artist);
		for (int i = 0; i < urls.size(); i++) {
			try {
				Document doc = Jsoup.connect(urls.get(i)).get();
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
	

    /**
     * 
     */
    public void getAllLyrics() {
        for (String artist : artistUrlMap.keySet()) {
            String lyrics = getArtistLyrics(artist);
            lyricMap.put(artist, lyrics);
        }
    }

	/**
	 * 
	 * @return
	 */
	public Map<String, String> getLyricMap() {
		return Collections.unmodifiableMap(lyricMap);
	}
}
