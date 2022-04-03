package webSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Scanner;

import textprocessing.KMP;

public class PatternMatching {

	public static void PatternMatching()
	{
		while(true)
		{
			Scanner scan = new Scanner(System.in);
			System.out.println("-------------------------------------------------------");
			System.out.println("Enter the word you would like to search: ");
			String SearchKey = scan.nextLine();
			int numOfOccurrence = 0;
			int pageCount = 0;
			String userChoice = "yes";
			Hashtable<String, Integer> listOfOccurences = new Hashtable<String, Integer>();
			
			try {
				File folder = new File("C:\\Users\\srava\\eclipse-workspace\\Assignement4_110072772\\src\\W3C Web Pages\\Text");
	
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					numOfOccurrence = wordSearch(files[i], SearchKey); // searching word for the user input
					
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
			System.out.println("\n\n Would you like to search something else(yes/no)?");
			userChoice = scan.nextLine();
			if(userChoice == "no")
			{
				break;
			}
		}
	}
		
	public static int wordSearch(File filePath, String word)
	{
		int occurrences = 0;
		String fileContent="";
		try
		{
			BufferedReader bufReader = new BufferedReader(new FileReader(filePath));
			String line = null;
			
			while ((line = bufReader.readLine()) != null){
				fileContent= fileContent+line;
			}
			bufReader.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception:" + ex);
		}
		// LOOKING FOR THE POSITION OF THE WORD
		String fileContents = fileContent; 
			
		int offsetw = 0;
		
		for (int location = 0; location <= fileContents.length(); location += offsetw + word.length()) 
		{
			offsetw = search(word, fileContents.substring(location)); 
			if ((offsetw + location) < fileContents.length()) {
				occurrences++;
			}
		}
		return occurrences;
	}

	public static int search(String pattern, String word) {
	
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
