package com.wander.note.exception;


public class SessionException extends RuntimeException{

	private static final long serialVersionUID = 299216638599253989L;
	
	public SessionException(){
		
	}
	
	public SessionException(String message){
		super(message);
	}
}
