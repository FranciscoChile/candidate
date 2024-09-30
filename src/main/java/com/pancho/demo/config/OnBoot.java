package com.pancho.demo.config;

import com.pancho.demo.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnBoot implements ApplicationRunner {

    @Autowired
    private CandidateService candidateService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        candidateService.load();
    }
}
