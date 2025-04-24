package com.sprint.project1.hrbank.infrastructure;

import java.io.IOException;

public interface ErrorLogWriter {
  byte[] generateLogBytes(Exception e) throws IOException;
}
