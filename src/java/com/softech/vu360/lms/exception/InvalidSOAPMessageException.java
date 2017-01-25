package com.softech.vu360.lms.exception;

import com.softech.vu360.lms.helpers.ExceptionMessagesHelper;

/**
 * @author ramiz.uddin
 * @since 0.1
 */
public class InvalidSOAPMessageException extends Exception {

	public InvalidSOAPMessageException() {

		super(ExceptionMessagesHelper
				.getExceptionMessageByClass(InvalidSOAPMessageException.class
						.getName()));

	}

	public InvalidSOAPMessageException(String message) {

		super(ExceptionMessagesHelper
				.getExceptionMessageByClass(
						InvalidSOAPMessageException.class.getName())
				.concat("\n").concat(message));

	}

}
