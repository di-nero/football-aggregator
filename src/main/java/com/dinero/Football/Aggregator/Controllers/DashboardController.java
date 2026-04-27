package com.dinero.Football.Aggregator.Controllers;

import com.dinero.Football.Aggregator.Service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping
    public CompletableFuture<Map<String , String>> dashboard(@RequestParam int leagueId , @RequestParam int season){

        return dashBoardService.getDashboard(leagueId , season);

    }

}
