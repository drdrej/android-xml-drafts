package com.touchableheroes.android.xml.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 * @param <C>
 */
public abstract class XMLTagHandler<C extends DomainSpecificBinding> {

	private final Tag root;
	private final C callback;

	public XMLTagHandler(final Tag root, final C callback) {
		this.callback = callback;
		this.root = root;
	}
	
	/**
	 * callback-method is called when start-tag has been catched.
	 * 
	 * @param current
	 * @param callback2
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected abstract void startTag(final Tag current, final XMLTagFacade facade) 
			throws Exception;

	/**
	 * callback-method is called when close-tag has been catched.
	 * 
	 * @param current
	 * @param callback
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	protected abstract void closeTag(final Tag current)
			throws Exception;
	
	
	public C callback() {
		return this.callback;
	}

	public Tag rootTag() {
		return this.root;
	}

}
