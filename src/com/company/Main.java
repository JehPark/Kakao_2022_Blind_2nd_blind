package com.company;

import com.company.api.ApiConnection;
import com.company.api.ApiMethods;
import com.company.api.Headers;
import com.company.api.URLs;
import com.company.api.domain.AuthKey;
import com.company.api.domain.Problem;
import com.company.domain.*;
import com.company.service.MatchService;
import com.google.gson.Gson;

import java.io.IOException;

public class Main {
    public static final String TOKEN = "8078a6429eac65f783d97e264650c38e";
    public static final int PROBLEM = 2;
    public static final int TIMES = 596;
    public static final int ENT_REQUEST = 555;

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
        final String authKey = startAPI(gson);

        // userInfo api
        final ApiConnection userInfoConn = new ApiConnection(URLs.BASE_URL, URLs.USER_INFO_URL, authKey);
        String userInfoResponse = userInfoConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
        UserInfo userInfo = gson.fromJson(userInfoResponse, UserInfo.class);

        // get waiting line;
        final ApiConnection waitingConn = new ApiConnection(URLs.BASE_URL, URLs.WAITING_LINE_URL, authKey);
        String waitingResponse = waitingConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
        WaitingLine waitingLine = gson.fromJson(waitingResponse, WaitingLine.class);

        // match conn
        final ApiConnection matchConn = new ApiConnection(URLs.BASE_URL, URLs.MATCH_URL, authKey);
        // gameResult api
        final ApiConnection gameResultConn = new ApiConnection(URLs.BASE_URL, URLs.GAME_RESULT_URL, authKey);
        // change grade
        final ApiConnection changeGradeConn = new ApiConnection(URLs.BASE_URL, URLs.CHANGE_GRADE_URL, authKey);

        // MatchService
        final MatchService matchService = new MatchService(waitingLine, userInfo);
        Commands commands = matchService.initGrade();
        String commandsJson = gson.toJson(commands, Commands.class);
        String commandResponse = changeGradeConn.makeConnection(ApiMethods.PUT_METHOD, Headers.AUTHORIZATION, commandsJson);

        userInfoResponse = userInfoConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
        userInfo = gson.fromJson(userInfoResponse, UserInfo.class);
        matchService.setUsers(userInfo.getUser_info());
        for (int i = 0; i < TIMES; i++){
            // match api
            final Pairs pairs = matchService.matchingAlgorithm();
            final String pairContent = gson.toJson(pairs);
            String matchResponse = matchConn.makeConnection(ApiMethods.PUT_METHOD, Headers.AUTHORIZATION, pairContent);
            Status status = gson.fromJson(matchResponse, Status.class);

            if (i < ENT_REQUEST){
                // waiting list api
                waitingResponse = waitingConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
                waitingLine = gson.fromJson(waitingResponse, WaitingLine.class);
                matchService.updateQueue(waitingLine);
                System.out.println(waitingLine);
            }

            // Game Result api
            String gameResultResponse = gameResultConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
            GameResults gameResults = gson.fromJson(gameResultResponse, GameResults.class);
            commands = matchService.updateGrade(gameResults);
            System.out.println(gameResults);
            commandsJson = gson.toJson(commands, Commands.class);
            commandResponse = changeGradeConn.makeConnection(ApiMethods.PUT_METHOD, Headers.AUTHORIZATION, commandsJson);
            userInfoResponse = userInfoConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
            userInfo = gson.fromJson(userInfoResponse, UserInfo.class);
            matchService.setUsers(userInfo.getUser_info());
        }

//         change grade api
        // score api
        final ApiConnection scoreConn = new ApiConnection(URLs.BASE_URL, URLs.SCORE_URL, authKey);
        final String score = scoreConn.makeConnection(ApiMethods.GET_METHOD, Headers.AUTHORIZATION);
        System.out.println("score = " + score);
    }

    private static String startAPI(Gson gson) throws IOException {
        // start API
        final Problem problem = new Problem(PROBLEM);
        final String noProblem = gson.toJson(problem);
        final ApiConnection startConn = new ApiConnection(URLs.BASE_URL, URLs.START_URL, TOKEN);
        final String startResponse = startConn.makeConnection(ApiMethods.POST_METHOD, Headers.X_AUTH_TOKEN, noProblem);
        final AuthKey authKeyRes = gson.fromJson(startResponse, AuthKey.class);
        return authKeyRes.getAuth_key();
    }
}
