package com.sindhu.loans;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Handles the "credit-check" service task.
 * Simulates a credit bureau call; the returned map is merged into the process variables.
 */
@Component
public class CreditCheckWorker {

    private static final Logger log = LoggerFactory.getLogger(CreditCheckWorker.class);

    @JobWorker(type = "credit-check")
    public Map<String, Object> checkCredit(@Variable String applicantName) {
        int creditScore = 500 + Math.floorMod(applicantName.toLowerCase().hashCode(), 350); // 500..849
        log.info("Credit score for {} is {}", applicantName, creditScore);
        return Map.of("creditScore", creditScore);
    }
}
