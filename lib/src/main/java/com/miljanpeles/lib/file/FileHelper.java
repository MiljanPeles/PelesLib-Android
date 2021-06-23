package com.miljanpeles.lib.file;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileHelper {
	private FileHelper() {}

	/**
	 * Brise sve fajlove u datom direktorijumu, nece obrisate prethodne direktorijume niti njihov sadrzaj
	 * @param dst_dir direktorija za ciscenje
	 */
	public static void clearFilesInDir( File dst_dir ) {
		if( dst_dir.exists() && dst_dir.isDirectory() ) {
			File list[] = dst_dir.listFiles();
			for( File f : list ) {
				if( f.isFile() ) {
					f.delete();
				}
			}
		}
	}

	private static final FileFilter move_filter = new FileFilter() { @Override public boolean accept( File pathname ) { return pathname.isFile(); } };
	public static boolean moveDir( File src_dir, File dst_dir ) {
		boolean result = false;
		if( src_dir.isDirectory() && ( dst_dir.isDirectory() || dst_dir.mkdirs() ) ) {
			result = true;
			File[] src_list = src_dir.listFiles( move_filter );
			for( File f : src_list ) {
				result &= move( f, new File( dst_dir, f.getName() ) );
			}
		}
		return result;
	}

	/**
	 * Pomera file. Operacija pokusava da bude safe, ako ne uspe pokusace da vrati file sistem u originalno stanje (sto isto ne moze da se garantuje)
	 * @param src source fajl
	 * @param dst destination fajl
	 * @return true ako je uspesno, false ako bilo sta ne uspe
	 */
	public static boolean move( File src, File dst ) {
		File dst_dir = dst.getParentFile();
		boolean ok = src.exists() && src.isFile() && !dst.exists() && ( dst_dir.isDirectory() || dst_dir.mkdirs() );
		if( ok ) {
			if( !src.renameTo( dst ) ) {
				ok = copy( src, dst ) && src.delete();
				if( !ok && src.exists() ) { dst.delete(); }
			}
		}
		return ok;
	}

	/**
	 * Kopira fajl. Nece ostaviti junk kopiran fajl ako kopiranje ne uspe
	 * @param src source fajl
	 * @param dst destination fajl
	 * @return true ako je uspesno, false ako bilo sta ne uspe
	 */
	public static boolean copy( File src, File dst ) {
		File dst_dir = dst.getParentFile();
		boolean ok = dst_dir.isDirectory() || dst_dir.mkdirs();
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		if( ok ) {
			try {
				fis = new FileInputStream( src );
				fos = new FileOutputStream( dst );
				inChannel = fis.getChannel();
				outChannel = fos.getChannel();
				long xfered = 0;
				while( xfered < inChannel.size() ) {
					xfered += outChannel.transferFrom( inChannel, xfered, inChannel.size() - xfered );
				}
				outChannel.close();
				outChannel = null;
				inChannel.close();
				inChannel = null;
				fos.close();
				fos = null;
				fis.close();
				fis = null;
			} catch( Exception e ) {
				//Logger.exception( e );
				ok = false;
			}
			finally {
				StreamHelper.safeClose( outChannel );
				StreamHelper.safeClose( inChannel );
				StreamHelper.safeClose( fos );
				StreamHelper.safeClose( fis );
				if( !ok ) { dst.delete(); }
			}
		}
		return ok;
	}

	/**
	 * Upisuje niz bajtova u fajl
	 * @param file - fajl u koji upisujem
	 * @param data - data koji upisujem
	 * @throws IOException - exception
	 */
	public static void save( File file, byte[] data ) throws IOException {
		OutputStream os = null;
		IOException e = null;
		try {
			os = new FileOutputStream( file );
			os.write( data );
		} catch( IOException ioe ) {
			e = ioe;
		}
		try {
			if( os != null ) {
				os.close();
			}
		} catch( IOException ioe ) {
			if( e == null ) { e = ioe; }
		}
		if( e != null ) { throw e; }
	}

	/**
	 * Upisuje vise nizova bajtova u fajl
	 * @param file - fajl u koji upisujem
	 * @param data - data koji upisujem
	 * @throws IOException - exception
	 */
	public static void save( File file, byte[] ... data ) throws IOException {
		OutputStream os = null;
		IOException e = null;
		try {
			os = new FileOutputStream( file );
			for( byte[] d : data ) {
				os.write( d );
			}
		} catch( IOException ioe ) {
			e = ioe;
		}
		try {
			if( os != null ) {
				os.close();
			}
		} catch( IOException ioe ) {
			if( e == null ) { e = ioe; }
		}
		if( e != null ) { throw e; }
	}

	/**
	 * Ucitava file u niz bajtova
	 * @param file - file koji ucitavam
	 * @return - niz bajtova
	 * @throws IOException - exception
	 */
	public static byte[] load( File file ) throws IOException {
		InputStream is = null;
		IOException e = null;
		byte[] result = null;
		try {
			int len = (int)file.length();
			int off = 0;
			result = new byte[len];
			is = new FileInputStream( file );
			while( off < len ) {
				off += is.read( result, off, len - off );
			}
		} catch( IOException ioe ) {
			e = ioe;
		}
		try {
			if( is != null ) {
				is.close();
			}
		} catch( IOException ioe ) {
			if( e == null ) { e = ioe; }
		}
		if( e != null ) { throw e; }
		return result;
	}


	/**
	 * Uzima size fajla
	 * @param file fajl
	 * @return - velicinu fajla
	 */
	public static long getFileSize(@NonNull File file) {
		long file_size = 0;
		if (file.exists()) {
			file_size = file.length();
		}
		return file_size;
	}



}
