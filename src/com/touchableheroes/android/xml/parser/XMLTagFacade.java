package com.touchableheroes.android.xml.parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.touchableheroes.android.log.Logger;

public class XMLTagFacade {

	private final XmlPullParser parser;
	
	private final Attributes attributes;
	
	private String currentTxt;

	public XMLTagFacade(final XmlPullParser parser) {
		this.parser = parser;
		this.attributes = new Attributes(parser);
	}

	public String tagName() {
		return parser.getName();
	}

	public int next() {
		try {
			return parser.next();
		} catch (final Throwable x) {
			throw new IllegalStateException("Couldn't exec next() on parser.",
					x);
		}
	}

	/**
	 * reads the text/value of a tag.
	 * 
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public String readText() throws IOException {
		if (currentTxt == null)
			throw new IllegalStateException(
					"Tag must ACCEPT text to call this function in parser.");

		return this.currentTxt;
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
	public int readTextInt(int defaultVal) throws XmlPullParserException,
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
	public URI readURI() throws IOException, XmlPullParserException {
		final String txt = readText();
		try {
			return new URI(txt);
		} catch (final URISyntaxException exc) {
			Logger.error("Couldn't read/parse URI: " + txt);
			return null;
		}
	}


	/**
	 * Reads Boolean from text-value.
	 * 
	 * @return true or false.
	 */
	public boolean readBoolean() {
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
		final String tagName = tagName();
		
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

	
	/**
	 * use to extract next text
	 */
	public void nextText() {
		final int currentState = next();

		if (currentState == XmlPullParser.TEXT) {
			currentTxt = parser.getText();
		} else {
			currentTxt = "";
		}
	}
	
	public void useDefaultText() {
		currentTxt = "";
	}

	public boolean isEndTag() {
		try {
			return (parser.getEventType() == XmlPullParser.END_TAG);
		} catch (final XmlPullParserException e) {
			throw new IllegalStateException("Couldn't check event-type.", e);
		}
	}

	public boolean isStartTag() {
		try {
			return (parser.getEventType() == XmlPullParser.START_TAG);
		} catch (final XmlPullParserException e) {
			throw new IllegalStateException("Couldn't check event-type.", e);
		}
	}

	public void resetText() {
		currentTxt = null;
	}

	public void resetAttributes() {
		attributes.resetAttributes();
	}

	public void catchAttributes() {
		attributes.catchAttributes();
	}
	
	public String getAttribute(final String fullAttrName) {
		return attributes.getAttribute(fullAttrName);
	}


}
