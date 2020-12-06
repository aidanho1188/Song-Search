package student;
import java.util.*;
import java.util.regex.*; 

public class PhraseRanking {
	/**
	 * rank a lyric bases on matches of lyricsPharse
	 * @param lyrics
	 * @param lyricsPhrase
	 * @return
	 */
	 static int rankPhrase(String lyrics, String lyricsPhrase) {
		// tokenize(split) for more accurate ranking
	    ArrayList<String> lyricsTokens = tokenizeWords(lyrics);
	    ArrayList<String> lyricsPhraseTokens = tokenizeWords(lyricsPhrase);
	    // case 1: exact match
	    if(lyrics.contains(lyricsPhrase + " ")) {
	    	return lyricsPhraseTokens.size();
	    }
	    // case 2: there are lyrisphrase words in lyric 
	    int minRank = Integer.MAX_VALUE;
	    /*
	     * loop through the whole lyrics and start counting when first word in lyricsPhrase is matched
	     * stop counting when it reach the last word in lyricsPhrase
	     */
	    for (int i = 0; i < lyricsTokens.size(); i++) {
	        int phraseIndex = 0;
	        if (lyricsTokens.get(i).equals(lyricsPhraseTokens.get(phraseIndex))) {
	            int rank = 0;
	            for (int j = i + 1; j < lyricsTokens.size(); j++) {
	                ++rank;
	                if (phraseIndex < lyricsPhraseTokens.size() - 1 && lyricsTokens.get(j).equals(lyricsPhraseTokens.get(phraseIndex + 1))) {
	                    ++phraseIndex;
	                }
	                if (phraseIndex == lyricsPhraseTokens.size() - 1) {
	                    ++rank;
	                    break;
	                }
	                if (j == (lyricsTokens.size() - 1)) {
	                    return minRank;
	                }
	            }
	            // take the smallest rank
	            if (rank < minRank) {
	                minRank = rank;
	            }
	        }

	    }
	    return minRank;
	}
	
	/**
	 * A method to check if a lyric word is a letter
	 * @param c
	 * @return
	 */
	static boolean isLetter(char c) {
	    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	/**
	 * A tokenize method for tokenize both lyrics and lyricPharse
	 * @param str
	 * @return
	 */
	static ArrayList<String> tokenizeWords(String str) {
		ArrayList<String> tokens = new ArrayList<String>();
		String token = "";
		for (int i = 0; i < str.length(); i++) {
			if (isLetter(str.charAt(i))) {
				token += str.charAt(i);
			} else {
				if (!token.equals("")) {
					tokens.add(token.toLowerCase());
				}
				token = "";
			}
		}

		if (!token.equals("")) {
			tokens.add(token.toLowerCase());
		}

		return tokens;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
		int rank, totalSongs = 0;
		String lyrics;
        SongCollection sc = new SongCollection(args[0]);
        Song[] allSongs = sc.getAllSongs();
        for (int i = 0; i < allSongs.length; i++) {
			lyrics = allSongs[i].getLyrics();
			rank = rankPhrase(lyrics, args[1]);
			// print out only songs that have lyricsPhrase
			// 0 and 2147483647 mean lyricsPharse not found
			if (rank > 0 && rank != 2147483647) {
				System.out.println(rank + " "+ allSongs[i].getArtist() + " \"" + allSongs[i].getTitle() + "\"");
				totalSongs++;
			}
		}
        System.out.println("Total Songs: " + totalSongs);
	}
}
