package webSearch;

import java.io.File;

import java.io.IOException;
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.searchengine.helper.In;



public class HtmltoText {
	
	public static void convertHtmlToText() {
		String s="";
		
		//--------------------HTML to Text---------------------\\
		
		File file = new File("htmlFiles/");

        System.out.println("Files location : "+file);

        System.out.println("\n********* Parsing HTML ********");


		String listOfFiles[] = file.list();
		
		for(int i=0; i<listOfFiles.length; i++) {
			
	         System.out.println("\nWeb Page: "+listOfFiles[i]);
	         In in = new In("htmlFiles/"+listOfFiles[i]);
	 		
	         
	       //--------------------Create File---------------------\\
	         
         try {
             File create = new File("Text/"+listOfFiles[i]+".txt");
             if (create.createNewFile()) {
               System.out.println("Text File:  '" + create.getName()+"'- Created");
               System.out.println(" ");
             } else {
               System.out.println("Text File: '" + create.getName()+"'- already exists.");
               System.out.println(" ");

             }
           } catch (IOException e) {
             System.out.println("An error occurred.");
             e.printStackTrace();
           }
         s ="";
	         
	         
	       //--------------------Write File---------------------\\
	         
	         
	         try {    
	         
	        	 FileWriter write = new FileWriter("Text/"+listOfFiles[i]+".txt");
	 		while (!in.isEmpty()) {
	 			s ="";
	 			Document document = Jsoup.parse(in.readLine());
	 			s = document.text();
	 			write.write(s+"\n");
	 		}
	 			write.close();
	 	    
	 		
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}  
     
	      }
		System.out.println();
		System.out.println();
		System.out.println("******* Parsing Done ! ********");
	       
		
	}
	public static void main(String[] args) {
	}
}


	
