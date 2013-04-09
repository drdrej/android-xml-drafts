package com.touchableheroes.android.xml.parser.example;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.touchableheroes.android.xml.parser.AbstractXMLPullParser;
import com.touchableheroes.android.xml.parser.Tag;

public class ExampleXMLParser extends AbstractXMLPullParser<ExampleCallback> {

	public ExampleXMLParser() throws XmlPullParserException {
		super(ExampleTag.ROOT);
	}

	@Override
	protected void startTag(final Tag current, final ExampleCallback callback) throws IOException,
			XmlPullParserException {
		if( current == ExampleTag.ITEM ) 
			callback.onItem( getAttribute( "name" ) );
	}

	@Override
	protected void closeTag(Tag current, ExampleCallback callback) throws IOException,
			XmlPullParserException {
		;
	}

}
