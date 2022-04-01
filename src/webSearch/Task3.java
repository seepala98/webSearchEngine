package webSearch;

import java.io.File;

import java.io.IOException;
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Task3 {

	public static void main(String[] args) {
		
		long start1, end1;
		
		String s="";
		
		//--------------------HTML to Text---------------------\\
		
		start1 = System.nanoTime();
        System.out.println("Start time "+start1);

		File file = new File("/Users/architsehgal/Desktop/University/ACC/Final Project/webSearchEngine/htmlFiles/");
//		File file = new File("/webSearchEngine/htmlFiles");

		System.out.println("Files: "+file);

		String listOfFiles[] = file.list();
        System.out.println("Web Page: "+listOfFiles);
		
		for(int i=0; i<listOfFiles.length; i++) {
			
	         System.out.println("Web Page: "+listOfFiles[i]);
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
		System.out.println();
		 end1 = System.nanoTime(); 
	       
	       System.out.println( "Total Time to execute: " + ( end1 - start1 ) );
		
		
	}
}


	
