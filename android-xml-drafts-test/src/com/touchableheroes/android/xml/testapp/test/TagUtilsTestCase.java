package com.touchableheroes.android.xml.testapp.test;

import android.test.ActivityTestCase;

import com.touchableheroes.android.xml.parser.Tag;
import com.touchableheroes.android.xml.parser.TagUtils;
import com.touchableheroes.android.xml.parser.example.ExampleTag;

public class TagUtilsTestCase extends ActivityTestCase {

	
	public void testHierarchie() {
		final Tag tag = TagUtils.identify( ExampleTag.ROOT, "items");
		assertNotNull( tag );
		assertEquals(ExampleTag.ITEMS, tag);
	}
	
	public void testReplaceBy() {
		final Tag replaceBy = TagUtils.replaceBy( ExampleTag.ITEMS_CHILDREN );
		assertNotNull( replaceBy );
		assertEquals( ExampleTag.ITEMS, replaceBy);
	}
}
