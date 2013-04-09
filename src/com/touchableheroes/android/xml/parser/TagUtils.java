package com.touchableheroes.android.xml.parser;






/**
 * Method to identify TAGs in an XML.
 * Enum represent an assigned state.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class TagUtils {

	public static Tag identify(final Tag parent, final String fullName) {
		if( parent == null ) {
			final String logMsg = "Couldn't identify tag, because passend parent is NULL.";
			throw new IllegalArgumentException( logMsg );
		}
		
	
		if (fullName == null)
			return null;

		final int pos = fullName.indexOf(":");
		if (pos < 0) {
			return identifyByName(parent, null, fullName);
		} else {
			final String ns = fullName.substring(0, pos);
			final String name = fullName.substring((pos + 1));
			return identifyByName(parent, ns, name);
		}
	}
	
	private static Tag identifyByName(final Tag parent, final String ns, final String name) {
		Tag[] children = parent.children();
		
		for (final Tag child : children) {
			if (!child.getName().equals(name)) 
				continue;
			
			if (ns != null ) {
				if( ns.equals( child.getNamespace() ) )
					return child;
			} else {
				return child;
			}
		}

		return null;
	}

//	public static boolean isText(final Tag tag) {
//		return (tag instanceof TextTag);
//	}
}