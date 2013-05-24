Drafts & Examples to handle XML in android.
===========================================

[![endorse](https://api.coderwall.com/drdrej/endorsecount.png)](https://coderwall.com/drdrej)


This project is an example of how to use xml-pull-parser in Android-SDK.
I've collected my expirience about it an pushed to this project.

... and **it is a small framework to handle xml** in android with pull-parser easier.


## Philosophy 
1. declaration over programming. 
2. types over strings.

## Features
1. based on XmlPullParser.
2. hide parser logic.
3. generate states based on enums.
4. you can bind a callback-object to parser to handle states.
5. parser has only 2 abstract methods (startTag() vs. endTag() ).
6. no special language need to learn - java is enough.


## Usage

Before you start to write a parser, you need to open your xml-file and 
plan/identify parser-states. 

### XML-document

1. all kind of xml accepted by xml-pull-parser.
2. you need to the states.

**Example**:

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

### Callback


## License: 
Apache License
http://www.apache.org/licenses/LICENSE-2.0.html

you can use it in commercial or private projects. you are free to help and to support.


