package com.touchableheroes.android.xml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This object will be used to store some tag-Info.
 * Use this object in a tag-enum to collect and refer tag-data.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class TagData implements Tag {

	private final String ns;
	private final String name;
	private final Tag[] children;
	
	private final Map<String, Tag> nameIdx;

	/**
	 * This constructor will be used as root-tag.
	 */
	public TagData() {
		this.ns = null;
		this.name = "";
		this.children = Tag.DEFAULT.children();
		
		this.nameIdx = indexingNames();
	}

	/** 
	 * inits index for tag-names.
	 * 
	 * @return
	 */
	private Map<String, Tag> indexingNames() {
		if( this.children.length == 0 )
			return Collections.emptyMap();
		
		final Map<String, Tag> rval = new HashMap<String, Tag>();
				
		for ( final Tag child : this.children ) {
			final String name = child.getName();
			
			if( !TagUtils.isPattern(name) )
				rval.put(name, child);
		}

		return rval;
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
		
		this.nameIdx = indexingNames();
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

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getNamespace() {
		return namespace();
	}

	@Override
	public Tag findInNameIndex(final String name) {
		return this.nameIdx.get( name );
	}
	
	public Tag[] patterns() {
		final List<Tag> patterns = new ArrayList<Tag>();
		
		for( final Tag tag : this.children() ) {
			final String name = tag.getName();
			
			if( TagUtils.isPattern(name) ) {
				patterns.add( tag );
			}
		}

		final int patternsSize = patterns.size();
		if( patternsSize < 1)
			return new Tag[0];
		
		final Tag[] rval = new Tag[ patternsSize ]; 
		patterns.toArray(rval);				
		
		return rval;
	}

	@Override
	public String type() {
		return name();
	}

}
