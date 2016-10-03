package com.wordsthatfollow.craigslistalert;

import java.util.HashMap;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author kshook
 */
public class NamespaceBuilder {
    private final HashMap<String,String> namespaces;
    
    private NamespaceBuilder()
    {
        namespaces = new HashMap<>();
    }
    
    public static NamespaceBuilder newInstance(String prefix, String uri)
    {
        NamespaceBuilder builder = new NamespaceBuilder();
        builder.namespaces.put(prefix, uri);
        return builder;
    }
    
    public NamespaceBuilder ns(String prefix, String uri)
    {
        namespaces.put(prefix, uri);
        return this;
    }
    
    public NamespaceContext build()
    {
        return new NamespaceContext()
        {

            @Override
            public String getNamespaceURI(String prefix) {
                if (namespaces.containsKey(prefix)) {
                    return namespaces.get(prefix);
                }
                return XMLConstants.NULL_NS_URI;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                throw new UnsupportedOperationException("Not supported.");
            }

            @Override
            public Iterator getPrefixes(String namespaceURI) {
                throw new UnsupportedOperationException("Not supported.");
            }
            
        };
    }
}
