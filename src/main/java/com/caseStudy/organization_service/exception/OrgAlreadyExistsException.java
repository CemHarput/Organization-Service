package com.caseStudy.organization_service.exception;

public class OrgAlreadyExistsException extends RuntimeException{

    public OrgAlreadyExistsException(String registryNumber) {
        super("Org already exists with register number: " + registryNumber);
    }
}
