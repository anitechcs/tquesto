package com.anitech.tquesto.exception;

/**
 * Custom Top level Exception class
 * 
 * @author Tapas
 *
 */
public class TquestoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TquestoException() {
		super();
	}

	public TquestoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TquestoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TquestoException(String message) {
		super(message);
	}

	public TquestoException(Throwable cause) {
		super(cause);
	}
	
}
