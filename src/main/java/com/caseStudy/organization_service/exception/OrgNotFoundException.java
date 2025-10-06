package com.caseStudy.organization_service.exception;

public class OrgNotFoundException extends RuntimeException{

    public OrgNotFoundException(String registryNumber) {
        super("Org could not found with this register number: " + registryNumber);
    }
}
