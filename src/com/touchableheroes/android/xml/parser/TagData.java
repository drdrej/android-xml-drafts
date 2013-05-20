package com.touchableheroes.android.xml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.touchableheroes.android.log.Logger;
import com.touchableheroes.android.xml.parser.ext.Text;
import com.touchableheroes.android.xml.parser.tag.SameRuleAs;


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
	private final String fullName;
	private final boolean isPattern;
	
	private final Pattern pattern;
	
	private final boolean skip;
	
	private final SameRuleAs ruleRef;
	
	private final Text handleText;

	/**
	 * This constructor will be used as root-tag.
	 */
	public TagData() {
		this.ns = null;
		this.name = "";
		this.fullName = "";
		this.isPattern = false;
		this.pattern = null;
		
		this.children = Tag.DEFAULT.children();
		
		this.nameIdx = indexingNames();
		this.skip = false;
		this.ruleRef = null;
		this.handleText = null;
	}
	

	public TagData(final String name, final SameRuleAs ruleRef) {
		this(null, name, false, ruleRef, null);
	}
	
	public TagData(final String name, final Text handleTxt) {
		this(null, name, false, null, handleTxt);
	}
	
	public TagData(final String ns, final String name,
			final boolean skip) {
		this(ns, name, true, null, null, new Tag[0] );
	}

    public TagData(final String ns, final String name,  
			final Tag... children) {
    	this(ns, name, false, null, null, children);
    }
    
    public TagData(final String ns, 
    		final String name, 
    		final boolean skip, 
    		final SameRuleAs ruleRef,
    		final Text handleText,
			final Tag... children) {
    	
		this.ns = ns;
		this.name = name;
		this.skip = skip;
		this.ruleRef = ruleRef;
		this.handleText = handleText;
		
		if( this.ns == null )
			this.fullName = this.name;
		else
			this.fullName = this.ns + ":" + this.name;
		
		this.isPattern = this.name.startsWith( "?" );
		if( isPattern ) {
			final String patternStr = this.name.substring(1).trim();
			this.pattern =  Pattern.compile(patternStr);
		} else 
			this.pattern = null;
				
				
		if( children == null ) {
			this.children = Tag.DEFAULT.children();
		} else {
			this.children = children;
		}
		
		this.nameIdx = indexingNames();
	}
	
	public boolean match(final String candidate) {
		if( !isPattern )
			return false;
		
		try {
			final Matcher matcher = pattern.matcher(candidate);
			return matcher.matches();
		} catch (final Throwable x) {
			Logger.exception(
					"couldn't parse tag-name-definition. maybe pattern is invalid = "
							+ pattern.pattern(), x);
			return false;
		}
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
			if( child.isPattern() )
				continue;
			
			rval.put(child.fullName(), child);
		}

		return rval;
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

	/**
	 * @param candidate is a full-name of a tag. never NULL.
	 */
	@Override
	public Tag findInNameIndex(
			final String candidate) {
		return this.nameIdx.get( candidate );
	}
	
	public Tag[] patterns() {
		final List<Tag> patterns = new ArrayList<Tag>();
		
		for( final Tag tag : this.children() ) {
			if( tag.isPattern() ) {
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

	@Override
	public String fullName() {
		return this.fullName;
	}

	@Override
	public boolean isPattern() {
		return this.isPattern ;
	}

	@Override
	public boolean shouldSkip() {
		return this.skip;
	}


	@Override
	public SameRuleAs ruleRef() {
		return this.ruleRef;
	}

	
	public boolean handleText() {
		return (this.handleText == Text.ACCEPT);
	}
}
