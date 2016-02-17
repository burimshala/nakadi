package de.zalando.aruha.nakadi.controller;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.Responses;

import javax.ws.rs.core.Response;

@ControllerAdvice
public final class ExceptionHandling implements ProblemHandling {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandling.class);

    @Override
    public String formatFieldName(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    @Override
    @ExceptionHandler
    public ResponseEntity<Problem> handleThrowable(final Throwable throwable, final NativeWebRequest request) {
        final String errorTraceId = generateErrorTraceId();
        LOG.error("InternalServerError (" + errorTraceId + "):", throwable);
        return Responses.create(Response.Status.INTERNAL_SERVER_ERROR, "An internal error happened. Please report it. (" + errorTraceId + ")", request);
    }

    private String generateErrorTraceId() {
        return "ETI" + RandomStringUtils.randomAlphanumeric(24);
    }


}
