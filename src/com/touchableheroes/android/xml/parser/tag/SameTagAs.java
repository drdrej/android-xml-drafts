package com.touchableheroes.android.xml.parser.tag;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class SameTagAs implements TagRule {

	private final String state;

	public SameTagAs(final String stateName) {
	   this.state = stateName;	
	}
	
	public String stateName() {
		return this.state;
	}
}
