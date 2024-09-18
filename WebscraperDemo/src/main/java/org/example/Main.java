//This class is a simple web scraper tool to scrape information from the University of Wisconsin's
//Library Database
//
//Author: Aiden Engel
//Date 9/16/24

package org.example;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        String URL = "https://search.library.wisc.edu";


        System.out.println("What to search for in the Library? (Quit to leave)");
        Scanner scnr = new Scanner(System.in);

        String userChoice = scnr.next();
        while (!userChoice.equals("Quit")) {

            //This creates a new client everytime the while loop iterates
            WebClient newClient = new WebClient(BrowserVersion.CHROME);
            newClient.getOptions().setCssEnabled(false);
            newClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage searchPage = newClient.getPage(URL);
            HtmlInput searchBar = (HtmlInput) searchPage.getByXPath("//input[@type='text']").get(1);
            HtmlButton submitButton = (HtmlButton) searchPage.getByXPath("//button[@type='submit']").get(1);

            //Type in the Users search and submit it to the database
            searchBar.type(userChoice);
            HtmlPage findingsPage = submitButton.click();

            //This list contains all the HTML Heading 2's which houses all the result titles
            List<HtmlHeading2> titles = findingsPage.getByXPath("//h2");

            //This list contains all the obejcts in the "Grouped" Class, This contains
            //All the data for the details of the results
            List<HtmlDefinitionList> groupedClass = findingsPage.getByXPath("//dl[@class='grouped']");

            //Just reports if no data was found on the search topic
            if(groupedClass.size() == 0){
                System.out.println("Nothing found on that topic");
            }

            //For every result, get and format the data, then print it out
            for (int i = 0; i < groupedClass.size(); i++) {
                String result = groupedClass.get(i).asNormalizedText();
                String[] splitResult = result.split("\n");
                String collected = "Title: " + titles.get(10 + i).asNormalizedText() + "\n";

                //if the data is even, there is only one way its available
                if(splitResult.length % 2 == 0) {
                    for (int j = 0; j < splitResult.length; j += 2) {
                        collected = collected += splitResult[j] + ": " + splitResult[j + 1] + "\n";
                    }
                }
                //if its odd, it has 2 ways so the formatting of the data changes slightly
                else{
                    for (int j = 0; j < splitResult.length - 3; j += 2) {
                        collected = collected += splitResult[j] + ": " + splitResult[j + 1] + "\n";
                    }
                    collected = collected +=splitResult[splitResult.length - 3] +
                            ": " + splitResult[splitResult.length -2]+ ", "+
                            splitResult[splitResult.length-1] + "\n";
                }
                System.out.println(collected);

            }

            //Repeat the process until the user says to stop
            System.out.println("Anything else to search for in the Library? (Quit to leave)");
            userChoice = scnr.next();
        }
    }


}