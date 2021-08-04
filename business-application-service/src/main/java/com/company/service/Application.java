package com.company.service;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.query.QueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SpringBootApplication
@EnableEncryptableProperties
@RestController
public class Application  {

    @Autowired
    private ProcessService processService;
    @Autowired
    private RuntimeDataService runtimeDataService;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    ApplicationContext appCtx;
    static {
      //  System.setProperty("jasypt.encryptor.password", "password");
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(@RequestParam Integer age) throws Exception {


        // Provided as an example. Not actually needed by our process.
        Map<String, Object> vars = new HashMap<>();
        vars.put("processVar1", "age");
        System.out.println("DB User: "+ appCtx.getEnvironment().getProperty("spring.datasource.username"));
        System.out.println("DB Pass decrypted: "+appCtx.getEnvironment().getProperty("spring.datasource.password"));
        Long processInstanceId = processService.startProcess("business-application-kjar-1_0-SNAPSHOT", "com.mastertheboss.LicenseDemo", vars);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("age", age);

        List<TaskSummary> taskSummaries = runtimeDataService.getTasksAssignedAsPotentialOwner("john", new QueryFilter());

        taskSummaries.forEach(s->{
            Status status = taskSummaries.get(0).getStatus();
            if ( status == Status.Ready )
                userTaskService.claim(s.getId(), "john");
            userTaskService.start(s.getId(), "john");
            userTaskService.complete(s.getId(), "john", params);
        });



        return ResponseEntity.status(HttpStatus.CREATED).body("Risk Ranking Task completed!");
    }
}