package com.wordsthatfollow.craigslistalert;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 * @author kshook
 */
public class Main
{

    private String searchTerm;
    private String cacheFilename;
    private String searchLocation;
    private static final String CL_URL = "https://%s.craigslist.org/search/sss?format=rss&sort=rel&query=%s";

    public static void main(String[] args)
    {
        Main main = new Main(args);
        main.start();
    }

    public Main(String[] args)
    {
        this.parseArgs(args);
        this.init();
    }

    private void parseArgs(String[] args)
    {
        int argpos = 0;
        while (argpos < args.length) {
            if ("-s".equals(args[argpos])) {
                argpos++;
                this.searchTerm = args[argpos];
            } else if ("-c".equals(args[argpos])) {
                argpos++;
                this.cacheFilename = args[argpos];
            } else if ("-l".equals(args[argpos])) {
                argpos++;
                this.searchLocation = args[argpos];
            } else {
                System.err.println("Argument '" + args[argpos] + "' not recognized.");
                this.printUsage();
                System.exit(1);
            }
            argpos++;
        }
    }
    
    private void printUsage()
    {
        System.out.println("Usage: -s <search terms> -l <location> -c <cache filename>\n");
    }

    private void init()
    {
        if (this.searchTerm == null) {
            System.err.println("No search time has been specified.");
            System.exit(1);
        }
        if (this.cacheFilename == null) {
            System.err.println("No cache file has been specified.");
            System.exit(1);
        }
        if (this.searchLocation == null) {
            System.err.println("No search location has been specified");
            System.exit(1);
        }
    }

    private void start()
    {
        SearchResults searchResults = new SearchResults();

        try {
            RssClient client = new RssClient();
            client.setUrlTemplate(CL_URL);
            String search = client.getSearch(this.searchLocation, this.searchTerm);
            Map<String, SearchResult> results = searchResults.parseSearchResults(search);
            Map<String, SearchResult> cachedResults = null;
            if (Files.exists(Paths.get(this.cacheFilename))) {
                String cache = SimpleReader.read(new FileInputStream(this.cacheFilename));
                cachedResults = searchResults.parseSearchResults(cache);
            }

            Map<String, SearchResult> diff = searchResults.diffSearchResults(cachedResults, results);
            if (diff.isEmpty() == false) {
                notify(diff);
                writeCache(search);
            } else {
                System.out.println(String.format("No new results found for search term [%s] in location [%s].",
                        this.searchTerm,
                        this.searchLocation));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    private void notify(Map<String, SearchResult> results)
    {
        String out = String.format("Results found for search term [%s] in location [%s].\n\n",
                this.searchTerm,
                this.searchLocation);

        for (SearchResult result : results.values()) {
            out += "(" + result.link + ") " + result.title + "\n";
        }

        System.out.println(out);
        this.sendEmail(String.format("List Alert: %s found in %s", this.searchTerm, this.searchLocation), out);
    }

    private void writeCache(String cacheData) throws IOException
    {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.cacheFilename))) {
            out.write(cacheData.getBytes());
            out.flush();
        }
    }

    public void sendEmail(String subject, String body)
    {
//        try {
//            Email email = new SimpleEmail();
//            email.setHostName("smtp.googlemail.com");
//            email.setSmtpPort(465);
//            email.setAuthenticator(new DefaultAuthenticator("un", "pw"));
//            email.setSSLOnConnect(true);
//            email.setFrom("from@gmail.com");
//            email.setSubject(subject);
//            email.setMsg(body);
//            email.addTo("to@gmail.com");
//            email.send();
//        } catch (EmailException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
