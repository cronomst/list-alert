package com.wordsthatfollow.craigslistalert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kshook
 */
public class SearchResultsTest {
    
    private SearchResults searchResults;
    
    public SearchResultsTest() {
    }
    
    @Before
    public void setUp() {
        searchResults = new SearchResults();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testParseSearchResults() throws Exception {
        System.out.println("testParseSearchResults");
        String rssFeed = this.readResource("/rss.xml");
        
        Map<String,SearchResult> actual = this.searchResults.parseSearchResults(rssFeed);
        
        assertEquals(25, actual.size());
    }
    
    @Test
    public void testDiffSearchResults_NoChange() throws Exception
    {
        String rssFeedOld = this.readResource("/rss.xml");
        String rssFeedNew = this.readResource("/rss.xml");
        
        Map parsedOld = this.searchResults.parseSearchResults(rssFeedOld);
        Map parsedNew = this.searchResults.parseSearchResults(rssFeedNew);
        
        Map<String,SearchResult> diff = this.searchResults.diffSearchResults(parsedOld, parsedNew);
        assertEquals(0, diff.size());
    }
    
    @Test
    public void testDiffSearchResults() throws Exception
    {
        String rssFeedOld = this.readResource("/rss.xml");
        String rssFeedNew = this.readResource("/rss_new.xml");
        
        Map parsedOld = this.searchResults.parseSearchResults(rssFeedOld);
        Map parsedNew = this.searchResults.parseSearchResults(rssFeedNew);
        
        Map<String,SearchResult> diff = this.searchResults.diffSearchResults(parsedOld, parsedNew);
        assertEquals(2, diff.size());
        
        assertEquals("NEW ONE!", diff.get("new1").title);
        assertEquals("NEW TWO!", diff.get("new2").title);
    }
    
    private String readResource(String resourcePath) throws Exception
    {
        return SimpleReader.read(getClass().getResourceAsStream(resourcePath));
    }
    
}
