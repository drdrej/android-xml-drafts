package com.touchableheroes.android.xml.parser;

import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;

import com.touchableheroes.android.log.Logger;
import com.touchableheroes.android.xml.parser.tag.SameRuleAs;

/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class XMLEventPipe {

	private final XMLTagFacade facade;
	private final Stack<TagEvent> stack = new Stack<TagEvent>();
	private final Stack<Tag> states = new Stack<Tag>();
	
	private final XMLTagHandler<? extends Callback> handler;

	public XMLEventPipe(final XmlPullParser parser,
			final XMLTagHandler<? extends Callback> handler,
			final XMLTagFacade facade) {
		this.handler = handler;
		this.facade = facade;
	}

	public void startTag() {
		final Tag tag = identifyTag();

		final TagEvent event = TagEvent.create(facade.tagName(), tag);
		stack.push(event);

		if( tag == null )
			return;
		
		if (tag.shouldSkip()) {
			facade.skip();
			return;
		}

		handleStartTag(tag);
		states.push(tag);
	}

	private void handleStartTag(final Tag tag) {
		try {
			handler.startTag(tag, this.facade);
		} catch (final Exception x) {
			Logger.exception("Couldn't handle start-tag.", x);
		}
	}


	private Tag identifyTag() {
		if (stack.isEmpty()) {
			return identifyRoot();
		}

		return identifyChildTag();
	}

	private Tag identifyChildTag() {
		final Tag parent = parentTag();
		
		if( parent == null )
			return null;
		
		final Tag use = TagUtils.replaceBy(parent);
		final String tagName = facade.tagName();

		return TagUtils.identify(use, tagName);
	}

	private Tag parentTag() {
		if( states.isEmpty() )
			throw new IllegalStateException( "Couldn't call parentTag() - stack is empty." );
		
		return states.peek();
	}

	private Tag identifyRoot() {
		final String name = facade.tagName();
		final Tag root = this.handler.rootTag();
		final String fullName = root.fullName();

		if (fullName.equals(name))
			return root;
		else
			throw new IllegalStateException(
					"Couldn't identify tag. Wrong root! expected "
							+ root.fullName() + " but got " + name);
	}

	public void endTag() {
		final TagEvent current = stack.pop();

		if( current.tag != null )
			handleEndTag(current);
		
		final Tag parent = states.peek();
		
		if( parent == current.tag )
			states.pop();
	}

	private void handleEndTag(final TagEvent current) {
		try {
			handler.closeTag(current.tag);
		} catch (final Exception x) {
			System.out.println( "-- end-tag: " + current.name );
			Logger.exception("Couldn't handle close-tag.", x);
		}
	}
}
