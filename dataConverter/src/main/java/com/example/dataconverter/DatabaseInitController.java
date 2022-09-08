package com.example.dataconverter;

import com.example.dataconverter.Service.DatabaseInitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class DatabaseInitController {
    @Resource
    private DatabaseInitService databaseInitService;
    @PostMapping("/init")
    Map<String, Boolean> initSuccess(){
        return databaseInitService.init();
    }
}
