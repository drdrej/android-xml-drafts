package com.touchableheroes.android.xml.parser;


/**
 * This object will be used to store some tag-Info.
 * Use this object in a tag-enum to collect and refer tag-data.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class TagData {

	private final String ns;
	private final String name;
	private final Tag[] children;

	/**
	 * This constructor will be used as root-tag.
	 */
	public TagData() {
		this.ns = null;
		this.name = "";
		this.children = Tag.DEFAULT.children();
	}

	public TagData(final String ns, final String name,
			final Tag... children) {
		this.ns = ns;
		this.name = name;
		
		if( children == null ) {
			this.children = Tag.DEFAULT.children();
		} else {
			this.children = children;
		}
	}

	public TagData(final String name, final Tag... children) {
		this(null, name, children );
	}
	
	public String name() {
		return this.name;
	}

	public Tag[] children() {
		return this.children;
	}

	public String namespace() {
		return this.ns;
	}

}
