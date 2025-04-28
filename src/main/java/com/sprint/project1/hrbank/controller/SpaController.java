package com.sprint.project1.hrbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

  @GetMapping({"/", "/dashboard", "/departments", "/change-logs", "/employees", "/backups"})
  public String redirect() {
    return "forward:/index.html";
  }
}