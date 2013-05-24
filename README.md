Drafts & Examples to handle XML in Android™.
===========================================

[![endorse](https://api.coderwall.com/drdrej/endorsecount.png)](https://coderwall.com/drdrej)

This project is an example of how to use an xml-pull-parser in Android™-SDK.
I've collected my expirience about it an pushed to this project.

... and **it is a small framework to handle xml** in Android™ with pull-parser easier.

**Name**: xml-drafts - **Version**: 0.9

this project is open-source and free, so if you use it or simply like it you are welcome to donate.
[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=D7GL3MAY2KYLG)


## Philosophy 
1. declaration over programming. 
2. types over strings.
3. easy to use.
4. should be fast as possible and use less memory as possible.



## Features
1. based on XmlPullParser.
2. hide parser logic.
3. generate states based on enums.
4. you can bind a callback-object to parser to handle states (called domain-specific-binding)
5. parser has only 2 abstract methods (startTag() vs. endTag()).
6. no special language need to learn - java is enough.


## Usage

Before you start to write a parser, you need to open your xml-file and 
plan/identify parser-states. To understand the functionality imagine you need to parse
item-tags in a given xml, skip some some tags and ignore unknown-tags. F.e. you want 
to collect names and content (text) of these item-tags in a list. In the following 
example I show you how you can write a parser to do this kind of work. Bevore we start 
you need an xml-file. 


### XML-document

This parser accepts all kind of xml that is accepted by an xml-pull-parser.
So you can create a file with the structure like in the following example.

**Example**: create an xml-file.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<root>
   <items>
      
	  <children> <!-- identify children tags -->
	       <item name="item1">text1</item>
	  </children>
	  
	  <item name="item2" >text2</item>

	  <i1> <!-- identify tag by pattern: -->
	      <item name="item3">text3</item>
	  </i1>
	  	  
      <ns:unknown />
	  
      <skip> <!-- skip because declared to skip -->
         <item name="item4" >text4</item>
      </skip>
            
   </items>
</root>

```


### States & Tags

1. create an enum
2. these enum should implements *com.touchableheroes.android.xml.parser.Tag*
3. to make these implementations easier use com.touchableheroes.android.xml.parser.TagData (@see delegate-pattern)

**Example**:

```java

public enum ExampleTag implements Tag {
	
	ITEMS_PATTERN_ITEM( "item", Text.ACCEPT ),
	
	ITEMS_PATTERN( "?^[a-z][0-9]$", ITEMS_PATTERN_ITEM ),
	
	ITEMS_SKIP( "skip", true ),

	ITEMS_CHILDREN( "children", new SameTagAs( "ITEMS" ) ),
	
	ITEM( "item", Text.ACCEPT ),
	
	ITEMS( "items", ITEM, ITEMS_CHILDREN, ITEMS_PATTERN, ITEMS_SKIP ),
	
	ROOT("root", ITEMS );

    ...
    
}

```





### Parser

**How-to**: follow these steps to use xml-parser.

1. declare xml-tag-andler.
2. declare domain spcific binding.
3. load parser.
4. bind input-stream.
5. parse. 


**Example**: declare xml-tag-handler.

```java

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final XMLTagHandler tagHandler = new XMLTagHandler(ExampleTag.ROOT, callback) {
			
			@Override
			protected void startTag(final Tag current, final XMLTagFacade facade)
					throws Exception {
				
				if( current == ExampleTag.ITEM 
				    || current == ExampleTag.ITEMS_PATTERN_ITEM ) {
					callback.onItem( 
					    facade.getAttribute("name" ),
					    facade.readText()
					);
				}

			}

			@Override
			protected void closeTag(Tag current) throws Exception {
				;
			}
		};
```

**Example**: declare domain-specific-binding.

```java

class CollectNames implements DomainSpecificBinding {


		final List<String> names = ...;
		final List<String> values = ...;

		public void onItem( final String name, final String content ) {
			names.add( name );
			values.add( value );
		}
		
}


```


**Example**: load and run parser.

```java
        final InpuStream in = ...;

		@SuppressWarnings("unchecked")
		final XMLParserLoop loop = XMLParserLoop.create( tagHandler  );
		
		loop.useInput( in );
		loop.run();

```



## License: 
Apache License
http://www.apache.org/licenses/LICENSE-2.0.html

you can use it in commercial or private projects. 
you are free to help and to support!

use it and have fun!
   Andreas Siebert *(aka drdrej)*


