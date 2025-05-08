package com.vihan.Drive.Management.Controller.Internal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    @PostMapping(value = "/auth/{passwordEntered}/{userId}")
    public boolean isAuthenticated(
            @PathVariable(value = "passwordEntered") String passwordEntered,
            @PathVariable(value = "userId") String userId) {

        return true;
    }
}
