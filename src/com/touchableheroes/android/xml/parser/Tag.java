package com.touchableheroes.android.xml.parser;

/**
 * Declaration of a tag.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public interface Tag {

	public String getName();

	public Tag[] children();

	public String getNamespace();
	
	public Tag findInNameIndex(final String name);
	
	public final static Tag DEFAULT = new DefaultTag();
	
	public final static Tag TEXT = new TextTag();

	
	
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
		public Tag findInNameIndex(String name) {
			return null;
		}

		@Override
		public Tag[] patterns() {
			return new Tag[0];
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
		public Tag findInNameIndex(String name) {
			return null;
		}

		@Override
		public Tag[] patterns() {
			return new Tag[0];
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

}
