package com.touchableheroes.android.xml.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.touchableheroes.android.log.Logger;

/**
 * Method to identify TAGs in an XML. Base on the idea of hierarchical
 * event-bus.
 * 
 * Enum represent an assigned state/event-type.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class TagUtils {

	/**
	 * 
	 * @param parent
	 * @param fullName without namespace.
	 * @return
	 */
	public static Tag identify(final Tag parent, final String fullName) {
		if (parent == null) {
			final String logMsg = "Couldn't identify tag, because passend parent is NULL.";
			throw new IllegalArgumentException(logMsg);
		}

		if (fullName == null)
			return null;

		final int pos = fullName.indexOf(":");
		System.out.println("-- found delimiter : " + fullName + " pos = " + pos);
		
//		if (pos < 0) {
			return identifyByName2(parent, null, fullName);
//		} else {
//			final String ns = fullName.substring(0, pos);
//			final String name = fullName.substring((pos + 1));
//			return identifyByName(parent, ns, name);
//		}
	}

	private static boolean matchTagNameByPattern(final String fullNamePattern,
			final String name) {
		final String patternStr = fullNamePattern.substring(1).trim();
		try {
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(name);

			return matcher.matches();
		} catch (final Throwable x) {
			Logger.exception(
					"couldn't parse tag-name-definition. maybe pattern is invalid = "
							+ patternStr, x);
			return false;
		}
	}

	/**
	 * match namePattern against candidate (namespace and tag-name)
	 * 
	 * @param declaredNamePattern
	 * @param declaredNs
	 * @param candidateName
	 * @return
	 */
	public static boolean isCandidate(final String declaredNs,
			final String declaredNamePattern, final String candidateNs,
			final String candidateName) {
		if (candidateName == null)
			throw new IllegalArgumentException(
					"parameter:candidateName is NULL.");

		if (declaredNamePattern == null)
			throw new IllegalArgumentException("parameter:namePattern is NULL.");

		if (isPattern(declaredNamePattern)) {
			if (!matchTagNameByPattern(declaredNamePattern, candidateName))
				return false;
		} else {

			if (!candidateName.equals(declaredNamePattern))
				return false;
		}

		if (declaredNs != null) {
			if (declaredNs.equals(candidateNs))
				return true;
		}

		return true;
	}

	
	public static final boolean isPattern(final String tagDeclarationName) {
		return tagDeclarationName.startsWith("?");
	}
	
	/**
	 * uses this method to identify a child of a tag by name and namespace.
	 * 
	 * @param parent point to start to seach a child in tag-declarations.
	 * @param ns namespace or NULL
	 * @param name name of the current tag in the xml.
	 * 
	 * @return identified child of parent or NULL
	 */
	private static Tag identifyByName(final Tag parent, final String ns,
			final String name) {
		if (name == null)
			return null;

		final Tag[] children = parent.children();

		return identifyByNameInArray(ns, name, children);
	}

	private static Tag identifyByNameInArray(final String ns,
			final String name, final Tag[] candidates) {
		for (final Tag child : candidates) {
			final String candidateName = child.getName();

			if (candidateName == null)
				return null;

			if (!isCandidate(child.getNamespace(), child.getName(), ns, name))
				continue;

			return child;
		}

		return null;
	}
	
	/**
	 * uses this method to identify a child of a tag by name and namespace.
	 * same as indexByName() but with suport name-index.
	 * should be faster, but uses more memory to hold indizes.
	 * 
	 * @param parent point to start to seach a child in tag-declarations.
	 * @param ns namespace or NULL
	 * @param name name of the current tag in the xml.
	 * 
	 * @return identified child of parent or NULL
	 */
	private static Tag identifyByName2(final Tag parent, final String ns,
			final String name) {
		if (name == null)
			return null;

		final Tag identified = parent.findInNameIndex(name);

		if( identified != null )
			return identified;
		
		final Tag[] patterns = parent.patterns();
		
		return identifyByNameInArray(ns, name, patterns);
	}

}