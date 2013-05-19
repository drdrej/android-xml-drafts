package com.touchableheroes.android.xml.parser.example;

import com.touchableheroes.android.xml.parser.Tag;
import com.touchableheroes.android.xml.parser.TagData;
import com.touchableheroes.android.xml.parser.ext.Text;
import com.touchableheroes.android.xml.parser.tag.SameRuleAs;

/**
 * An example how to declare states of the parser.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public enum ExampleTag implements Tag {
	
	ITEMS_PATTERN_ITEM( "item", Text.ACCEPT ),
	
	ITEMS_PATTERN( "?^[a-z][0-9]$", ITEMS_PATTERN_ITEM ),
	
	ITEMS_SKIP( "skip", true ),

	ITEMS_CHILDREN( "children", new SameRuleAs( "ITEMS" ) ),
	
	ITEM( "item", Text.ACCEPT ),
	
	ITEMS( "items", ITEM, ITEMS_CHILDREN, ITEMS_PATTERN, ITEMS_SKIP ),
	
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

	ExampleTag(final String name, final boolean skip) {
		this.data = new TagData(null, name, skip);
	}
	
	ExampleTag(final String name, final Text useText) {
		this.data = new TagData(name, useText);
	}
	
	ExampleTag(final String name, final Tag... children) {
		this.data = new TagData(null, name, children);
	}
	
	ExampleTag(final String name, final SameRuleAs ruleRef) {
		this.data = new TagData(name, ruleRef);
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

	@Override
	public Tag findInNameIndex(final String fullName) {
		return data.findInNameIndex(fullName);
	}

	@Override
	public Tag[] patterns() {
		return data.patterns();
	}

	@Override
	public String fullName() {
		return data.fullName();
	}

	@Override
	public boolean isPattern() {
		return data.isPattern();
	}

	@Override
	public boolean match(final String candidate) {
		return data.match(candidate);
	}

	@Override
	public boolean shouldSkip() {
		return this.data.shouldSkip();
	}

	@Override
	public SameRuleAs ruleRef() {
		return data.ruleRef();
	}

	@Override
	public boolean handleText() {
		return data.handleText();
	}
	
}
