package com.dinero.Football.Aggregator.Controllers;


import com.dinero.Football.Aggregator.Queue.ReportProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    private ReportProducer reportProducer;

    @PostMapping("/api/report/generate")
    public ResponseEntity<String> postReport(@RequestBody String matchId){

        reportProducer.publishReportJob(matchId);
        return ResponseEntity.accepted().body("Report generation queued");

    }

}
