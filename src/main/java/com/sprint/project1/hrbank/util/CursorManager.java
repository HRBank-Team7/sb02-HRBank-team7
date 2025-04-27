package com.sprint.project1.hrbank.util;

import com.sprint.project1.hrbank.exception.util.InvalidCursorFormatException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class CursorManager {

  public static String encode(Instant instant) {
    if (instant == null) {
      return null;
    }
    String instantString = instant.toString();
    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(instantString.getBytes(StandardCharsets.UTF_8));
  }

  public static Instant decode(String cursor) {
    if (cursor == null || cursor.isBlank()) {
      return null;
    }
    try {
      String decodedString = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
      return Instant.parse(decodedString);
    } catch (Exception e) {
      throw new InvalidCursorFormatException("잘못된 커서 포맷 입니다");
    }
  }
}
