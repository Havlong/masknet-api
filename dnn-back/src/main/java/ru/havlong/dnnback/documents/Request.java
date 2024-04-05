package ru.havlong.dnnback.documents;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Data
@Document
public class Request {
    @Id
    @NonNull
    private Long id;

    @Nullable
    private Instant finishedAt;

    @Nullable
    private Map<String, String> embeddedParams;
    @Nullable
    private Map<String, Double> numericalParams;
}
