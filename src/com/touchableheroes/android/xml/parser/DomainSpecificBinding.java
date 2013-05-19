package com.touchableheroes.android.xml.parser;


/**
 * Marker-Interface for callbacks for xml-parser.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public interface DomainSpecificBinding<T> {

	public T current();
	
}
