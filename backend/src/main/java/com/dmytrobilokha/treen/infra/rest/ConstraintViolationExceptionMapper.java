package com.dmytrobilokha.treen.infra.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException e) {
        var message = e
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(". "));
        LOG.error("Exception thrown in JAX-RS resource", e);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ExceptionResponse(message))
                .build();
    }

}
