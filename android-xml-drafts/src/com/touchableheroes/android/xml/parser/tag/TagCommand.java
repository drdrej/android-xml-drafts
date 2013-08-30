package com.touchableheroes.android.xml.parser.tag;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public enum TagCommand {
	SKIP, /* should be used as command to skip a tag.*/
	ACCEPT_TEXT,
	ACCEPT_ATTRIBUTES,
	ACCEPT /* accept tag complete */
}
