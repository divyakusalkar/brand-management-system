package com.brandmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateBrandException extends RuntimeException {

    public DuplicateBrandException(String brandName, String chainName) {
        super("Brand '" + brandName + "' already exists under company '" + chainName + "'");
    }
}
