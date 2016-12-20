package com.loic.perso.health;


import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeConsumedIndicator extends AbstractHealthIndicator {

    private LocalDateTime localDateTime = LocalDateTime.now();

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.withDetail("last.time.checked", localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}