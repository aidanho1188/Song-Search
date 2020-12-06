package student;

import java.io.*;
import java.util.*;

import student.Song.CmpArtist;

/*
 * SearchByArtistPrefix.java
 * starting code
 * Boothe 2016
 */
public class SearchByArtistPrefix {

	private Song[] songs; // The constructor fetches and saves a reference to the song array here

	public SearchByArtistPrefix(SongCollection sc) {
		songs = sc.getAllSongs();
	}

	/**
	 * find all songs matching artist prefix uses binary search should operate in
	 * time log n + k (# matches)
	 */
	public Song[] search(String artistPrefix) {
		// write this method
	
		// declarations of variables and initializations
		CmpArtist cmpArtist = new CmpArtist();
		Song[] matchArtist = null;
		String artist, prefix;
		int low, high, cmpCnt;
	
		// negative number = no match, insertion point
		low = Arrays.binarySearch(songs, new Song(artistPrefix, "", ""), cmpArtist);
		
		// reset cmpCnt from binarySearch before count for comparison
		cmpArtist.resetCmpCnt();
	
		// Calling getCmpCnt method on our comparator to get the call count
		cmpCnt = ((CmpCnt) cmpArtist).getCmpCnt();
		low = Math.abs(low) -1 ;
		high = low;
		
		
		
		// if low is negative
		// then high =low; increase high each loop
		
		// if low is positive
		// then high = low; but decrease low each loop
		
		
		// abs low and -1 to match the index
		while (low >= 0) {
			artist = songs[low].getArtist().toLowerCase();
			prefix = artistPrefix.toLowerCase();
			if (artist.startsWith(prefix)) {
				low--;
			} else if (!artist.startsWith(prefix)) {
				break;
			}
			cmpCnt++;
		}
		low= low+1;
		/**
		 * loop to find the high index of binary search, compare artist with
		 * artistPrefix, array increase high by 1
		 */
		while (high < songs.length) {
			artist = songs[high].getArtist().toLowerCase();
			prefix = artistPrefix.toLowerCase();
			if (artist.startsWith(prefix)) {
				high++;
			} else if (!artist.startsWith(prefix)) {
				break;
			}
			cmpCnt++;
		}
		// print out number of comparison
		// System.out.println("Total number of comparison is " + cmpCnt);
	
		// copy over maychArtist array
		matchArtist = Arrays.copyOfRange(songs, low, high);
		// return array
		return matchArtist;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

		if (args.length > 1) {
			System.out.println("searching for: " + args[1]);
			Song[] byArtistResult = sbap.search(args[1]);

			// to do: show first 10 matches
			SongCollection.sample(byArtistResult);
		}
	}
}
