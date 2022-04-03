import java.io.*;
import java.util.*;

public class spellCheck {
	
	
	private static ArrayList<String> filtered = new ArrayList<>();
	
	
	public static void main(String[] args) throws IOException{

		String[] wordsOutput = new String[20];
		Scanner sc = new Scanner(System.in);
		String searchWord;
		
		System.out.println("Enter the Word to be searched\n");
		searchWord = sc.next();
		
		
		wordsOutput = getOutputWords(searchWord);
		System.out.println("\nspell checking started\n");
		System.out.println("Words are: \n");
		
		for(int i=0;i<20;i++)
		{
			System.out.println(ordinal(i+1) + " -'" + wordsOutput[i] + "'");
		}
	}
	
	public static String ordinal(int i) {
	    String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + suffixes[i % 10];

	    }
	}
	
	public static String[] getOutputWords(String searchWord) throws IOException {
		getFiltered();
		HashMap<String, Integer> nwords = new HashMap<>();
		String[] outputWords = new String[20];
		for (String w : filtered) {
			int editDist = editDistance(searchWord, w);
			nwords.put(w, editDist);
		}
		Map<String, Integer> awords = sortByValue(nwords);
		// get top 10 alternative words
		int rank = 0;
		for (Map.Entry<String, Integer> temp : awords.entrySet()) {
			if (temp.getValue() != 0) {
				outputWords[rank] = temp.getKey();
				rank++;
				if (rank == 20){ break; }
			}
		}
		return outputWords;
	}
	


	private static void getFiltered() throws IOException {
		File textfiles = new File("C:\\Users\\3298s\\Documents\\text");
		File[] textlists = textfiles.listFiles();
//		for(int i=0;i<12;i++)
//		{
//		System.out.println(textlists[i].toString());
//		}
		StringBuilder ftext = new StringBuilder();
		assert textlists != null;
		for (File txt : textlists) {
			BufferedReader br = new BufferedReader(new FileReader(txt));
			String str;
			while ((str = br.readLine()) != null) {
				ftext.append(str);
			}
			br.close();
		}
		String finalText = ftext.toString();
		StringTokenizer tokenizer =
				new StringTokenizer(finalText, "0123456789 ,`*$|~(){}_@><=+[]\\?;/&#-.!:\"'\n\t\r");
		while (tokenizer.hasMoreTokens()) {
			String temp = tokenizer.nextToken().toLowerCase(Locale.ROOT);
			if (!filtered.contains(temp)) {
				filtered.add(temp);
			}
		}
	}

	public static int editDistance(String word1, String word2) {
		int length1 = word1.length();
		int length2 = word2.length();
		
		// Initialized Edit distance table
		int[][] edt = new int[length1 + 1][length2 + 1];

		for (int i = 0; i <= length1; i++) { edt[i][0] = i;	}
		for (int j = 0; j <= length2; j++) { edt[0][j] = j; }
		//iterate though, and check last char
		for (int i = 0; i < length1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < length2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					edt[i + 1][j + 1] = edt[i][j];
				} else {
					int rep = edt[i][j] + 1;
					int ins = edt[i][j + 1] + 1;
					int del = edt[i + 1][j] + 1;
	 
					int min = Math.min(rep, ins);
					min = Math.min(del, min);
					edt[i + 1][j + 1] = min;
				}
			}
		}
		return edt[length1][length2];
	}

	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map)
	{
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer> > olist = new LinkedList<>(map.entrySet());

		// Sort the list
		olist.sort(Map.Entry.comparingByValue());

		// put data from sorted list to hashmap
		HashMap<String, Integer> nlist = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> temp : olist) {
			nlist.put(temp.getKey(), temp.getValue());
		}
		return nlist;
	}

	
	

}

