package webSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.searchengine.helper.PriorityQueue;

public class Crawler {
	public static HashMap<String, String> crawledUrls = new HashMap<String, String>();

	// crawler method to take inputs as url, depth and return the unique urls
	public static HashSet<String> crawler(String url, int depth) throws IOException {
		HashSet<String> uniqueUrls = new HashSet<>();
		HashSet<String> urls = new HashSet<>();
		urls.add(url);
		uniqueUrls.add(url);
		for (int i = 0; i < depth; i++) {
			HashSet<String> temp = new HashSet<>();
			for (String s : urls) {
				try {
					Document doc = Jsoup.connect(s).ignoreContentType(true).get();
					Elements links = doc.select("a[href]");
					for (Element link : links) {
						String linkHref = link.attr("href");
						if (linkHref.startsWith("http")) {
							temp.add(linkHref);
							uniqueUrls.add(linkHref);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			urls = temp;
		}
		return uniqueUrls;
	}

	// get the html content of the url and create html files and store it in the
	// folder
	public static String getHtml(String url) {
		// create a new html file
		String html = "";
		try {
			// get the html content of the url
			html = Jsoup.connect(url).get().html();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

	// method to remove folder and files and create a new one
	public static void removeFolder(String folderName) {
		File folder = new File(folderName);
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}

	// method to create a folder
	public static void createFolder(String folderName) {
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdir();
		}
	}

	// method to write the html content to the file
	public static String writeToFile(String folderName, String fileName, String html) {
		String filePath = null;
		try {
			filePath = folderName + "/" + fileName.replace(" ", "") + ".html";
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(html);
			fileWriter.close();
		} catch (IOException e) {
			filePath = null;
			e.printStackTrace();
		}
		return fileName.replace(" ", "") + ".html";
	}

	// download html content and store it in the folder
	public static String downloadHtml(String html) throws IOException {
		Document doc = Jsoup.parse(html);
		String title = doc.title();
		return writeToFile("htmlFiles", title, html);

	}

	private static boolean shouldCrawlUrl(String nextUrl, String baseUrl) {
		if (nextUrl.startsWith("javascript:")) {
			return false;
		}
		if (nextUrl.contains("mailto:")) {
			return false;
		}
		if (nextUrl.contains("#") || nextUrl.contains("?")) {
			return false;
		}
		String[] exception_extensions = { ".swf", ".txt", ".pdf", ".png", ".gif", "jpg", ".jpeg" };
		for (String extension : exception_extensions) {
			if (nextUrl.endsWith(extension)) {
				return false;
			}
		}
		// condition to check if the url is of specific domain
		if (!nextUrl.contains(baseUrl)) {
			return false;
		}
		return true;
	}

	// method to get the domain name of the url
	public static String getDomainName(String url) {
		String domainName = "";
		try {
			URI uri = new URI(url);
			domainName = uri.getHost();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return domainName;
	}

	// method to check if the url is correct
	public static boolean isUrlCorrect(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		//create a set to store the unique urls
		HashSet<String> uniqueUrls = null;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the url to crawl");
		String user_url = scanner.next();
		String domainName = getDomainName(user_url);
		System.out.println(" the domain name is " + domainName);
		if (isUrlCorrect(user_url)) {
			uniqueUrls = crawler(user_url, 2);
		} else {
			System.out.println("Invalid url");
		}
		removeFolder("htmlFiles");
		createFolder("htmlFiles");
		removeFolder("Text");
		createFolder("Text");
		assert uniqueUrls != null;
		int count = 0;
		for (String url : uniqueUrls) {
			if (shouldCrawlUrl(url, domainName) && count < 80) {
				System.out.println(url);
				String html = getHtml(url);
				String fileName = downloadHtml(html);
				crawledUrls.put(fileName, url);
				count++;
			}
		}
		//print the unique urls
		System.out.println(uniqueUrls);
		
		//HtmltoText
		HtmltoText.convertHtmlToText();
		
		//
		showPageRanking();
	}
	
	/**
	 * @author rajpatel
	 * @since 25-02-2022
	 * This method is used rank pages based on occurrences of input word
	 */
	public static void showPageRanking() {
		PageRanking ranking = new PageRanking(crawledUrls);
		Scanner scanner = new Scanner(System.in);
		System.out.println("*** Enter the word to be searched ***");
		String input = scanner.next();
		PriorityQueue<Integer, String> pq = null;
		try {
			pq = PageRanking.getWordCountFromAllFiles(input);
			RankingPayload[] rankedPageMetadata = PageRanking.convertToRankPayload(pq);
			if(rankedPageMetadata.length>0) {
			for (int i = 0; i < rankedPageMetadata.length && i<10; i++) {
				System.out.println((i + 1) + " Rank goes to - " + crawledUrls.get(rankedPageMetadata[i].getName()));
				System.out.println("Occurences of word in " + rankedPageMetadata[i].getName() +  " is/are --> " + rankedPageMetadata[i].getRank());
				System.out.println("________________________________________________");
			}
			}else {
				System.out.println(" Word :- " + input + " is not found in any of the files so printing first 10 links");
				Iterator it = crawledUrls.entrySet().iterator();
				int counter = 0;
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println((counter + 1) + " Rank goes to - " + pair.getValue());
			        it.remove(); // to avoid a ConcurrentModificationException
			        counter++;
			        if(counter == 10) {
			        	break;
			        }
			    }
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Error Occured while calculating page ranking");
			System.out.println(e.getMessage());
		}
		
		System.out.println("*** Finished ranking *** ");
	}
}