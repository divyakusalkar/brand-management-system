package com.brandmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BrandLinkedToZoneException extends RuntimeException {

    public BrandLinkedToZoneException(String brandName) {
        super("Brand '" + brandName + "' cannot be deleted because it is linked to one or more Zones.");
    }
}
