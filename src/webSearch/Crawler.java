package webSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import  org.jsoup.Jsoup;
import  org.jsoup.nodes.Document;
import  org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    //crawler method to take inputs as url, depth and return the unique urls
    public static HashSet<String> crawler(String url, int depth) {
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

    // get the html content of the url and create html files and store it in the folder
    public static String getHtml(String url){
        //create a new html file
        String html = "";
        try {
            //get the html content of the url
            html = Jsoup.connect(url).get().html();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    //method to remove folder and files and create a new one
    public static void removeFolder(String folderName){
        File folder = new File(folderName);
        File[] files = folder.listFiles();
        if(files!=null){
            for(File f: files){
                f.delete();
            }
        }
    }

    //method to create a folder
    public static void createFolder(String folderName){
        File folder = new File(folderName);
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    //method to write the html content to the file
    public static void writeToFile(String folderName, String fileName, String html){
        try {
            FileWriter fileWriter = new FileWriter(folderName + "/" + fileName + ".html");
            fileWriter.write(html);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //download html content and store it in the folder
    public static void downloadHtml(String html) throws IOException {
        Document doc = Jsoup.parse(html);
        String title = doc.title();
        writeToFile("htmlFiles", title, html);
    }

    private static boolean shouldCrawlUrl(String nextUrl) {
        if (nextUrl.startsWith("javascript:")) {
            return false;
        }
        if (nextUrl.contains("mailto:")) {
            return false;
        }
        if (nextUrl.contains("#") || nextUrl.contains("?")) {
            return false;
        }
        String[] exception_extensions = {".swf", ".txt", ".pdf", ".png", ".gif", "jpg", ".jpeg"};
        for (String extension : exception_extensions) {
            if (nextUrl.endsWith(extension)) {
                return false;
            }
        }
        return true;
    }

    //method to check if the url is correct
    public static boolean isUrlCorrect(String url){
        if(url.startsWith("http://") || url.startsWith("https://")){
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
        if (isUrlCorrect(user_url)) {
            uniqueUrls = crawler(user_url, 3);
        }
        else {
            System.out.println("Invalid url");
        }
        //call the crawler method to get the unique urls
//        uniqueUrls = crawler(user_url, 3);
        removeFolder("htmlFiles");
        createFolder("htmlFiles");
        for (String url : uniqueUrls) {
            if (shouldCrawlUrl(url)) {
                System.out.println(url);
                String html = getHtml(url);
                downloadHtml(html);
            }
        }
        //print the unique urls
        System.out.println(uniqueUrls);
    }
}