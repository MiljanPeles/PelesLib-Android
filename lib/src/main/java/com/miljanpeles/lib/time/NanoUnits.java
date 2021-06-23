package com.miljanpeles.lib.time;

public final class NanoUnits {
	private NanoUnits() {}

	public static final long NANOSECOND = 1;
	public static final long MILLISECOND = NANOSECOND * 1000000;
	public static final long SECOND = MILLISECOND * 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;

}
