package com.sprint.project1.hrbank.entity.employee;

public enum EmployeeStatus {
  ACTIVE("재직중"), // enum 상수는 객체이다.
  ON_LEAVE("휴직중"),
  RESIGNED("퇴사");


  private final String status; // 한글 상태명

  EmployeeStatus(String status) { // 생성자로 enum 상수에 상태 할당
    this.status = status;
  }

  public String explain(){
    return this.status;
  }

  // Enum 항목을 넣으면 한글 상태 명으로 return 해주는 메서드
  public static String explainStatus(EmployeeStatus status){
    return status.explain();
  }

}
