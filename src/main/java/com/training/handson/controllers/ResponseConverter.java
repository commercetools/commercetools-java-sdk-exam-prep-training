package com.training.handson.controllers;

import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.http.ResponseEntity;

public class ResponseConverter {
    public static <T> ResponseEntity<T> convert(ApiHttpResponse<T> response) {

        if (response.getBody() != null) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }

    }
}
