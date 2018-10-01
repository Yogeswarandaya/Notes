package com.wander.note.exception;

/**
 * @author home
 *
 */
public class BadRequestException extends RuntimeException{

	private static final long serialVersionUID = 299216638599253989L;
	
	public BadRequestException(){
		
	}
	
	public BadRequestException(String message){
		super(message);
	}
}
