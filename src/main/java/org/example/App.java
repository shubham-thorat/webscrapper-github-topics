
package org.example;


import java.util.*;

public class App {

    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        Scrape webScrapper = new Scrape();

        String path = sc.nextLine();
        webScrapper.Scrape_Topics_Create_CSV(path);
    }
}



/*
    1) Pick a website and describe your objective

    Browse through different sites and pick on to scrape. Check the "Project Ideas" section for inspiration.
    Identify the information you'd like to scrape from the site. Decide the format of the output CSV file.
    Summarize your project idea and outline your strategy in a Juptyer notebook. Use the "New" button above.
*/


//2) Use the requests library to download web pages
//3) Use Beautiful Soup to parse and extract information
//4) Create CSV file(s) with the extracted information
//5) Document and share your work




//PROJECT OUTLINE
//1) going to scrape from https://github.com/topics -
//      * gets all lists of topics we will get topic title,topic page url and description

    //for each topic we will create csv file format:
        /*
        Repo Name,Username,Stars,Repo URL
        three.js,mrdoob,669700,https://github.com/mrdoob/three.js
        libgdx,libgdx,19100,https://github.com/libgdx/libgdx
         */


//      For each topic we will get top 25 repository from topic page
//          For each repo we will get RepoName, Username,stars,Repo URL,description



/*
    1) Write a single function
     i) Get the list of topics from topic page
     ii)Get the list of tops repos from individual topic pages
     iii) For each topic created csv of tops repos of topic
 */