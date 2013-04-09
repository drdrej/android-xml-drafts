package com.touchableheroes.android.xml.parser.example;

import com.touchableheroes.android.xml.parser.Tag;
import com.touchableheroes.android.xml.parser.TagData;

/**
 * An example how to declare states of the parser.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public enum ExampleTag implements Tag {
	
	// TAG-Definition :::
	ITEM( "item" ),
	ITEMS( "items", ITEM ),
	ROOT("root", ITEMS );

	// Attributes :::
	private TagData data;
	
	// Constructors :::
	ExampleTag() {
		this.data = new TagData();
	}

	ExampleTag(final String ns, final String name,
			final Tag... children) {
		this.data = new TagData(ns, name, children);
	}

	ExampleTag(final String name, final Tag... children) {
		this.data = new TagData(null, name, children);
	}
	
	// Interface:Tag :::
	@Override
	public Tag[] children() {
		return this.data.children();
	}

	@Override
	public String getNamespace() {
		return this.data.namespace();
	}

	@Override
	public String getName() {
		return data.name();
	}

	@Override
	public String type() {
		return this.name();
	}
	
}
