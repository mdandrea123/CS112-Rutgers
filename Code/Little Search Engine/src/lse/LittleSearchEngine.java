package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages
 * in which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the
	 * associated value is an array list of all occurrences of the keyword in
	 * documents. The array list is maintained in DESCENDING order of frequencies.
	 */
	HashMap<String, ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String, ArrayList<Occurrence>>(1000, 2.0f);
		noiseWords = new HashSet<String>(100, 2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword
	 * occurrences in the document. Uses the getKeyWord method to separate keywords
	 * from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an
	 *         Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String, Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (docFile == null) {
			throw new FileNotFoundException("file not found");
		}
		HashMap<String, Occurrence> map = new HashMap<String, Occurrence>(1000, 2.0f);
		Scanner input = new Scanner(new File(docFile));

		while (input.hasNext()) {
			String word = input.next();
			word = getKeyword(word);

			if (word == null) {
				continue;
			}
			if (map.containsKey(word) == false) {
				Occurrence count = new Occurrence(docFile, 1);
				map.put(word, count);
			} else {
				Occurrence count = map.get(word);
				count.frequency++;
				map.put(word, count);
			}
		}
		return map;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex hash
	 * table. For each keyword, its Occurrence in the current document must be
	 * inserted in the correct place (according to descending order of frequency) in
	 * the same keyword's Occurrence list in the master hash table. This is done by
	 * calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String, Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String i : kws.keySet()) {
			if (keywordsIndex.containsKey(i) == false) {
				ArrayList<Occurrence> test = new ArrayList<Occurrence>();
				Occurrence occ = kws.get(i);
				test.add(occ);
				insertLastOccurrence(test);
				keywordsIndex.put(i, test);
			} else {
				ArrayList<Occurrence> test = keywordsIndex.get(i);
				Occurrence occ = kws.get(i);
				test.add(occ);
				insertLastOccurrence(test);
				keywordsIndex.put(i, test);
			}
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of
	 * any trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!' NO
	 * OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be
	 * stripped So "word!!" will become "word", and "word?!?!" will also become
	 * "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (word == null) {
			return null;
		}
		word = word.toLowerCase();
		char hold = word.charAt(word.length() - 1);
		while (endHasPunctuation(hold)) {
			word = word.substring(0, word.length() - 1);
			if (word.length() > 1) {
				hold = word.charAt(word.length() - 1);
			} else {
				break;
			}
		}
		if (hasDigits(word)){
			return null;
		}
		if (wordHasPunctuation(word)){
			return null;
		}
		if (noiseWords.contains(word)){
			return null;
		}

		return word;
	}

	private static boolean hasDigits(String word){
		for (int i = 0; i < word.length(); i ++){
			if (Character.isDigit(word.charAt(i))){
				return true;
			}
		}
		return false;
	}

	private static boolean endHasPunctuation(char last){
		if (last == '.' || last == ',' || last == '?' || last == ':' || last == ';' || last == '!'){
			return true;
		}
		return false;
	}

	private static boolean wordHasPunctuation(String word){
		for (int i = 0; i < word.length(); i++){
			if (!Character.isLetterOrDigit(word.charAt(i))){
				return true;
			}
		}
		return false;
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in
	 * the list, based on ordering occurrences on descending frequencies. The
	 * elements 0..n-2 in the list are already in the correct order. Insertion is
	 * done by first finding the correct spot using binary search, then inserting at
	 * that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary
	 *         search process, null if the size of the input list is 1. This
	 *         returned array list is only used to test your code - it is not used
	 *         elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		ArrayList<Integer> arr = new ArrayList<Integer>();

		if (occs.size() == 1){
			return null;
		}
		Occurrence temp = occs.get(occs.size() -1);
		occs.remove(occs.size() -1);

		int hi = 0;
		int low = occs.size() -1;
		int mid = 0;
		int midFreq;

		while (hi <= low){
			mid = (hi + low) / 2;
			midFreq = occs.get(mid).frequency;

			if (midFreq == temp.frequency) {
				arr.add(mid);
				break;
			}

			if (midFreq < temp.frequency) {
				low = mid - 1;
				arr.add(mid);
			}

			if (midFreq > temp.frequency) {
				hi = mid + 1;
				arr.add(mid);
				mid++;
			}
		}
		occs.add(mid, temp);

		return arr;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all
	 * keywords, each of which is associated with an array list of Occurrence
	 * objects, arranged in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile       Name of file that has a list of all the document file
	 *                       names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise
	 *                       word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input
	 *                               files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String, Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2
	 * occurs in that document. Result set is arranged in descending order of
	 * document frequencies.
	 * 
	 * Note that a matching document will only appear once in the result.
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. That is,
	 * if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all,
	 * result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in
	 *         descending order of frequencies. The result size is limited to 5
	 *         documents. If there are no matches, returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		ArrayList<String> top5results = new ArrayList<String>();
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<Occurrence> list1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> list2 = keywordsIndex.get(kw2);
		if ((kw1 == null && kw2 == null) || (!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2))
				|| (keywordsIndex.isEmpty())) {// both strings are not found
			System.out.println("Both strings are not found");
			return null;
		} else if (keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {// contains kw1 but not kw2
			for (int i = 0; i < list1.size(); i++) {
				Occurrence occurrence = list1.get(i);
				if (top5results.size() < 5) {
					top5results.add(occurrence.document);
				}
			}
			System.out.println("contains kw1 but not kw2");
			System.out.println("Results:" + top5results);
			return top5results;
		} else if (keywordsIndex.containsKey(kw2) && !keywordsIndex.containsKey(kw1)) {// contains kw1 but not kw2
			for (int i = 0; i < list2.size(); i++) {
				Occurrence occurrence = list2.get(i);
				if (top5results.size() < 5) {
					top5results.add(occurrence.document);
				}
			}
			System.out.println("contains kw2 but not kw1");
			System.out.println("Results:" + top5results);
			return top5results;
		} else {// both are keywords
			System.out.println("both are keywords");
			ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
			occs.addAll(keywordsIndex.get(kw1));
			occs.addAll(keywordsIndex.get(kw2));
			for (int count = 0; count < 5 && !occs.isEmpty(); count++) {
				int ptr = 0;
				int prev = -1;
				for (ptr = 0; ptr < occs.size() && occs.get(ptr) != null; ptr++) {
					if (prev == -1) {
						if (!top5results.contains(occs.get(ptr).document))
							prev = ptr;
					} else if (occs.get(ptr).frequency > occs.get(prev).frequency) {
						if (!top5results.contains(occs.get(ptr).document))
							prev = ptr;
					} else if (occs.get(ptr).frequency == occs.get(prev).frequency) {
						if (keywordsIndex.get(kw1).contains(occs.get(ptr))) {
							if (!top5results.contains(occs.get(ptr).document))
								prev = ptr;
						}
					}
				}
				if (prev != -1)
					top5results.add(occs.remove(prev).document);
			}
			System.out.println("Result: " + top5results);
			return top5results;
		}
	}

}
