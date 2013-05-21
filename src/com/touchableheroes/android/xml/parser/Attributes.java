package com.touchableheroes.android.xml.parser;

import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class Attributes {

	private Map<String, String> currentAttributes = new HashMap<String, String>();
	private final XmlPullParser parser;

	public Attributes(final XmlPullParser parser) {
		this.parser = parser;
	}

	public void catchAttributes() {
		final int size = parser.getAttributeCount();
		for (int i = 0; i < size; i++) {
			final String key = buildKey(i);
			final String value = parser.getAttributeValue(i);

			this.currentAttributes.put(key, value);
		}
	}

	private String buildKey(final int index) {
		final String name = parser.getAttributeName( index );
		final String ns = parser.getAttributeNamespace( index );
		
		final boolean isEmptyNS = ns == null || ns.trim().length() < 1;
		if( isEmptyNS )
			return name;
		
		return ns + ":" + name;
		
	}

	public void resetAttributes() {
		this.currentAttributes.clear();
	}

	/**
	 * gets a string-value of an attribute.
	 * 
	 * @param attrName
	 * @return
	 */
	public String getAttribute(final String fullAttrName) {
		return currentAttributes.get(fullAttrName);
	}

}
