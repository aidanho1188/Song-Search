package student;

import java.io.*;
import java.util.*;
import student.Song.CmpTitle;


public class SearchByTitlePrefix {

	private FibList<Song> titles;
	CmpTitle cmpTitle;
	public SearchByTitlePrefix(SongCollection sc) {
		cmpTitle = new CmpTitle();
		titles = new FibList<Song>(cmpTitle);
		// Add songs to the FibList
		for (Song title : sc.getAllSongs()) {
			titles.add(title);
		}
		// print out comparison count
		System.out.println("Total build comparisons: "+ cmpTitle.getCmpCnt());
	}

	public Song[] search(String titlePrefix) {
		// reset cmp count
		cmpTitle.resetCmpCnt(); 
		//uppercase first letter for comparison when call subList()
		titlePrefix = titlePrefix.substring(0, 1).toUpperCase() + titlePrefix.substring(1);
		
		Song fromElement = new Song("",titlePrefix,"");
		String lastTitle = titlePrefix.concat("~");
		Song toElement = new Song("",lastTitle,"");
		// create temp FibList to return
		FibList<Song> tempList = titles.subList(fromElement, toElement);
		Song[] matchedList = new Song[tempList.size];
		// print total comparison
		System.out.println("Total search comparisons: "+ cmpTitle.getCmpCnt());
		// return list
		return tempList.toArray(matchedList);
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);

		if (args.length > 1) {
			System.out.println("searching for: " + args[1]);
			Song[] byTitleResult = sbtp.search(args[1]);

			// to do: show first 10 matches
			SongCollection.sample(byTitleResult);
		}
	}
}
