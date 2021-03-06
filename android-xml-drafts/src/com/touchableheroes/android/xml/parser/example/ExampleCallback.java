package com.touchableheroes.android.xml.parser.example;

import java.util.ArrayList;
import java.util.List;

import com.touchableheroes.android.xml.parser.DomainSpecificBinding;

/**
 * is an example-callback-object to handle tags in the example-doc.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class ExampleCallback implements DomainSpecificBinding<String> {

	private List<String> names = new ArrayList<String>();

	public void onItem(final String name) {
		if (name == null || name.length() < 1)
			throw new IllegalArgumentException(
					"couldn't use passed name. name is NULL or empty.");

		names.add(name);
	}

	public List<String> getItems() {
		return this.names;
	}

	@Override
	public String current() {
		return names.get( (names.size() - 1) );
	}

}
