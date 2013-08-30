package com.touchableheroes.android.xml.testapp.test.skipTag;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.touchableheroes.android.xml.parser.DomainSpecificBinding;
import com.touchableheroes.android.xml.parser.Tag;
import com.touchableheroes.android.xml.parser.XMLParserLoop;
import com.touchableheroes.android.xml.parser.XMLTagFacade;
import com.touchableheroes.android.xml.parser.XMLTagHandler;
import com.touchableheroes.android.xml.parser.example.ExampleTag;

import android.content.res.AssetManager;
import android.test.ActivityTestCase;

public class SkipTagTestCase extends ActivityTestCase {

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
	
	
	
	public void test() throws Exception {
		final AssetManager assets = getInstrumentation().getContext()
				.getAssets();
		final InputStream in = assets.open( "skip-tag.xml" );
		assertNotNull( in );
		
		final TestCallBack callback = new TestCallBack();
		
		final List<String> names = new ArrayList<String>();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final XMLTagHandler tagHandler = new XMLTagHandler(ExampleTag.ROOT, callback) {

			@Override
			protected void startTag(final Tag current, final XMLTagFacade facade)
					throws Exception {
				System.out.println( "<" + current.getName() + ">" );
				startCounter++;
				
				if( current == ExampleTag.ITEM )
					names.add( facade.getAttribute( "name" ) );
			}

			@Override
			protected void closeTag(Tag current) throws Exception {
				closeCounter++;
				System.out.println( "</" + current.getName() + ">");
			}
		};
		
		@SuppressWarnings("unchecked")
		final XMLParserLoop loop = XMLParserLoop.create( tagHandler  );
		
		assertNotNull(loop);
		
		loop.useInput( in );
		loop.run();

		assertFalse( names.isEmpty() );
		assertEquals(  "item1", names.get(0) );
		assertEquals(  1, names.size() );
		
		assertEquals( startCounter, closeCounter );

		assertEquals( 3, startCounter );
	}
}
