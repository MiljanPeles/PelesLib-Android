package com.miljanpeles.lib.file;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelper {
	private static final int DEFAULT_BUFFER_SIZE = 128 * 1024;
	private StreamHelper() {}

	/**
	 * Copied in to out. Transfering proceeds untill the input is consumed or an exception is thrown.
	 * @param in source {@link InputStream}
	 * @param out destination {@link OutputStream}
	 * @param buffer provided buffer or NULL (method will allocate buffer on its own if needed)
	 * @return number of bytes transfered
	 * @throws IOException propagated from underlying stream operations
	 */
	public static long transfer( InputStream in, OutputStream out, byte[] buffer ) throws IOException {
		if( buffer == null || buffer.length == 0 ) {
			buffer = new byte[DEFAULT_BUFFER_SIZE];
		}
		int xfered = 0;
		long total_xfered = 0;
		while( xfered >= 0 ) {
			xfered = in.read( buffer );
			if( xfered > 0 ) {
				out.write( buffer, 0, xfered );
				total_xfered += xfered;
			}
		}
		return total_xfered;
	}

	/**
	 * Closes the given {@link Closeable} and silently swallows any {@link Throwable} that migh get thrown. Should only be used to augment handling stream exceptions and not as general close procedure.
	 * @param in {@link Closeable} to close
	 */
	public static void safeClose( Closeable cls ) {
		if( cls != null ) {
			try { cls.close(); } catch( Throwable ignored ) {}
		}
	}

}
