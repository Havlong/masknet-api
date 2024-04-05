package ru.havlong.dnnback.models;

import java.util.Map;

import org.springframework.lang.Nullable;

public record RequestDto(@Nullable Map<String, String> embeddedParams, @Nullable Map<String, Double> numericalParams) {

}
