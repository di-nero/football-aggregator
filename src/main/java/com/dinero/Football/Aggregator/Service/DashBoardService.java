package com.dinero.Football.Aggregator.Service;


import com.dinero.Football.Aggregator.Client.FootballApiClient;
import com.dinero.Football.Aggregator.Config.AsyncConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class DashBoardService {

    @Autowired
    private FootballApiClient footballApiClient;
    @Autowired
    @Qualifier("footballExecutor")
    private Executor footballExecutor;

    public CompletableFuture<Map<String , String>> getDashboard(int leagueId, int season){

        CompletableFuture<String> fixturesFuture = CompletableFuture
                .supplyAsync(() -> footballApiClient.getFixtures(leagueId, season).block() , footballExecutor);

        CompletableFuture<String> standingsFuture = CompletableFuture
                .supplyAsync(() -> footballApiClient.getStandings(leagueId , season).block(), footballExecutor);

        CompletableFuture<String> liveScoresFuture = CompletableFuture
                .supplyAsync(() -> footballApiClient.getLiveScores(leagueId).block() , footballExecutor);

        CompletableFuture.allOf(fixturesFuture, standingsFuture, liveScoresFuture).join();

         HashMap<String , String> result = new HashMap<>();
         result.put("Fixtures" , fixturesFuture.join());
         result.put("Standing" , standingsFuture.join());
         result.put("LiveScore" , liveScoresFuture.join());

        return CompletableFuture.completedFuture(result);
    }
}
