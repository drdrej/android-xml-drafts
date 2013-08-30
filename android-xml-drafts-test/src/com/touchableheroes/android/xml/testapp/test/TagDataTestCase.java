package com.touchableheroes.android.xml.testapp.test;

import android.test.ActivityTestCase;

import com.touchableheroes.android.xml.parser.TagData;



public class TagDataTestCase extends ActivityTestCase {

	public void testTagDataPattern() {
		TagData td = new TagData( "?^([a-z])+$");
		
		assertTrue(td.isPattern());
		assertTrue(td.match( "abc" ) );
	}
}
