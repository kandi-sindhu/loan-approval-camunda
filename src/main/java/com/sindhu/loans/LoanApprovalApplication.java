package com.sindhu.loans;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Deploys the BPMN process and DMN decision to Zeebe on startup.
 */
@SpringBootApplication
@Deployment(resources = {"classpath*:/bpmn/*.bpmn", "classpath*:/dmn/*.dmn"})
public class LoanApprovalApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApprovalApplication.class, args);
    }
}
