package com.touchableheroes.android.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.touchableheroes.android.log.Logger;


/**
 * An example how to parse XML with pull-parser in android.
 * 
 * This class is based on XmlPullParser (andoird-sdk, v1)
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 *
 */
public abstract class AbstractXMLPullParser<C extends Callback> {

	private final State state = new State();
	private final XmlPullParser parser;
	private final Tag root;

	public AbstractXMLPullParser(final Tag root) throws XmlPullParserException {
		if( root == null )
			throw new IllegalArgumentException( "passed ROOT-Tag is NULL." );
		
		this.root = root;
		this.parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
	}

	/**
	 * Parse and close the stream
	 * 
	 * @param in
	 *            not NULL
	 */
	public void parse(final InputStream in,
			final C callback) {
		try {
			parser.setInput(in, "UTF-8");
			readFeed(callback);
		} catch (final Throwable tx) {
			System.out.println("Couldn't read feed.xml");
			tx.printStackTrace();
		} finally {
			closeInSilently(in);
		}
	}

	protected void readFeed(final C callback)
			throws Throwable {
		try {
			visitRoot(callback);
		} catch (final Throwable t) {
			t.printStackTrace();

			throw new RuntimeException(t);
		}
	}

	private void visitRoot(final C callback)
			throws Throwable {
		visitChildren(callback);

		this.state.isReady = true;
	}

	private void visitChildren(final C callback)
			throws Throwable {
		startDoc();

		int open = 0;
		int close = 0;

		while (isEndOfDoc()) {
			final Tag current = nextTag(state.parentTag());

			if (current == null)
				continue;

			switch (parser.getEventType()) {
			case XmlPullParser.END_TAG:
				closeTag(current, callback);
				getState().closeTag(current);
				
				close++;
				break;

			case XmlPullParser.START_TAG:
				startTag(current, callback);
				getState().openTag(current);
				
				open++;
				break;
			}
		}

		closeDoc(callback);

		if (Logger.isDebug()) {
			Logger.debug("-- found tags: open = " + open + " / closed = "
					+ close);
		}
	}

	public XmlPullParser getParser() {
		return parser;
	}

	public void closeDoc(final C callback) {
		;
	}

	/**
	 * find the next tag.
	 * 
	 * @param current
	 * @return
	 * @throws Throwable
	 */
	public Tag nextTag(final Tag current)
			throws Throwable {
		final Tag tag = findNextTag(current);

		if (tag == null)
			return null;

		return tag;
	}

	public Tag findNextTag(final Tag previousTag)
			throws Throwable {
		if (parser.getEventType() == XmlPullParser.END_DOCUMENT)
			return null;

		parser.next();

		while ((parser.getEventType() != XmlPullParser.END_DOCUMENT)) {
			switch (parser.getEventType()) {
			case XmlPullParser.START_TAG:
				final Tag tag = TagUtils.identify(previousTag, parser
						.getName());

				if (Logger.isDebug()) {
					Logger.debug("-- identify: parent = " + state.parentTag()
							+ " - identified: " + tag + " / "
							+ parser.getName() + " ::: "
							+ parser.getEventType());
				}

				return tag;
			case XmlPullParser.END_TAG:
				final Tag parentTag = state.parentTag();
				if (parentTag == null)
					return null;

				if (parentTag.getName().equals(parser.getName())) {
					return parentTag;
				}

				return null;
			}

			parser.next();
		}

		return null;
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

		return result.toString();
	}

	
	/**
	 * Reads and converts a value of a tag to int.
	 * if couldn't parse value, returs passed default-value.
	 * 
	 * @param defaultVal
	 * @return
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected int readTextInt(int defaultVal) throws XmlPullParserException, IOException {
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
		try {
			final String txt = readText();
			return new URI(txt);
		} catch (final URISyntaxException exc) {
			return null;
		}
	}
	
	/**
	 * gets a string-value of an attribute.
	 * 
	 * @param attrName
	 * @return
	 */
	protected String getAttribute(final String attrName) {
		final int length = parser.getAttributeCount();
		
		for( int i = 0; i < length; i++ ) {
			final String name = parser.getAttributeName(i);
			
			if( attrName.equals( name ) )
				return parser.getAttributeValue(i);
		}
		
		return null;
	}
	
	
	/**
	 * gets a string-value of an attribute.
	 * supports namespace.
	 * 
	 * @param ns
	 * @param attrName
	 * 
	 * @return
	 */
	protected String getAttribute(final String ns, final String attrName) {
		if( ns == null )
			throw new IllegalArgumentException( "Namespace is NULL" );
		
		return parser.getAttributeValue(ns, attrName);
	}

	public void startDoc() throws Throwable {
		parser.nextTag();

		final boolean isStartTag = (parser.getEventType() != XmlPullParser.START_TAG);
		if (isStartTag) {
			throw new IllegalStateException(
					"Couldn't parse document, because waiting for START_TAG, but found: "
							+ parser.getEventType());
		}

		state.openRoot( this.root );
	}

	/**
	 * @return never NULL
	 */
	public State getState() {
		return state;
	}

	private void closeInSilently(final InputStream in) {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * check state of the parser.
	 * 
	 * @return
	 * @throws Throwable
	 */
	public boolean isEndOfDoc() throws Throwable {
		return (parser.getEventType() != XmlPullParser.END_DOCUMENT);

		// if (parser.getEventType() != XmlPullParser.END_TAG)
		// return true;

		// return state.hasAllOpenTagsClosed();
	}

	
	/**
	 * callback-method is called when start-tag has been catched.
	 * 
	 * @param current
	 * @param callback
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected abstract void startTag(final Tag current,
			final C callback) throws IOException,
			XmlPullParserException;

	/**
	 * callback-method is called when close-tag has been catched.
	 *  
	 * @param current
	 * @param callback
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected abstract void closeTag(final Tag current,
			final C callback) throws IOException,
			XmlPullParserException;

}