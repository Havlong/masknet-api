package ru.havlong.dnnback.models;

import java.util.Map;

import org.springframework.lang.Nullable;

public record RequestDto(@Nullable Map<String, Integer> embeddedParams, @Nullable Map<String, String> textParams,
        @Nullable Map<String, Double> numericalParams) {

}
