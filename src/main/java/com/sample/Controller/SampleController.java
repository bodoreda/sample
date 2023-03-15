package com.sample.Controller;

import com.sample.Service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SampleController {
    @Autowired
    SampleService sampleService;

    @GetMapping("/hello")
    public String helloWorld() {
        return "hello!!";
    }

    @PostMapping("/member")
    public void createMember(){

    }

    @GetMapping("/memberList")
    public void getMemberList(){

    }

    @GetMapping("/member")
    public void getMember(){

    }

    @PutMapping("/member")
    public void updateMember(){

    }

    @DeleteMapping("/member")
    public void deleteMember(){

    }
}
