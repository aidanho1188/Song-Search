package student;

import java.util.*;

public class SearchByLyricsWords {
	
	public String[] words = { "the", "of", "and" , "a", "to", "in", "is",
			           "you", "that", "it", "he", "for", "was", "on",
			 		   "are", "as", "with", "his", "they", "at", "be",
			 		   "this", "from", "I", "have", "or", "by", "one",
			 		   "had", "not", "but", "what", "all", "were", "when",
			 		   "we", "there", "can", "an", "your", "which", "their",
			 		   "if", "do", "will", "each", "how", "them","then",
			 		   "she", "many", "some", "so", "these", "would",
			 		   "into", "has", "more", "her", "two", "him", "see",
			 		   "could", "no", "make", "than", "been", "its", "now",
			 		   "my", "made", "did", "get", "our", "me", "too"
			 		   };
	
	TreeMap<String, TreeSet<Song>> lyricsMap;
	TreeSet<String> commonWords;
	TreeSet<Song> songsWithUniqueWords;

	public SearchByLyricsWords(SongCollection sc){
		// build
		lyricsMap = new TreeMap<String, TreeSet<Song>>();
		commonWords = new TreeSet<String>();
		// add words into commonWords
		commonWords.addAll(Arrays.asList(words));
		// adding unique words to a new map
		Song[] songs = sc.getAllSongs();
		// iterate the WHOLE list of song
		for (int i = 0; i < songs.length;i++) {
			// ignore punctuation and numbers                        
			String[] lyrics = songs[i].getLyrics().split("[^a-zA-Z]+"); 
			// iterate the WHOLE song of the list
			for (int j = 0; j < lyrics.length; j++) {                   
				String word = lyrics[j].toLowerCase();
				if(!commonWords.contains(word)) {
					// exclude single letters, alphabet a - z
					if(word.length() > 1) {                             
						// add to treeSet
						// map unique word into TreeSet of songs
						songsWithUniqueWords = lyricsMap.get(word);
						if (lyricsMap.containsKey(word)) {
							songsWithUniqueWords.add(songs[i]);
						} else {
							songsWithUniqueWords = new TreeSet<Song>();
							songsWithUniqueWords.add(songs[i]);
							lyricsMap.put(word, songsWithUniqueWords);
						}
					}
				}
			}
		}
	}
	
	public void statistic() {
		int k = 0;
		int s=0;
		
		// testing code
//		for (Map.Entry<String, TreeSet<Song>> entry : lyricsMap.entrySet()) {
//		     System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
//		     k++;
//		}

		for(TreeSet<Song> entry: lyricsMap.values()) {
			s += entry.size();
		}

		// print the number of total number of keys and spaces
		System.out.println("Number of keys: "+ lyricsMap.keySet().size());
		System.out.println("Total number of songs: "+ s);  
		System.out.println("Total space for key: "+ lyricsMap.keySet().size()*6);
		System.out.println("Total space for Song: "+ s*6);
		System.out.println("Total space for the whole data structure: "+ ((s*6)+(lyricsMap.keySet().size()*6)));
		System.out.println("Space usage expressed as (6k + 6s)/s = "+ (double)((s*6)+(lyricsMap.keySet().size()*6))/s);		
	}
	
	public Song[] search(String lyricsWords) {
		TreeSet<Song> songSet = new TreeSet<Song>();
		String[] searchedLyrics = lyricsWords.toLowerCase().split("[^a-zA-Z]+"); // split words
		for (String word : searchedLyrics) { // loop through the words and add songs
			if (lyricsMap.containsKey(word) && word.length() > 1 && !commonWords.contains(word)) {
				// add songs into treeSet
				if (songSet.isEmpty()) {
					songSet.addAll(lyricsMap.get(word));
				} else {
					songSet.retainAll(lyricsMap.get(word));
					// if it still empty after retain, return
					if (songSet.isEmpty()) {
						Song[] result = new Song[0];
						return result;
					}
				}
			}
		}
		// convert treeSet into Song[] array
		Song[] result = songSet.toArray(new Song[songSet.size()]);
		return result;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
 
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
//		sblw.statistic();
		if (args.length > 1) {
			System.out.println("searching for: " + args[1]);
			Song[] byLyricWord = sblw.search(args[1]);
			SongCollection.sample(byLyricWord);
		}
	}
	
}
