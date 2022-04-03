package webSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.searchengine.helper.Entry;
import com.searchengine.helper.PriorityQueue;
import com.searchengine.helper.SortedPriorityQueue;
import com.searchengine.helper.TST;

/**
 * 
 * @author rajpatel This class is used to rank web pages using different
 *         algorithms and criteria.
 * @since 25-03-2022
 */
public class PageRanking {

	public HashMap<String, String> crawledUrlAndFileName;

	public PageRanking(HashMap<String, String> crawledUrlAndFileName) {
		this.crawledUrlAndFileName = crawledUrlAndFileName;
	}

	public static PriorityQueue<Integer, String> getWordCountFromAllFiles(String scan) throws IOException {
		final String txtPath = "Text/";
		final String htmlPath = "htmlFiles/";
		File txt = new File(txtPath);
		File[] textFile = txt.listFiles();
		File html = new File(htmlPath);
		File[] htmlFiles = html.listFiles();
		PriorityQueue<Integer, String> pq = null;
		
		if (textFile.length != 0 && htmlFiles.length != 0 && (textFile.length == htmlFiles.length)) {
			pq = new SortedPriorityQueue<>();
			// scan new files
			for (int i = 0; i < textFile.length; i++) {
				if (textFile[i].isFile()) {
					TST<Integer> tst = new TST<Integer>();
					String path = (txtPath + textFile[i].getName());
					setFrequency(path, tst);
					if (tst.get(scan) != null) {
						// store occurrence and web name in priority queue
						pq.insert(tst.get(scan), htmlFiles[i].getName());
					}
				}
			}
		}
		return pq;
	}
	
	/**
	 * 
	 * @param pq
	 * @return
	 * @throws IOException
	 * This helper method is used
	 */
	public static RankingPayload[] convertToRankPayload(PriorityQueue<Integer, String> pq) throws IOException {
		RankingPayload[] queryResults = new RankingPayload[pq.size()];
		Iterator<Entry<Integer, String>> QueueIterator = pq.iterator();
		int flag = 0;
		while (QueueIterator.hasNext()) {
			Entry<Integer, String> entry = QueueIterator.next();
			RankingPayload payload = new RankingPayload(entry.getKey(), entry.getValue());
			queryResults[(pq.size() - 1) - flag] = payload;
			flag++;
		}
		return queryResults;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param tst
	 * @throws IOException
	 * This method is used to set frequecy of each word to TST data structure. 
	 */
	public static void setFrequency(String fileName, TST<Integer> tst) throws IOException {
		StringBuffer sb = new StringBuffer();
		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
//			TST<Integer> tst = new TST<Integer>();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		String content = sb.toString();
		
		//  get word by use of Tokenizer
		StringTokenizer tokenizer = new StringTokenizer(content, " ,`*$|~(){}_@><=+[]\\?;/&#<-.!:\"\"''\n");
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			if (tst.contains(word)) {
				int count = tst.get(word);
				tst.put(word, count + 1);
			} else {
				tst.put(word, 1);
			}
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PriorityQueue<Integer, String> pq = getWordCountFromAllFiles("the");
			if (pq != null) {
				RankingPayload[] docs = convertToRankPayload(pq);
				for (int i = 0; i < docs.length; i++) {
					System.out.println("Rank of the file :- " + docs[i].getName() + " is :- " + docs[i].getRank());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
