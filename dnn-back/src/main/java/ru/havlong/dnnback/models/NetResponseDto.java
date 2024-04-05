package ru.havlong.dnnback.models;

import java.util.List;
import org.springframework.lang.Nullable;

public record NetResponseDto(@Nullable List<Double> probabilities) {

}
