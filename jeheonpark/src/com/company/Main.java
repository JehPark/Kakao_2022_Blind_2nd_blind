package com.company;

import com.company.api.ApiConnection;
import com.company.api.domain.AuthKey;
import com.company.api.domain.Problem;
import com.company.domain.*;
import com.company.service.MatchService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    /**
     * Json Parser는 Gson과 Json을 사용하고 있습니다.
     * 출처 : https://github.com/google/gson Gson
     * 출처 : https://github.com/stleary/JSON-java Json
     *
     * Api는 직접 작성해서 사용하는 코드로 호출합니다.
     *
     * 1. API호출을 먼저 만들어 놓고 호출이 몇번 실행되어야 할지 확인.
     * 2. 문제를 읽어서 정확한 전략을 세워 놓자.
     * 3. 전략을 수정하면서 점수를 올려가자.
    **/
    public static void main(String[] args) throws IOException {
        // gson
        final Gson gson = new Gson();

        // start API
        final String baseURL = "https://huqeyhi95c.execute-api.ap-northeast-2.amazonaws.com/prod";
        final String token = "8078a6429eac65f783d97e264650c38e";
        final Problem problem = new Problem(2);
        final String noProblem = gson.toJson(problem);
        final ApiConnection startConn = new ApiConnection(baseURL, "/start", token);
        final String startResponse = startConn.makeConnection("POST", "X-Auth-Token", noProblem);
        final AuthKey authKeyRes = gson.fromJson(startResponse, AuthKey.class);
        final String authKey = authKeyRes.getAuth_key();

        // userInfo api
        final ApiConnection userInfoConn = new ApiConnection(baseURL, "/user_info", authKey);
        String userInfoResponse = userInfoConn.makeConnection("GET", "Authorization", "");
        UserInfo userInfo = gson.fromJson(userInfoResponse, UserInfo.class);

        // get waiting line;
        final ApiConnection waitingConn = new ApiConnection(baseURL, "/waiting_line", authKey);
        String waitingResponse = waitingConn.makeConnection("GET", "Authorization", "");
        WaitingLine waitingLine = gson.fromJson(waitingResponse, WaitingLine.class);

        // match conn
        final ApiConnection matchConn = new ApiConnection(baseURL, "/match", authKey);

        // gameResult api
        final ApiConnection gameResultConn = new ApiConnection(baseURL, "/game_result", authKey);
        // change grade
        final ApiConnection changeGradeConn = new ApiConnection(baseURL, "/change_grade", authKey);

        // MatchService
        final MatchService matchService = new MatchService(waitingLine, userInfo);
        Commands commands = matchService.initGrade();
        String commandsJson = gson.toJson(commands, Commands.class);
        String commandResponse = changeGradeConn.makeConnection("PUT", "Authorization", commandsJson);

        userInfoResponse = userInfoConn.makeConnection("GET", "Authorization", "");
        userInfo = gson.fromJson(userInfoResponse, UserInfo.class);
        matchService.setUsers(userInfo.getUser_info());
        for (int i = 0; i < 596; i++){
            // match api
            final Pairs pairs = matchService.matchingAlgorithm();
            final String pairContent = gson.toJson(pairs);
            String matchResponse = matchConn.makeConnection("PUT", "Authorization", pairContent);
            Status status = gson.fromJson(matchResponse, Status.class);

            if (i < 555){
                // waiting list api
                waitingResponse = waitingConn.makeConnection("GET", "Authorization", "");
                waitingLine = gson.fromJson(waitingResponse, WaitingLine.class);
                matchService.updateQueue(waitingLine);
                System.out.println(waitingLine);
            }

            // Game Result api
            String gameResultResponse = gameResultConn.makeConnection("GET", "Authorization", "");
            GameResults gameResults = gson.fromJson(gameResultResponse, GameResults.class);
            commands = matchService.updateGrade(gameResults);
            System.out.println(gameResults);
            commandsJson = gson.toJson(commands, Commands.class);
            commandResponse = changeGradeConn.makeConnection("PUT", "Authorization", commandsJson);
            userInfoResponse = userInfoConn.makeConnection("GET", "Authorization", "");
            userInfo = gson.fromJson(userInfoResponse, UserInfo.class);
            matchService.setUsers(userInfo.getUser_info());
        }

//         change grade api
        // score api
        final ApiConnection scoreConn = new ApiConnection(baseURL, "/score", authKey);
        final String score = scoreConn.makeConnection("GET", "Authorization", "");
        System.out.println("score = " + score);
    }
}
