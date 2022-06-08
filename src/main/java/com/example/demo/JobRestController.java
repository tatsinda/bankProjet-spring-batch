package com.example.demo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//@Lazy
public class JobRestController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private  BankTransactionItemAnalyticsProcessor analyticsProcessor;
    //api permettamt de lancer le job
    @GetMapping("/startJob")
    public BatchStatus load() throws Exception{
        System.out.println("TATSINDAM--------------------");
        Map<String, JobParameter> params= new HashMap<>();
        params.put("time",new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters=new JobParameters(params);
        JobExecution jobExecution=jobLauncher.run(job,jobParameters);
        while (jobExecution.isRunning())
        {
            System.out.println("........");
        }
        return jobExecution.getStatus();//permettant de voir le niveau d'avancement du job
    }

    //pour obtenir les donnees traitess le processor 2
    @GetMapping("/analytics")
    public Map<String,Double> analytics()
    {
        Map<String,Double> map=new HashMap<>();
        map.put("totalCredit",analyticsProcessor.getTotalCredit());
        map.put("totalDebit",analyticsProcessor.getTotalDebit());
        return map;
    }

}
