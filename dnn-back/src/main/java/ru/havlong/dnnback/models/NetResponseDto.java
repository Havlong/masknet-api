package ru.havlong.dnnback.models;

import java.util.Map;

import org.springframework.lang.Nullable;

public record NetResponseDto(@Nullable Map<String, Double> probabilities) {

}
