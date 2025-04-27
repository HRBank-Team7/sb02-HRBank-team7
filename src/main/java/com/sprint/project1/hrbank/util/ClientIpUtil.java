package com.sprint.project1.hrbank.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientIpUtil {

  /**
   * HttpServletRequest에서 클라이언트 IP 주소를 추출합니다.
   * 프록시나 로드 밸런서 환경을 고려하여 X-Forwarded-For, X-Real-IP 헤더를 확인하고,
   * 없으면 getRemoteAddr() 값을 반환합니다.
   *
   * @param request 클라이언트 요청 HttpServletRequest 객체
   * @return 클라이언트 IP 주소 문자열
   */
  public String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
      int commaIndex = ip.indexOf(',');
      if (commaIndex > 0) {
        ip = ip.substring(0, commaIndex);
      }
      return ip.trim();
    }

    ip = request.getHeader("X-Real-IP");
    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
      return ip.trim();
    }

    return request.getRemoteAddr();
  }
}
