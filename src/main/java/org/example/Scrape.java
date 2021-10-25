package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.Destination;
import tech.tablesaw.io.csv.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Scrape {


    public Document get_html_page(String url) throws Exception{
        Document docs = Jsoup.connect(url).get();
        Document html = Jsoup.parse(docs.html());
        return html;
    }

    public ArrayList<String> get_topic_titles(Document code){
        Elements topic_title =  code.getElementsByClass("f3 lh-condensed mb-0 mt-1 Link--primary");
        ArrayList<String> titles = new ArrayList<>();

        topic_title.forEach(e ->{
            titles.add(e.text());
        });

        return titles;
    }

    public ArrayList<String> get_topic_desc(Document code){
        Elements desc =  code.getElementsByClass("f5 color-text-secondary mb-0 mt-1");
        ArrayList<String> topic_desc = new ArrayList<>();

        desc.forEach(e ->{
            topic_desc.add(e.text());
        });

        return topic_desc;
    }

    public ArrayList<String> get_topic_urls(Document code){
        Elements urls = code.getElementsByClass("f3 lh-condensed mb-0 mt-1 Link--primary");
        ArrayList<String> topic_urls = new ArrayList<>();

        String base_url = "https://github.com";
        urls.forEach(e ->{
            topic_urls.add(base_url + e.parent().parent().select("a").attr("href"));
        });

        return topic_urls;
    }

    public int[] get_indexes(int size){
        int indexes[] = new int[size];
        for(int i=0;i<size;i++){
            indexes[i] = i;
        }
        return indexes;
    }

    public Table scrape_topics() throws Exception{
        String url = "https://github.com/topics";
        Document html = get_html_page(url);

        ArrayList<String> titles = get_topic_titles(html);
        ArrayList<String> desc = get_topic_desc(html);
        ArrayList<String> repo_url = get_topic_urls(html);
        int indexes[] = get_indexes(titles.size());

        Table topic_table = Table.create("Topics");
        DoubleColumn c1 = DoubleColumn.create("Sl_no",indexes);
        StringColumn c2 = StringColumn.create("Title",titles);
        StringColumn c3 = StringColumn.create("Description",desc);
        StringColumn c4 = StringColumn.create("Topic URL",repo_url);

        topic_table.addColumns(c1,c2,c3,c4);

        return topic_table;
    }

    public ArrayList<String> get_repo_owner_names(Document code){
        Elements repo_owner_names =  code.getElementsByClass("f3 color-fg-muted text-normal lh-condensed");

        ArrayList<String> owners = new ArrayList<>();
        repo_owner_names.forEach(e->{
            String str[] = e.text().split("/");
            owners.add(str[0].trim());
        });

        return owners;
    }

    public ArrayList<String> get_repo_names(Document code){
        Elements repo_names =  code.getElementsByClass("f3 color-fg-muted text-normal lh-condensed");

        ArrayList<String> names = new ArrayList<>();
        repo_names.forEach(e->{
            String str[] = e.text().split("/");
            names.add(str[1].trim());
        });

        return names;
    }
    public ArrayList<String> get_repo_urls(Document code){
        Elements repo_urls =  code.getElementsByClass("f3 color-fg-muted text-normal lh-condensed");

        ArrayList<String> urls = new ArrayList<>();
        String base_url = "https://github.com/";
        repo_urls.forEach(e->{
            String repo_url = base_url +  e.select("a:nth-child(2)").attr("href");
            urls.add(repo_url);
        });

        return urls;
    }
    public ArrayList<String> get_repo_stars(Document code){
        Elements repo_stars = code.getElementsByClass("social-count float-none");

        ArrayList<String> stars = new ArrayList<>();
        repo_stars.forEach(e->{
            stars.add(e.text());
        });

        return stars;
    }

    //tasks get repo page - convert into tablesaw return tables and create csv files
    public Table scrape_topics_repo(String topic_url,String topic_name) throws Exception{
        Document html = get_html_page(topic_url); //topic repos html

        ArrayList<String> repo_owners = get_repo_owner_names(html);
        ArrayList<String> repo_names= get_repo_names(html);
        ArrayList<String> repo_urls= get_repo_urls(html);
        ArrayList<String> repo_stars= get_repo_stars(html);
        int indexes[] = get_indexes(repo_owners.size());

        Table repos = Table.create(topic_name);
        IntColumn c1 = IntColumn.create("Sl_no",indexes);
        StringColumn c2 = StringColumn.create("Owners",repo_owners);
        StringColumn c3 = StringColumn.create("Stars",repo_stars);
        StringColumn c4 = StringColumn.create("Repo URL",repo_urls);

        repos.addColumns(c1,c2,c3,c4);
        return repos;
    }

    public void generateCSV(Table table,String path,String filename) throws IOException{
        String path_name = path + filename + ".csv";

        File file = new File(path_name);
        if(file.exists() == true){
            System.out.println("File name exists skipping creation of file..." + filename );
            return;
        }

        CsvWriter writer = new CsvWriter();
        writer.write(table,new Destination(file));
    }

    public void Scrape_Topics_Create_CSV(String pathname) throws Exception{
        ScrapeData sc = new ScrapeData();
        Table topics =  sc.scrape_topics();

        File folder = new File(pathname);

        try{
            if(!folder.mkdirs()){
                System.out.println("Folder already exists");
            }
            else {
                System.out.println("Folder created");
            }
        }
        catch (Exception e){
            throw  new Exception(e.getMessage());
        }

        try{
            for(int i=0;i<topics.rowCount();i++){
                String url = topics.getString(i,"Topic URL");
                String name = topics.getString(i,"Title");
                File file = new File(pathname + name + ".csv");

                if(file.exists() == false){
                    Table repos = sc.scrape_topics_repo(url,name);
                    System.out.println("Scraping topic "+ name + " : " + url);
                    sc.generateCSV(repos,pathname,name);
                }
                else{
                    System.out.println("File already exists skipping..." + name);
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Try once again to download remaining files...");
        }

    }

}


/*
        Single function
        i) get a list of topics from topics page
        ii) get list top repos for topics
        ii) create csv for each topic
 */