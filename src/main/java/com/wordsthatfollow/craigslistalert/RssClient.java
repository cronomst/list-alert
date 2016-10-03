package com.wordsthatfollow.craigslistalert;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author kshook
 */
public class RssClient {
    private String urlTemplate;

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }
    
    public String getSearch(String location, String searchTerm) throws Exception
    {
        String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        String urlString = String.format(urlTemplate,
                location,
                encodedSearchTerm);
        URL url = new URL(urlString);
        InputStream in = url.openStream();
        
        return SimpleReader.read(in);
    }
}
