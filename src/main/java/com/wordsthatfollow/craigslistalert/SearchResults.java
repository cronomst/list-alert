/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordsthatfollow.craigslistalert;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author kshook
 */
public class SearchResults {

    public Map<String, SearchResult> parseSearchResults(String rssXml) throws Exception {
        HashMap<String, SearchResult> results = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NamespaceBuilder
                .newInstance("rss", "http://purl.org/rss/1.0/")
                .ns("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
                .build());
        
        ByteArrayInputStream in = new ByteArrayInputStream(rssXml.getBytes());
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);

        XPathExpression expr = xpath.compile("//rss:channel/rss:items/rdf:Seq/rdf:li/@rdf:resource");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i=0; i<nodeList.getLength(); i++) {
            String resourceUrl = nodeList.item(i).getNodeValue();
            results.put(resourceUrl, this.createSearchResultForResource(doc, xpath, resourceUrl));
        }

        return results;
    }
    
    public Map<String,SearchResult> diffSearchResults(Map<String,SearchResult> oldResults, Map<String,SearchResult> newResults)
    {
        if (oldResults == null) {
            return newResults;
        }
        
        HashMap<String,SearchResult> diff = new HashMap<>();
        
        for (String key : newResults.keySet()) {
            if (oldResults.containsKey(key) == false) {
                diff.put(key, newResults.get(key));
            }
        }
        return diff;
    }
    
    private SearchResult createSearchResultForResource(Document doc, XPath xpath, String resourceUrl) throws Exception
    {
        SearchResult result = new SearchResult();
        String xpathBase = String.format("/rdf:RDF/rss:item[@rdf:about=\"%s\"]", resourceUrl);
        
        result.title = xpath.compile(xpathBase + "/rss:title").evaluate(doc);
        result.description = xpath.compile(xpathBase + "/rss:description").evaluate(doc);
        result.link = xpath.compile(xpathBase + "/rss:link").evaluate(doc);
        
        return result;
    }
}
