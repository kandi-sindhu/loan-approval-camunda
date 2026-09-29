# loan-approval-camunda

A loan approval workflow built on **Camunda 8**. The process is modelled in **BPMN 2.0**, the approval rules sit in a **DMN** decision table, and **Spring Boot** job workers do the actual work.

```
(start) ─▶ [Check credit score] ─▶ [Decide on loan (DMN)] ─▶ <Review needed?> ──No──▶ [Notify applicant] ─▶ (end)
             job worker               business rule task           │                        ▲
                                                                   └─Yes─▶ [Manual review] ─┘
                                                                           user task (Tasklist)
```

## Decision rules (`loan-decision.dmn`, hit policy FIRST)

| Credit score | Loan / annual income | Decision |
|---|---|---|
| < 580 | any | REJECTED |
| any | > 1.0 | REJECTED |
| >= 720 | <= 0.5 | APPROVED |
| anything else | | REVIEW (goes to an underwriter) |

Business users can change these rules without touching Java code.

## What it shows

- Orchestrating a process with BPMN: service tasks, a business rule task, an exclusive gateway with a FEEL condition, and a user task
- Keeping business rules in DMN, separate from the code
- Spring Boot `@JobWorker` handlers from the Camunda 8 Spring SDK, with automatic job completion and variable mapping
- Deploying the BPMN and DMN files automatically on startup with `@Deployment`
- Starting process instances from a validated REST API
- A local Camunda 8 stack (Zeebe, Operate, Tasklist, Elasticsearch) through Docker Compose

## Tech stack

Java 21 · Spring Boot 3 · Camunda 8.6 (Zeebe) · BPMN 2.0 · DMN 1.3 · FEEL · Docker Compose · Maven

## Run it

```bash
# 1. Start Camunda 8 (the first start takes a minute)
docker compose up -d

# 2. Start the app. It deploys the BPMN and DMN files and registers the workers.
mvn spring-boot:run

# 3. Apply for a loan
curl -X POST localhost:8080/api/loans \
  -H "Content-Type: application/json" \
  -d '{"applicantName":"Jane Doe","email":"jane@example.com","amount":20000,"annualIncome":85000}'
```

- **Operate** (http://localhost:8081, login `demo` / `demo`) shows each process instance moving through the diagram.
- **Tasklist** (http://localhost:8082, login `demo` / `demo`) is where an underwriter picks up applications that need review. Set the variable `decision` to `"APPROVED"` or `"REJECTED"`, then complete the task.

The credit score is simulated from the applicant's name, so different names lead to different paths through the process.

## Project layout

```
src/main/resources/bpmn/loan-approval.bpmn   Process model (open it in Camunda Modeler)
src/main/resources/dmn/loan-decision.dmn     Decision table
src/main/java/com/sindhu/loans/              REST controller + job workers
docker-compose.yml                           Local Camunda 8 stack
```

## Next steps

- Add a Camunda form to the manual review task
- Add a timer boundary event that escalates reviews after 2 days
- Add process tests with `zeebe-process-test`
