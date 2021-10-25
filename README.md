# Scraping the repositories on the Topics on Github:
TODO : 
Introduction about Web Scrapping
Introduction about Github Topics and the problem statement
Mention the tools (Java, Jsoup, table saw, IntelliJ Idea)

OUTLINE:
1) We are going to scrape from https://github.com/topics -
2) We will get lists of topics for each topic we will title, topic page URL, and description
3)For each repo we will grab repo_name, username, repo URL, stars
4) for each topic we will create csv file format:
        Repo Name,Username,Stars,Repo URL
        three.js,mrdoob,669700,https://github.com/mrdoob/three.js
        libgdx,libgdx,19100,https://github.com/libgdx/libgdx
        

## Scrape List of Topics from Github
    - With the help of Java, the Jsoup library get requests to download the Github topic page <br/>
    - Use Jsoup. parse() to parse the downloaded page into HTML <br/>
    - convert HTML into table saw data frame

## Scrape list of Repositories for each topic
    - The data frame that we created above get the (topic URL, topic name) for which you want to extract repos <br/>
    - again, requests to download page of a particular topic <br/>
    - parse the download page <br/>
    - Extracts lists of data (repo_name, username, repo URL, stars) from the page.
        convert it into tablesaw data frame

## Create CSV file for each Topic
    - The data frame that we have created for each topic convert into csv file. <br/>
    - Use csvWriter for creating csv files from each topic data frame

###### ** In main function take input as folder path for storing the csv Files ** 