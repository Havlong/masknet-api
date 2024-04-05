package ru.havlong.dnnback.services.impls;

import java.time.Instant;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.havlong.dnnback.services.SnowflakeProvider;

@Service
public class SnowflakeProviderFast implements SnowflakeProvider {

    private final int SERIAL_BITS = 10;
    private final int SHARD_BITS = 10;
    private final int TIME_BITS = 64 - (SERIAL_BITS + SHARD_BITS);

    private final int SERIAL_SHIFT = 0;
    private final int SHARD_SHIFT = SERIAL_SHIFT + SERIAL_BITS;
    private final int TIME_SHIFT = SHARD_SHIFT + SHARD_BITS;

    private final long TIME_MOD = 1L << TIME_BITS;

    private long lastSnowflake = 0;
    private long shardId;

    public SnowflakeProviderFast(@Value("${app.shardid}") int shardId) {
        int threadId = (int) Thread.currentThread().getId();
        this.shardId = (shardId << (SHARD_BITS / 2)) + threadId;
        this.shardId <<= SHARD_SHIFT;
    }

    @Override
    public Long generateSnowflake() {
        long baseline = snowflakeOfTimestamp(Instant.now());
        if (baseline <= this.lastSnowflake) {
            baseline = ++this.lastSnowflake;
        } else {
            baseline += this.shardId;
            this.lastSnowflake = baseline;
        }
        return baseline;
    }

    @Override
    public Long snowflakeOfTimestamp(Instant timeInstant) {
        long millis = timeInstant.toEpochMilli();
        millis = Long.remainderUnsigned(millis, TIME_MOD);
        return millis << TIME_SHIFT;
    }

    @Override
    public Instant instantOfSnowflake(Long snowflake) {
        if (Objects.isNull(snowflake))
            return Instant.EPOCH;

        long millis = snowflake >> TIME_SHIFT;
        return Instant.ofEpochMilli(millis);
    }

}
