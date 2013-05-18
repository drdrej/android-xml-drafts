package com.touchableheroes.android.xml.parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.touchableheroes.android.log.Logger;

public class XMLTagFacade {

	private final XmlPullParser parser;

	public XMLTagFacade(
			final XmlPullParser parser) {
		this.parser = parser;
	}
	
	public String tagName() {
		return parser.getName();
	}

	public int next() {
		try {
			return parser.next();
		} catch (final Throwable x) {
			throw new IllegalStateException(
				"Couldn't exec next() on parser.", x);
		}
	}
	
	/**
	 * reads the text/value of a tag.
	 * 
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected String readText() throws IOException, XmlPullParserException {
		final StringBuilder result = new StringBuilder(100);

		if (parser.next() == XmlPullParser.TEXT) {
			result.append(parser.getText());
		}

		return result.toString().trim();
	}

	/**
	 * Reads and converts a value of a tag to int. if couldn't parse value,
	 * returs passed default-value.
	 * 
	 * @param defaultVal
	 * @return
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected int readTextInt(int defaultVal) throws XmlPullParserException,
			IOException {
		final String txt = readText();

		try {
			return Integer.parseInt(txt);
		} catch (final NumberFormatException exc) {
			return defaultVal;
		}
	}

	/**
	 * reads URI from text-value of a tag.
	 * 
	 * @return null
	 * 
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected URI readURI() throws IOException, XmlPullParserException {
		final String txt = readText();
		try {
			return new URI(txt);
		} catch (final URISyntaxException exc) {
			Logger.error("Couldn't read/parse URI: " + txt);
			return null;
		}
	}

	/**
	 * gets a string-value of an attribute.
	 * 
	 * @param attrName
	 * @return
	 */
	public String getAttribute(final String fullAttrName) {
		final int length = parser.getAttributeCount();

		for (int i = 0; i < length; i++) {
			final String name = parser.getAttributeName(i);

			if (fullAttrName.equals(name))
				return parser.getAttributeValue(i);
		}

		return null;
	}

	public String getAttribute(final String ns, final String name) {
		final String fullAttrName;

		if (ns == null)
			fullAttrName = name;
		else
			fullAttrName = ns + ":" + name;

		return getAttribute(fullAttrName);
	}

	/**
	 * Reads Boolean from text-value.
	 * 
	 * @return true or false.
	 */
	protected boolean readBoolean() {
		try {
			final String txt = readText();
			return Boolean.parseBoolean(txt);
		} catch (final Throwable exc) {
			return false;
		}
	}
	

	/**
	 * skip tags to ignore. based on googles examples
	 * 
	 * @param current
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public void skip() {
		int depth = 1;

		while (depth != 0) {
			switch (next()) {

			case XmlPullParser.END_TAG:
				depth--;
				break;

			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
