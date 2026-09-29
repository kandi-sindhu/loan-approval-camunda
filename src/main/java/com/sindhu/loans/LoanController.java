package com.sindhu.loans;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final ZeebeClient zeebe;

    public LoanController(ZeebeClient zeebe) {
        this.zeebe = zeebe;
    }

    /** Starts a new "loan-approval" process instance and returns its key. */
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> apply(@Valid @RequestBody LoanRequest request) {
        var instance = zeebe.newCreateInstanceCommand()
                .bpmnProcessId("loan-approval")
                .latestVersion()
                .variables(request)
                .send()
                .join();

        return Map.of(
                "processInstanceKey", instance.getProcessInstanceKey(),
                "status", "SUBMITTED");
    }
}
