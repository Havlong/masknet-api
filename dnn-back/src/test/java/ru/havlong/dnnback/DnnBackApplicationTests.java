package ru.havlong.dnnback;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ru.havlong.dnnback.repositories.RequestRepository;
import ru.havlong.dnnback.services.SnowflakeProvider;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(RequestRepository.class) // RequestRepository requires MongoDB connection to be set up
class DnnBackApplicationTests {

	@Autowired
	private SnowflakeProvider snowflakeProvider;

	@Test
	void testContextLoads() {
	}

	@Test
	void testGeneratedSnowflakesDiffer() {
		final long snowflake1 = snowflakeProvider.generateSnowflake();
		final long snowflake2 = snowflakeProvider.generateSnowflake();
		assertNotEquals(snowflake1, snowflake2);
	}

	@Test
	void testSnowflakeTemporalProperties() {
		Instant now = Instant.now();
		Instant dayBefore = now.minus(1, ChronoUnit.DAYS);
		Instant weekBefore = now.minus(7, ChronoUnit.DAYS);
		Instant yearBefore = now.minus(365, ChronoUnit.DAYS);
		assumeTrue(now.toEpochMilli() - dayBefore.toEpochMilli() == 1000*3600*24);
		
		assumeTrue(dayBefore.plus(1, ChronoUnit.DAYS).equals(now));
		assumeTrue(weekBefore.plus(7, ChronoUnit.DAYS).equals(now));
		assumeTrue(yearBefore.plus(365, ChronoUnit.DAYS).equals(now));

		final long snowflakeNow = snowflakeProvider.snowflakeOfTimestamp(now);
		final long snowflakeDayBefore = snowflakeProvider.snowflakeOfTimestamp(dayBefore);
		final long snowflakeWeekBefore = snowflakeProvider.snowflakeOfTimestamp(weekBefore);
		final long snowflakeYearBefore = snowflakeProvider.snowflakeOfTimestamp(yearBefore);
		
		assertTrue(snowflakeNow > snowflakeDayBefore);
		assertTrue(snowflakeDayBefore > snowflakeWeekBefore);
		assertTrue(snowflakeWeekBefore > snowflakeYearBefore);
	}
}
