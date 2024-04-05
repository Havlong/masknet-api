package ru.havlong.dnnback.models;

import java.util.List;

import org.springframework.lang.Nullable;

public record ParamsDto(@Nullable List<String> embeddedParams, @Nullable List<String> textParams,
        @Nullable List<String> numericalParams) {

}
