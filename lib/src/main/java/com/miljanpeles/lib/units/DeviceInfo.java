package com.miljanpeles.lib.units;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Za koriscenje ovih metoda potrebna je permisija READ_PRIVILEGED_PHONE_STATE
 */
public class DeviceInfo {

	private DeviceInfo() {}

	@SuppressLint("MissingPermission")
	public static String getPrimaryImei(Context context) {
		String manufacturer = Build.MANUFACTURER;

		TelephonyManager tm = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE );
		Class<TelephonyManager> tmcls = TelephonyManager.class;
		if( "Xiaomi".equalsIgnoreCase( manufacturer ) ) {
			try {
				for( Method m : tmcls.getMethods() ) {
					if( Modifier.isStatic( m.getModifiers() ) && m.getName().equals( "from" ) && m.getParameterTypes().length == 2 ) {
						tm = (TelephonyManager)m.invoke( null, context, 0 );
					}
				}
			} catch( Throwable ignore ) {}
		} else if( "HUAWEI".equalsIgnoreCase( manufacturer ) ) {
			try {
				for( Method m : tmcls.getMethods() ) {
					if( !Modifier.isStatic( m.getModifiers() ) && m.getName().equals( "getDeviceId" ) ) {
						Class<?>[] params = m.getParameterTypes();
						if( params.length == 1 && params[0].equals( Integer.TYPE ) ) {
							return (String) m.invoke( tm, 0 );
						}
					}
				}
			} catch( Throwable ignore ) {}
		}
		return tm.getDeviceId();
	}

	@SuppressLint("MissingPermission")
	public static String[] getSimSerials(Context context) {
		String manufacturer = Build.MANUFACTURER;
		TelephonyManager tm = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE );
		String ssn1 = n2e( tm.getSimSerialNumber() );
		String ssn2 = "";
		Class<TelephonyManager> tmcls = TelephonyManager.class;
		if( "Xiaomi".equalsIgnoreCase( manufacturer ) ) {
			try {
				for( Method m : tmcls.getMethods() ) {
					if( Modifier.isStatic( m.getModifiers() ) && m.getName().equals( "from" ) && m.getParameterTypes().length == 2 ) {
						tm = (TelephonyManager)m.invoke( null, context, 0 );
						ssn1 = n2e( tm.getSimSerialNumber() );
						tm = (TelephonyManager)m.invoke( null, context, 1 );
						ssn2 = n2e( tm.getSimSerialNumber() );
					}
				}
			} catch( Throwable ignore ) {}
		} else if( "HUAWEI".equalsIgnoreCase( manufacturer ) ) {
			try {
				for( Method m : tmcls.getMethods() ) {
					if( !Modifier.isStatic( m.getModifiers() ) && m.getName().equals( "getSimSerialNumber" ) ) {
						Class<?>[] params = m.getParameterTypes();
						if( params.length == 1 && params[0].equals( Integer.TYPE ) ) {
							ssn1 = n2e( (String) m.invoke( tm, 0 ) );
							ssn2 = n2e( (String) m.invoke( tm, 1 ) );
						}
					}
				}
			} catch( Throwable ignore ) {}
		}
		return new String[] { ssn1, ssn2 };
	}

	public static String getSortedSimSerials(Context context) {
		String[] serials = getSimSerials(context);
		return mergeSorted( serials[0], serials[1] );
	}

	private static String n2e( String s ) { return s == null ? "" : s; }

	private static String mergeSorted( String ssn1, String ssn2 ) {
		return ssn1.compareTo( ssn2 ) >= 0 ? ( ssn1 + ssn2 ) : ( ssn2 + ssn1 );
	}
	
	public static String getUnderscoredSimSerials(Context context) {
		String[] serials = getSimSerials(context); //always length 2
		String returnValue = serials[0];
		if( !serials[1].equals("") ) returnValue += "_" + serials[1];
		return returnValue;
	}

}
