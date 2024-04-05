package ru.havlong.dnnback.services;

import java.time.Instant;

public interface SnowflakeProvider {
    public Long generateSnowflake();

    public Long snowflakeOfTimestamp(Instant timeInstant);

    public Instant instantOfSnowflake(Long snowflake);
}
