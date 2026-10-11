package com.quickcrave.controller;

import com.quickcrave.model.User;
import com.quickcrave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> finndUserByJwtToke(@RequestHeader("Authorization") String jwt) throws Exception {
       User user=userService.findUserByjwtToken(jwt);
       return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
