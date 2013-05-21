package com.touchableheroes.android.xml.parser;

import com.touchableheroes.android.xml.parser.tag.SameTagAs;

/**
 * Declaration of a tag.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public interface Tag {

	public String getName();

	public Tag[] children();

	public String getNamespace();
	
	public Tag findInNameIndex(final String fullName);
	
	public final static Tag DEFAULT = new DefaultTag();
	
	public final static Tag TEXT = new TextTag();

	public boolean isPattern();
	
	public boolean match(final String candidate);
	
	public boolean shouldSkip();
	
	public SameTagAs ruleRef();
	
	public boolean handleText();
	
	
	
	static class DefaultTag implements Tag {

		@Override
		public String getName() {
			return "";
		}

		@Override
		public Tag[] children() {
			return new DefaultTag[0];
		}

		@Override
		public String getNamespace() {
			return "";
		}

		@Override
		public String type() {
			return "UNKNOWN";
		}

		@Override
		public Tag findInNameIndex(final String fullName) {
			return null;
		}

		@Override
		public Tag[] patterns() {
			return new Tag[0];
		}

		@Override
		public String fullName() {
			return "";
		}

		@Override
		public boolean isPattern() {
			return false;
		}

		@Override
		public boolean match(final String candidate) {
			return false;
		}

		@Override
		public boolean shouldSkip() {
			return false;
		}

		@Override
		public SameTagAs ruleRef() {
			return null;
		}

		@Override
		public boolean handleText() {
			return false;
		}
	}

	
	/**
	 * This Tag represents a TEXT block.
	 * 
	 * @author asiebert
	 *
	 */
	public static class TextTag implements Tag {

		@Override
		public String getName() {
			return "TEXT";
		}

		@Override
		public Tag[] children() {
			return new DefaultTag[0];
		}

		@Override
		public String getNamespace() {
			return "";
		}

		@Override
		public String type() {
			return "TEXT";
		}

		@Override
		public Tag findInNameIndex(final String fullName) {
			return null;
		}

		@Override
		public Tag[] patterns() {
			return new Tag[0];
		}

		@Override
		public String fullName() {
			return "";
		}
		
		@Override
		public boolean isPattern() {
			return false;
		}

		@Override
		public boolean match(final String candidate) {
			return false;
		}

		@Override
		public boolean shouldSkip() {
			return false;
		}

		@Override
		public SameTagAs ruleRef() {
			return null;
		}
		
		@Override
		public boolean handleText() {
			return false;
		}
	}


    /**
     * represents the name of the state/tag-type.
     * never null!
     * 
     * @return
     */
	public String type();

	/**
	 * all assigned patterns.
	 * 
	 * @return
	 */
	public Tag[] patterns();

	public String fullName();

}
