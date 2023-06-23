package com.prostudy.controller;

import com.prostudy.entity.RestBean;
import com.prostudy.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n";

    @Resource
    AuthorizeService service;

    @PostMapping("/valid-emali")
    public RestBean<String> validateEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email) {
        if (service.sendValidateEmail(email)) {
            return RestBean.success("邮件已发送，请注意查收");
        } else
            return RestBean.failure(400, "邮件发送失败，请联系管理员");
    }
}
