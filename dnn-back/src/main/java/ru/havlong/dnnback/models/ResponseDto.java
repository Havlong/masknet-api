package ru.havlong.dnnback.models;

import java.util.Map;

import org.springframework.lang.Nullable;

import com.mongodb.lang.NonNull;

import ru.havlong.dnnback.utility.Status;

public record ResponseDto(@Nullable Map<String, Double> probabilities, @NonNull Status status,
        @Nullable String descString) {

}
