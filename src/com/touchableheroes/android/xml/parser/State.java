package com.touchableheroes.android.xml.parser;

import java.util.ArrayList;

import com.touchableheroes.android.log.Logger;



/**
 * Represents the state of the xml-parser.
 * 
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class State {

	private int depth = 0;
	public boolean isReady = false;

	public final ArrayList<Tag> path = new ArrayList<Tag>();

	public void openRoot(Tag root) {
		this.path.add(root);
	}

	public void openTag(final Tag tag) {
		if (Logger.isDebug()) {
			Logger.debug("-- tag.open: " + tag);
			Logger.debug("-- path: " + path);
		}

//		if (TagUtils.isText(tag))
//			return;

		path.add(tag);
		depth++;
	}

	/**
	 * gibt das letzte Element zurück.
	 * 
	 * @return
	 */
	public Tag popTag() {
		if (path.isEmpty())
			return null;

		final int last = (path.size() - 1);
		return path.remove(last);
	}

	/**
	 * gibt das letzte Element zurück.
	 * 
	 * @return
	 */
	public Tag peekTag() {
		if (path.isEmpty())
			return null;

		final int last = (path.size() - 1);
		return path.get(last);
	}

	public void closeTag(final Tag tag) {
		if (Logger.isDebug()) {
			Logger.debug("-- tag.close: " + tag);
		}

//		if (TagUtils.isText(tag) )
//			return;

		if (peekTag() == tag) {
			popTag();
		} else {
			final String msg = "Couldn't close tag, because tag is incorrect."
					+ " waiting for tag: " + peekTag() + " - but got tag: "
					+ tag + " ::: \n " + "-- depth: " + this.depth;

			throw new IllegalStateException(msg);
		}

		depth--;
	}

	public Tag parentTag() {
		return peekTag();
	}

	public String toPathString() {
		final StringBuilder buffer = new StringBuilder();

		for (final Tag tag : this.path) {
			buffer.append(tag.type());
			buffer.append("/");
		}

		return buffer.toString();
	}
}
