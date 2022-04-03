package webSearch;

import java.io.File;

import java.io.IOException;
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HtmltoText {

	public static void main(String[] args) {
				
		String s="";
		
		//--------------------HTML to Text---------------------\\
		

		File file = new File("/Users/architsehgal/Desktop/University/ACC/Final Project/webSearchEngine/htmlFiles/");

		System.out.println("Taking Files from : "+file);

		String listOfFiles[] = file.list();
        System.out.println("\n****** Converting HTML to Text ********");
		System.out.println();
		System.out.println();

		
		for(int i=0; i<listOfFiles.length; i++) {
			
	         System.out.println("HTML Page: "+listOfFiles[i]);
	         In in = new In("/Users/architsehgal/Desktop/University/ACC/Final Project/webSearchEngine/htmlFiles/"+listOfFiles[i]);
	 		
	         
	       //--------------------Create File---------------------\\
	         
         try {
             File create = new File("/Users/architsehgal/Desktop/University/ACC/Final Project/webSearchEngine/Text/"+listOfFiles[i]+".txt");
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
	         
	        	 FileWriter write = new FileWriter("/Users/architsehgal/Desktop/University/ACC/Final Project/webSearchEngine/Text/"+listOfFiles[i]+".txt");
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
		System.out.println("**** Converted all HTML files to Text ******* ");	       
		
		
	}
}


	
