package com.touchableheroes.android.xml.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public interface TagEventHandler<C extends Callback> {

	public void handleStartTagEvent(final C callback, final Tag current)
			throws IOException, XmlPullParserException;

	public void handleCloseTagEvent(final C callback, final Tag current)
			throws IOException, XmlPullParserException;
}
