package student;

import java.util.*;
import student.Song.CmpRank;
import student.PhraseRanking;

public class SearchByLyricsPhrase {

	private Song[] allSongs;

	public SearchByLyricsPhrase(SongCollection sc) {
		allSongs = sc.getAllSongs();
	}

	/**
	 * Set rank, sort rank, then return as Song[]
	 * 
	 * @param lyricsPhrase
	 * @return
	 */
	public Song[] search(String lyricsPhrase) {
		List<Song> rankArray = new ArrayList<Song>();
		for (Song song : allSongs) {
			int rank = PhraseRanking.rankPhrase(song.getLyrics(), lyricsPhrase);

			if (rank > 0 && rank != 2147483647) {
				song.setRank(rank);
				rankArray.add(song);
			}
		}
		Collections.sort(rankArray, new CmpRank());
		Song[] results = new Song[rankArray.size()];
		results = (Song[]) rankArray.toArray(new Song[rankArray.size()]);
		return results;
	}
	
	/**
	 * print out the first 10 songs with rank
	 * @param byLyricsPhrase
	 */
	public static void rankSample(Song[] byLyricsPhrase) {
		// # of songs
		System.out.println("Total songs = " + byLyricsPhrase.length + ", first 10 songs:");

		// first 10 songs
		for (int i = 0; i < 10; i++) {
			if (i < byLyricsPhrase.length) {
				System.out.println(byLyricsPhrase[i].getRank() + " " + byLyricsPhrase[i]);
			} else {
				break;
			}
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsPhrase sblp = new SearchByLyricsPhrase(sc);
		if (args.length > 1) {
			System.out.println("searching for: " + args[1]);
			Song[] byLyricsPhrase = sblp.search(args[1]);
			rankSample(byLyricsPhrase);
		}
	}
}
