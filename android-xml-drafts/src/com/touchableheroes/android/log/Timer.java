package com.touchableheroes.android.log;

import java.util.HashMap;
import java.util.Map;

public class Timer {

	private final long start;
	private final String name;

	private long time;

	private Timer(final String name) {
		this.start = System.currentTimeMillis();
		this.name = name;
	}
	
	public static Timer start( final Class<?> source, final String scope ) {
		return new Timer(source.getName() + "." + scope);
	}
	
	/**
	 * 
	 */
	private static Map<String, Long> MAP = new HashMap<String, Long>();
	
	public long end() {
		return end( false );
	}
	
	public long end(final boolean accumulate) {
		if( this.start < 0) 
			Logger.error( "Couldn't end timer, because timer is unusable." );
		
		this.time = System.currentTimeMillis() - this.start;
		
		if( Logger.isDebug() )
		 Logger.debug( "[TIME] -- timer : " + this.name + " = "+ this.time + "ms " );
		
		if( accumulate )
			this.accumulate();
		
		return this.time;
	}

	
	
	
	/**
	 * this Method is used by many stacks
	 */
	public synchronized void accumulate() {
		final Long accumulated = MAP.get( this.name );
		
		if( accumulated == null  ) {
			MAP.put( this.name, 0L );
		} else {
			MAP.put(this.name, (accumulated + this.time));
		}
		
//		System.out.println( "[ACCUMULATE] -- timer : "
//				+ this.name + " = " + MAP.get( this.name ) + "ms" );
			
		
	}
	
}
