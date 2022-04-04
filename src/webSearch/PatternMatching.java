package webSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import textprocessing.KMP;

public class PatternMatching {

	public static void PatternMatching()
	{
		while(true)
		{
			Scanner scan = new Scanner(System.in);
			
			System.out.println("-------------------------------------------------------");
			
			int numOfOccurrence = 0;
			int pageCount = 0;
			System.out.println("Would you like to search the contact details: ");
			String userChoice = scan.nextLine();
			Hashtable<String, Integer> listOfOccurences = new Hashtable<String, Integer>();
			File folder = new File("Text/");
			
			File[] files = folder.listFiles();
			
			if(userChoice.equals("yes"))
			{
				System.out.println("Enter email ID domain or area code of the Phone number you would like to search: ");
				String SearchKey = scan.nextLine();
				ArrayList<String> emailPattern = new ArrayList<String>();
				ArrayList<String> phPattern = new ArrayList<String>();
					
				if(SearchKey.contains("@"))//EMAIL PATTERN
				{
					String email = "^[A-Z0-9._%+-]+" + SearchKey;
					for(File file : files) {
						if(!file.isDirectory()) {
							String fileText = GetTextFromFile(file);
							Matcher emailMatch = Pattern.compile(email, Pattern.CASE_INSENSITIVE).matcher(fileText);
							while (emailMatch.find()) {
								emailPattern.add(file.getName());
							}
						}
					}
					
					System.out.println("Below files contain the email with domain " + SearchKey);
					for(String fileName : emailPattern)
					{
						System.out.println(fileName);
					}
				}
				else//MOBILE NUMBER 
				{
					String phNumber = "^" + SearchKey + "+(\\d{7}|(?:\\(?\\d{3}\\)?[-\\s]?){2}(\\d{4}))";
					for(File file : files) {
						if(!file.isDirectory()) {
							String fileText = GetTextFromFile(file);
							Matcher pattern = Pattern.compile(phNumber, Pattern.CASE_INSENSITIVE).matcher(fileText);
							while (pattern.find()) {
								phPattern.add(file.getName());
							}
						}
					}
					
					System.out.println("Below files contain the phone numbers with area code " + SearchKey);
					for(String fileName : phPattern)
					{
						System.out.println(fileName);
					}
				}
				
			}
			else
			{
				try {
					
					System.out.println("Enter the word you would like to search: ");
					String SearchKey = scan.nextLine();
				
					for (int i = 0; i < files.length; i++) {
						numOfOccurrence = PatternSearch(files[i], SearchKey); // searching word for the user input
						
						if (numOfOccurrence != 0)
						{
							pageCount++;
							listOfOccurences.put(files[i].getName(), numOfOccurrence);
						}
					}
					System.out.println("\n----------------------------------------------------------------");
		
					if (pageCount == 0) {
						System.out.println("\n\n--------------------------------------------------------------");
						System.out.println("Searched word is not found!!!");
					} else {
						
						System.out.println("Searched word is found!!!");
						listOfOccurences.forEach(
					            (k, v) -> System.out.println("Key : " + k + ", Value : " + v));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			System.out.println("\n\n Would you like to search something else(yes/no)?");
			userChoice = scan.nextLine();
			if(userChoice == "no")
			{
				break;
			}
		}
	}
		
	public static String GetTextFromFile(File f) {
		StringBuilder stringBuilder = new StringBuilder();
		String lines;
		try {
		BufferedReader bufferReader = new BufferedReader(new FileReader(f));
		while ((lines = bufferReader.readLine()) != null) {
			stringBuilder.append(lines);
		}
		bufferReader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	public static int PatternSearch(File filePath, String searchPattern)
	{
		int occurrences = 0;
		String fileContent="";
		try
		{
			BufferedReader bufReader = new BufferedReader(new FileReader(filePath));
			String line = null;
			
			while ((line = bufReader.readLine()) != null){
				fileContent= fileContent+line;//GET THE FILE CONTENTS AS A STRING
			}
			bufReader.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception:" + ex);
		}
		// LOOKING FOR WORD OFFSET
		String fileContents = fileContent; 
			
		int offset = 0;
		
		for (int location = 0; location <= fileContents.length(); location += offset + searchPattern.length()) 
		{
			offset = search(searchPattern, fileContents.substring(location)); //SEARCH USING THE KMP ALGORITHM
			if ((offset + location) < fileContents.length()) {
				occurrences++;
			}
		}
		return occurrences;
	}

	public static int search(String pattern, String word) 
	{
	
			try
			{
				KMP kmp = new KMP(pattern);
				int offset = kmp.search(word);
				return offset;
			}
			catch(Exception e)
			{
				return 0;
			}
	}
	
	
	public static void main(String[] args)
	{
		PatternMatching();
	}
}
