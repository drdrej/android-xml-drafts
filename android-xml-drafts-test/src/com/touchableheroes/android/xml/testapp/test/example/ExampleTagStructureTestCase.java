package com.touchableheroes.android.xml.testapp.test.example;

import android.test.ActivityTestCase;

import com.touchableheroes.android.xml.parser.Tag;
import com.touchableheroes.android.xml.parser.TagUtils;
import com.touchableheroes.android.xml.parser.example.ExampleTag;

public class ExampleTagStructureTestCase extends ActivityTestCase {

	public void testStructure() {
		Tag items = TagUtils.identify(ExampleTag.ROOT, "items" );
		assertNotNull( items );
		assertEquals( items, ExampleTag.ITEMS);
		
		Tag itemsChildren = TagUtils.identify(ExampleTag.ITEMS, "children" );
		assertNotNull( itemsChildren );
		assertEquals( itemsChildren, ExampleTag.ITEMS_CHILDREN);
		

		Tag itemsSkip = TagUtils.identify(ExampleTag.ITEMS, "skip" );
		assertNotNull( itemsSkip );
		assertEquals( itemsSkip, ExampleTag.ITEMS_SKIP);

		Tag itemsPattern = TagUtils.identify(ExampleTag.ITEMS, "i1" );
		assertNotNull( itemsPattern );
		assertEquals( itemsPattern, ExampleTag.ITEMS_PATTERN);

		Tag itemsPatternItem = TagUtils.identify(ExampleTag.ITEMS_PATTERN, "item" );
		assertNotNull( itemsPatternItem );
		assertEquals( itemsPatternItem, ExampleTag.ITEMS_PATTERN_ITEM);
		
		Tag itemsItems = TagUtils.identify(ExampleTag.ITEMS, "items" );
		assertNull( itemsItems );
	}
}
