package com.touchableheroes.android.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.touchableheroes.android.log.Logger;
import com.touchableheroes.android.log.Timer;
import com.touchableheroes.android.xml.parser.tag.SameRuleAs;

/**
 * An example how to parse XML with pull-parser in android.
 * 
 * This class is based on XmlPullParser (andoird-sdk, v1)
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 * 
 */
public abstract class AbstractXMLPullParser<C extends Callback> implements
		TagEventHandler<C> {

	private Stack<String> names = new Stack<String>();

	private final State state = new State();
	private final XmlPullParser parser;
	private final Tag root;

	public AbstractXMLPullParser(final Tag root) throws XmlPullParserException {
		if (root == null)
			throw new IllegalArgumentException("passed ROOT-Tag is NULL.");

		this.root = root;
		this.parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);

		names.push(root.fullName());
	}

	/**
	 * Parse and close the stream
	 * 
	 * @param in
	 *            not NULL
	 */
	public void parse(final InputStream in, final C callback) {
		try {
			parser.setInput(in, "UTF-8");
			readXML(callback);
		} catch (final Throwable tx) {
			Logger.debug("Couldn't read xml-file");
			tx.printStackTrace();
		} finally {
			closeInSilently(in);
		}
	}

	protected void readXML(final C callback) throws Throwable {
		try {
			visitRoot(callback);
		} catch (final Throwable t) {
			t.printStackTrace();

			throw new RuntimeException(t);
		}
	}

	private void visitRoot(final C callback) throws Throwable {
		visitChildren(callback);

		this.state.isReady = true;
	}

	private void visitChildren(final C callback) throws Throwable {
		startDoc();

		while (isEndOfDoc()) {
			final Tag current = nextTag(state.parentTag());

			if (current == null)
				continue;

			switch (parser.getEventType()) {
			case XmlPullParser.END_TAG:
				handleCloseTagEvent(callback, current);
				break;

			case XmlPullParser.START_TAG:
				if (current.shouldSkip())
					skip(current);
				else
					handleStartTagEvent(callback, current);

				break;
			}
		}

		closeDoc(callback);
	}

	/**
	 * skip tags to ignore. based on googles examples
	 * 
	 * @param current
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private void skip(final Tag current) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException("Couldn't skip: " + current
					+ " because no start");
		}

		int depth = 1;

		while (depth != 0) {
			switch (parser.next()) {

			case XmlPullParser.END_TAG:
				depth--;
				break;

			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}

		removeSkipedCandidate(current);
	}

	private void removeSkipedCandidate(final Tag current) {
		final String candidate = lastName();

		if (candidate == null)
			return;

		names.pop();
	}

	public void handleStartTagEvent(final C callback, final Tag current)
			throws IOException, XmlPullParserException {
		getState().openTag(current);
		startTag(current, callback);
	}

	/**
	 * Replace one Tag by another - is heplful to declare tag-references in the
	 * "tag/state-grammar".
	 * 
	 * @param current
	 * 
	 * @return same Tag or another one.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Tag replaceBy(final Tag current) {
		final SameRuleAs ref = current.ruleRef();

		if (ref != null) {

			final Enum openTag = (Enum) current;
			final Enum replaceBy = Enum.valueOf(openTag.getClass(),
					ref.stateName());

			return (Tag) replaceBy;
		} else {
			return current;
		}
	}

	public void handleCloseTagEvent(final C callback, final Tag current)
			throws IOException, XmlPullParserException {
		closeTag(current, callback);
		getState().closeTag(current);
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
	public Tag nextTag(final Tag current) throws Throwable {
		final Tag tag = findNextTag(current);

		if (tag == null)
			return null;

		return tag;
	}

	public Tag findNextTag(final Tag previousTag) throws Throwable {
		final Timer methodTimer = Timer.start(this.getClass(),
				"findNextTag(Tag)");

		try {
			return execFindNextTag(previousTag);
		} finally {
			methodTimer.end(true);
		}
	}

	private Tag execFindNextTag(final Tag previousTag)
			throws XmlPullParserException, IOException {
		if (parser.getEventType() == XmlPullParser.END_DOCUMENT)
			return null;

		parser.next();

		while ((parser.getEventType() != XmlPullParser.END_DOCUMENT)) {
			switch (parser.getEventType()) {

			case XmlPullParser.START_TAG:
				return prepareStartTag(previousTag);

			case XmlPullParser.END_TAG:
				return prepareEndTag();
				
			}

			parser.next();
		}

		return null;
	}

	private Tag prepareEndTag() throws XmlPullParserException {
		final String candidate = lastName();

		if (candidate == null)
			return null;

		names.pop(); // reset stack of names

		final Tag parent = state.parentTag();

		if (parent == null)
			return null;

		System.out.println("-- ajnane </" + candidate + "> -> " + parent);

		if (candidate.equals(parent.getName())) {
			logIdentifiedCloseTag(parent);
			return parent;
		}

		return null;
	}

	private Tag prepareStartTag(final Tag previousTag)
			throws XmlPullParserException {
		final String tagName = parser.getName();
		
		if( isEmptyTag() ) {
			return null;
		}
		
		names.push(tagName);
		Tag use = null;
		try {
			use = replaceBy(previousTag);
		} catch( Throwable x ) {
			x.printStackTrace();
		}
		
		System.out.println("-- (ajnane) <" + tagName + "> :::  -> " + use
				+ " for passed parent::: " + previousTag + " --- empty: " + isEmptyTag());

		final Tag tag = TagUtils.identify(use, tagName);

		
		logIdentifiedStartTag(tag);
		return tag;
	}

	private boolean isEmptyTag() {
		try {
			return getParser().isEmptyElementTag();
		} catch (final XmlPullParserException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private String lastName() {
		if (names.isEmpty())
			return null;

		return names.peek();
	}

	private void logIdentifiedCloseTag(final Tag parentTag)
			throws XmlPullParserException {
		if (Logger.isDebug()) {
			Logger.debug("-- close: parent = " + parentTag
					+ " ::: parser-state : tag-name: " + parser.getName()
					+ " , parser-event-type: " + parser.getEventType());
		}
	}

	private void logIdentifiedStartTag(final Tag tag)
			throws XmlPullParserException {
		if (Logger.isDebug()) {
			Logger.debug("-- identify start: parent = " + state.parentTag()
					+ " - identified: " + tag);
			Logger.debug("-- start-tag: parser-state ::: " + " tag-name = "
					+ parser.getName() + ", event-type = "
					+ parser.getEventType());
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

	public void startDoc() throws Throwable {
		parser.nextTag();

		final boolean isStartTag = (parser.getEventType() != XmlPullParser.START_TAG);
		if (isStartTag) {
			throw new IllegalStateException(
					"Couldn't parse document, because waiting for START_TAG, but found: "
							+ parser.getEventType());
		}

		state.openRoot(this.root);
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
	}

	/**
	 * callback-method is called when start-tag has been catched.
	 * 
	 * @param current
	 * @param callback
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected abstract void startTag(final Tag current, final C callback)
			throws IOException, XmlPullParserException;

	/**
	 * callback-method is called when close-tag has been catched.
	 * 
	 * @param current
	 * @param callback
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected abstract void closeTag(final Tag current, final C callback)
			throws IOException, XmlPullParserException;

}