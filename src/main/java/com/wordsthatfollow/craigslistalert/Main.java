/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordsthatfollow.craigslistalert;

/**
 *
 * @author kshook
 */
public class Main {
    private String searchTerm;
    private String cacheFilename;
    
    public static void main(String[] args)
    {
        /* Command line arguments
        -s <search term>
        -c <cache filename>
        */
        
        for (int i=0; i<args.length; i++)
        {
            System.out.println(String.format("%d [%s]", i, args[i]));
        }
        
//        Main main = new Main();
//        main.start();
    }
    
    private void init()
    {
        
    }
    
    private void start()
    {
        try {
            RssClient client = new RssClient();
            client.setBaseUrl("https://orlando.craigslist.org/search/sss?format=rss&sort=rel");
            String search = client.getSearch("dreamcast");
            
            System.out.println(search);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
