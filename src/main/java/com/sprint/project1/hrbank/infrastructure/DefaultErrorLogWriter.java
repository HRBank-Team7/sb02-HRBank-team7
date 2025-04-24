package com.sprint.project1.hrbank.infrastructure;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class DefaultErrorLogWriter implements ErrorLogWriter {

  @Override
  public byte[] generateLogBytes(Exception e) {
    StringBuilder sb = new StringBuilder("[" + Instant.now() + "] Backup error occurred:\n");
    sb.append(e.toString()).append("\n");
    for (StackTraceElement ste : e.getStackTrace()) {
      sb.append("    at ").append(ste.toString()).append("\n");
    }
    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }
}
