package com.divitbui.exception;

public class TransferFailedException extends Exception {

    public TransferFailedException(RuntimeException e) {
        super(e);
    }

}
