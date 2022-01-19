package com.proyectotaller.agendamiento_ms.exceptions;

public class AgendamientoNotFoundException extends RuntimeException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1619404115800530959L;

	public AgendamientoNotFoundException(String msg) {
		super(msg);
	}
}
