package com.touchableheroes.android.xml.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;






/**
 * Method to identify TAGs in an XML.
 * Base on the idea of hierarchical event-bus.
 * 
 * Enum represent an assigned state/event-type.
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
	
	private static boolean matchTagNameByPattern(final Tag tag, final String name ) {
		final String full = tag.getName();
		final String patternStr = full.substring(1).trim();
		
		final Pattern pattern = Pattern.compile( patternStr );
		final Matcher matcher = pattern.matcher( name );
		
		return matcher.matches();
	}
	
	private static Tag identifyByName(final Tag parent, final String ns, final String name) {
		if( name == null )
			return null;
		
		final Tag[] children = parent.children();
		
		for (final Tag child : children) {
			final String candidateName = child.getName();
			if( candidateName == null )
				return null;
			
			final boolean isPattern = candidateName.startsWith( "?");
			if( isPattern ) {
				if( matchTagNameByPattern(child, candidateName) )
					continue;
			} else {
				if (!child.getName().equals(name)) 
					continue;
			}
			
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