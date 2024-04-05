package ru.havlong.dnnback.models;

import java.util.List;
import org.springframework.lang.Nullable;

import com.mongodb.lang.NonNull;

import ru.havlong.dnnback.utility.Status;

public record ResponseDto(@Nullable List<Double> probabilities, @NonNull Status status,
        @Nullable String descString) {

}
