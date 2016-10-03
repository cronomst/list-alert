/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordsthatfollow.craigslistalert;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author kshook
 */
public class RssClient {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getSearch(String searchTerm) throws Exception
    {
        String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        URL url = new URL(baseUrl + "&query=" + encodedSearchTerm);
        InputStream in = url.openStream();
        
        return SimpleReader.read(in);
    }
}
