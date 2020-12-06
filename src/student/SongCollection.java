package student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * SongCollection.java
 * Read the specified data file and build an array of songs.
 * 
 * Starting code by Prof. Boothe 2019
 */
public class SongCollection {

	private Song[] songs;

	public SongCollection(String filename) {
		// read in the song file and fill in the array
		try {
			File f = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(f));
			StringBuilder sb = new StringBuilder();
			String st;
			String artist = "", title = "", lyrics = "";
			ArrayList<Song> songArr = new ArrayList<>();
			// read file line by line then fill in array
			while ((st = br.readLine()) != null) {
				if (st.contains("ARTIST")) {
					artist = st.substring(8, st.lastIndexOf("\""));
				} else if (st.contains("TITLE")) {
					title = st.substring(7, st.lastIndexOf("\""));
				} else if (!st.startsWith("\"") || (st.contains("LYRICS"))) {
					st = st.replaceAll("LYRICS=\"", "");
					lyrics = sb.append(st + "\n").toString();
				} else {
					/*
					 * when reach the end of lyrics add song to array clean lyrics stringBuilder
					 */
					songArr.add(new Song(artist, title, lyrics));
					sb = new StringBuilder();
				}
			}
			/*
			 * close reader convert songArr to plain array
			 */
			br.close();
			songs = songArr.toArray(new Song[songArr.size()]);
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		// sort the songs array
		Arrays.sort(songs);
	}

	// returns the array of all Songs
	// this is used as the data source for building other data structures
	public Song[] getAllSongs() {
		return songs;
	}

	/**
	 * A sample method that return total # of songs and the first 10 songs
	 * 
	 * @param SongCollection object
	 */
	public static void sample(Song[] byArtistResult) {
		// # of songs
		System.out.println("Total songs = " + byArtistResult.length + ", first 10 songs:");

		// first 10 songs
		for (int i = 0; i < 10; i++) {
			if (i < byArtistResult.length) {
				System.out.println(byArtistResult[i]);
			} else {
				break;
			}
		}
	}

	// testing method
	// it gets the name of the song file as a command line argument
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);

		// todo: show song count and first 10 songs (name & title only, 1 per line)
		// sample(sc);

	}

}
