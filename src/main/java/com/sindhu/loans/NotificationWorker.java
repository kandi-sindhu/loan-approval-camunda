package com.sindhu.loans;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles the "notify-applicant" service task. In production this would send an email or SMS.
 */
@Component
public class NotificationWorker {

    private static final Logger log = LoggerFactory.getLogger(NotificationWorker.class);

    @JobWorker(type = "notify-applicant")
    public void notifyApplicant(@Variable String applicantName,
                                @Variable String email,
                                @Variable String decision) {
        log.info("Sending '{}' decision to {} <{}>", decision, applicantName, email);
    }
}
