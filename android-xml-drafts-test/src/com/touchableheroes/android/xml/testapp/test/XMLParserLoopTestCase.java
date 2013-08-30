package com.touchableheroes.android.xml.testapp.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.AssetManager;
import android.test.ActivityTestCase;

import com.touchableheroes.android.xml.parser.DomainSpecificBinding;
import com.touchableheroes.android.xml.parser.Tag;
import com.touchableheroes.android.xml.parser.XMLParserLoop;
import com.touchableheroes.android.xml.parser.XMLTagFacade;
import com.touchableheroes.android.xml.parser.XMLTagHandler;
import com.touchableheroes.android.xml.parser.example.ExampleTag;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class XMLParserLoopTestCase extends ActivityTestCase {

	int patternTag = 0;
	int childrenTagCounter = 0;
	int startCounter = 0;
	int closeCounter = 0;
	
	class TestCallBack implements DomainSpecificBinding<String> {
		
		@Override
		public String current() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		startCounter = 0;
		closeCounter = 0;
	}
	
	public void testXMLParserCountTags() throws Exception {
		final AssetManager assets = getInstrumentation().getContext()
				.getAssets();
		final InputStream in = assets.open( "children.xml" );
		assertNotNull( in );
		
		final TestCallBack callback = new TestCallBack();
		final List<String> names = new ArrayList<String>();
		final List<String> values = new ArrayList<String>();
		
		final List<Tag> states = new ArrayList<Tag>();
		final List<Tag> statesClose = new ArrayList<Tag>();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final XMLTagHandler tagHandler = new XMLTagHandler(ExampleTag.ROOT, callback) {
			
			@Override
			protected void startTag(final Tag current, final XMLTagFacade facade)
					throws Exception {

				System.out.println( "<" + current.getName() + ">" );
				startCounter++;
				
				states.add( current );
				
					
				if( current == ExampleTag.ITEMS_CHILDREN )  {
					childrenTagCounter++;
				}
				
				if( current == ExampleTag.ITEMS_PATTERN ) {
					patternTag++;
				}
					
				
				if( current == ExampleTag.ITEM ) {
					names.add( facade.getAttribute("name" ) );
					values.add( facade.readText() );
				}
				
				if( current == ExampleTag.ITEMS_PATTERN_ITEM ) {
					names.add( facade.getAttribute( "name" ));
					values.add( facade.readText() );
				}
					
			}

			@Override
			protected void closeTag(Tag current) throws Exception {
				closeCounter++;
				System.out.println( "</" + current.getName() + ">");
				
				statesClose.add( current );
			}
		};
		
		@SuppressWarnings("unchecked")
		final XMLParserLoop loop = XMLParserLoop.create( tagHandler  );
		
		loop.useInput( in );
		loop.run();

		// ----------------------------------------------- Tests :::
		assertEquals( states.size(), statesClose.size() );
		assertEquals( startCounter, closeCounter );
		
		final boolean isNameSizeNotNull = names.size() > 1;
		assertTrue( isNameSizeNotNull );
		assertEquals( "item1", names.get(0) );
		assertEquals( "item2", names.get(1) );
		assertEquals( "item4", names.get(2) );
		assertEquals( "item5", names.get(3) );
		assertEquals( "item6", names.get(4) );
		assertEquals( "item7", names.get(5) );
		assertEquals( "item8", names.get(6) );
		
		assertEquals( 7, names.size() );
		assertEquals( 7, values.size() );
		
		assertEquals( "text1", values.get(0) );
		assertEquals( "text2", values.get(1) );
		assertEquals( "text4", values.get(2) );
		assertEquals( "text5", values.get(3) );
		assertEquals( "", values.get(4) );
		
		assertEquals( 1,  childrenTagCounter );
		assertEquals( 1, patternTag );
		
		assertEquals( 11, startCounter );

		assertEquals( 11, states.size() );
		assertEquals( ExampleTag.ROOT, states.get(0) );
		assertEquals( ExampleTag.ITEMS, states.get(1) );
		assertEquals( ExampleTag.ITEMS_CHILDREN, states.get(2) );
		assertEquals( ExampleTag.ITEM, states.get(3) );
		assertEquals( ExampleTag.ITEM, states.get(4) );
		assertEquals( ExampleTag.ITEM, states.get(5) );
		assertEquals( ExampleTag.ITEMS_PATTERN, states.get(6) );
		assertEquals( ExampleTag.ITEMS_PATTERN_ITEM, states.get(7) );
		assertEquals( ExampleTag.ITEM, states.get(8) );
	}
	
}
