package com.sprint.project1.hrbank.util;

import com.sprint.project1.hrbank.exception.util.InvalidCursorFormatException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CursorManager {

  public static String encode(Long id) {
    if (id == null) {
      return null;
    }
    return Base64.getUrlEncoder()
        .encodeToString(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
  }

  public static Long decode(String cursor) {
    if (cursor == null || cursor.isBlank()) {
      return null;
    }
    try {
      String decoded = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
      return Long.parseLong(decoded);
    } catch (Exception e) {
      throw new IllegalArgumentException("포맷이 올바르지 않습니다");
    }
  }

  public static String decodeCursorToString(String cursor) {
    try {
      return new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
    }catch(IllegalArgumentException ex){
      throw new InvalidCursorFormatException("잘못된 커서 형식입니다.");
    }
  }
}
