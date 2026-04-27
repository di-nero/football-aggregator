package com.dinero.Football.Aggregator.Controllers;


import com.dinero.Football.Aggregator.Client.FootballApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/fixtures")
public class FixtureController {

    @Autowired
    private FootballApiClient footballApiClient;

    @GetMapping
    public Mono<String> getFixture(@RequestParam int leagueId , @RequestParam int season){

        return footballApiClient.getFixtures(leagueId , season);

    }


}
