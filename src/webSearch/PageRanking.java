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

	public static PriorityQueue<Integer, String> occurrences(String scan) throws IOException {
		String txtPath = "Text/";
		String htmlPath = "htmlFiles/";

		File txt = new File(txtPath);
		File[] Files = txt.listFiles();
		File html = new File(htmlPath);
		File[] Webs = html.listFiles();
		PriorityQueue<Integer, String> pq = null;
		
		if (Files.length != 0 && Webs.length != 0) {
			pq = new SortedPriorityQueue<>();
			// scan new files
			for (int i = 0; i < Files.length; i++) {
				if (Files[i].isFile()) {
					TST<Integer> tst = new TST<Integer>();
					String path = (txtPath + Files[i].getName());
					scanFile(path, tst);// get occurrence from matching given word
					if (tst.get(scan) != null) {
						// store occurrence and web name in priority queue
						pq.insert(tst.get(scan), Webs[i].getName());
					}
				}
			}
		}
		return pq;
	}

	public static RankingPayload[] queue2List(PriorityQueue<Integer, String> pq) throws IOException {
		RankingPayload[] queryResults = new RankingPayload[pq.size()];
		Iterator<Entry<Integer, String>> s = pq.iterator();
		int flag = 0;
		while (s.hasNext()) {
			Entry<Integer, String> tmp = s.next();
			RankingPayload doc = new RankingPayload(tmp.getKey(), tmp.getValue());
			queryResults[(pq.size() - 1) - flag] = doc;
			flag++;
		}
		return queryResults;
	}

	public static void scanFile(String fileName, TST<Integer> tst) throws IOException {
		StringBuffer sb = new StringBuffer();
		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
//			TST<Integer> tst = new TST<Integer>();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		String article = sb.toString();
		// split ,*)?;/&#<-.!:\"\"''\n to get word by use Tokenizer
		StringTokenizer st = new StringTokenizer(article, " ,`*$|~(){}_@><=+[]\\?;/&#<-.!:\"\"''\n");
		while (st.hasMoreTokens()) {
			String word = st.nextToken(); // return string until next token;
			if (tst.contains(word)) {
				int count = tst.get(word);
				tst.put(word, count + 1);
			} else {
				tst.put(word, 1);
			}
		}
	}

	public static void main(String[] args) {
		try {
			PriorityQueue<Integer, String> pq = occurrences("the");
			if (pq != null) {
				RankingPayload[] docs = queue2List(pq);
				for (int i = 0; i < docs.length; i++) {
					System.out.println("Rank of the file :- " + docs[i].getName() + " is :- " + docs[i].getRank());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
