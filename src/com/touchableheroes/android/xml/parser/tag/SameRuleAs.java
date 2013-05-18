package com.touchableheroes.android.xml.parser.tag;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class SameRuleAs {

	private final String state;

	public SameRuleAs(final String stateName) {
	   this.state = stateName;	
	}
	
	public String stateName() {
		return this.state;
	}
}
