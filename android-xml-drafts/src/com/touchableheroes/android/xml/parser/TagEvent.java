package com.touchableheroes.android.xml.parser;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class TagEvent {
	
	public final String name;
	public final Tag tag;
	
	public TagEvent(final String name, final Tag tag) {
		this.name = name;
		this.tag = tag;
	}

	public static TagEvent create(final String tagName, final Tag tag) {
		return new TagEvent(tagName, tag);
	}
}
