package com.dinero.Football.Aggregator.Client;


import com.dinero.Football.Aggregator.RateLimit.ApiQuotaTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class FootballApiClient {

    @Autowired
    private WebClient webClient;
    @Autowired
    private RedisTemplate<String , String> redisTemplate;
    @Autowired
    private ApiQuotaTracker apiQuotaTracker;

    public Mono<String> getFixtures(int leagueId, int season) {

        String cacheKey = "fixtures::" + leagueId + "::" + season;
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return Mono.just(cached);
        if (!apiQuotaTracker.isSafe()) { //unreachable code is from here
            return Mono.just("API quota exhausted");
        }

        Mono<String> apiCall = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures")
                        .queryParam("league", leagueId)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3 , Duration.ofMillis(500)))
                .doOnNext(result -> {redisTemplate
                        .opsForValue()
                        .set(cacheKey, result, 5, TimeUnit.MINUTES);
                    apiQuotaTracker.increment();
                });

        return apiCall;
    } // to here

    public Mono<String> getMatchById(String matchId){

        String cacheKey = "matchId::" + matchId;
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return Mono.just(cached);
        if (!apiQuotaTracker.isSafe()) { //unreachable code is from here
            return Mono.just("API quota exhausted");
        }


        Mono<String> apiCall = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("id" , matchId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3 , Duration.ofMillis(500)))
                .doOnNext(result -> {redisTemplate
                        .opsForValue()
                        .set(cacheKey , result , 5 , TimeUnit.MINUTES);
                    apiQuotaTracker.increment();});
        return apiCall;
    }

    public Mono<String> getStandings(int leagueId, int season){

        String cacheKey = "standings::" + leagueId + "::" + season;
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return Mono.just(cached);
        if (!apiQuotaTracker.isSafe()) { //unreachable code is from here
            return Mono.just("API quota exhausted");
        }

        Mono<String> apiCall = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/standings")
                        .queryParam("league" , leagueId)
                        .queryParam("season" , season)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3 , Duration.ofMillis(500)))
                .doOnNext(result -> { redisTemplate
                        .opsForValue()
                        .set(cacheKey , result , 5 , TimeUnit.MINUTES);
                apiQuotaTracker.increment();
                }
                );

        return apiCall;

    }

    public Mono<String> getLiveScores(int leagueId){

        String cacheKey = "leagueId::" + leagueId;
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return Mono.just(cached);
        if (!apiQuotaTracker.isSafe()) { //unreachable code is from here
            return Mono.just("API quota exhausted");
        }

        Mono<String> apiCall = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("league", leagueId)
                        .queryParam("live", "all")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3 , Duration.ofMillis(500)))
                .doOnNext(result -> { redisTemplate
                        .opsForValue()
                        .set(cacheKey , result , 5 , TimeUnit.MINUTES);
                apiQuotaTracker.increment();
                }
                );
        return apiCall;
    }


}
