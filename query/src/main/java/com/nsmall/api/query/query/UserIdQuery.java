package com.nsmall.api.query.query;


import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class UserIdQuery {
    private String userId;
}