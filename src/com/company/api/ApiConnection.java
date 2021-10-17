package com.company.api;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
* 카카오 2차 코딩 테스트를 위해 본인(박제헌)이 직접 만든 코드입니다.
**/

public class ApiConnection {
    private String baseURL;
    private String specificURL;
    private String token;
    private URL requestURL;

    public ApiConnection(String baseURL, String specificURL, String token) throws MalformedURLException, UnsupportedEncodingException {
        this.baseURL = baseURL;
        this.specificURL = specificURL;
        this.token = token;
        this.requestURL = getRequestURL();
    }

    public ApiConnection(String baseURL, String specificURL, String token, String query) throws MalformedURLException, UnsupportedEncodingException {
        this.baseURL = baseURL;
        this.specificURL = specificURL;
        this.token = token;
        this.requestURL = getRequestURL(query);
    }

    private URL getRequestURL() throws UnsupportedEncodingException, MalformedURLException {
        return new URL(baseURL + specificURL);
    }

    private URL getRequestURL(String query) throws UnsupportedEncodingException, MalformedURLException {
        return new URL(baseURL + specificURL+ URLEncoder.encode(query, "UTF-8"));
    }

    public String makeConnection(String method, String tokenHeader) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
        setConnection(method, tokenHeader, conn);
        return getResponse(conn);
    }

    public String makeConnection(String method, String tokenHeader, String content) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
        setConnection(method, tokenHeader, conn);
        if (!content.equals("")){
            writeContent(content, conn);
        }
        return getResponse(conn);
    }

    private String getResponse(HttpURLConnection conn) throws IOException {
        final int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        if (responseCode == 200){
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null){
                response.append(line);
            }
        }else{
            System.out.println(responseCode);
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null){
                response.append(line);
            }
        }
        return response.toString();
    }

    private void setConnection(String method, String tokenHeader, HttpURLConnection conn) throws ProtocolException {
        if (!method.equals("GET")){
            conn.setDoOutput(true);
        }
        conn.setRequestMethod(method);
        conn.setRequestProperty(tokenHeader, token);
        conn.setRequestProperty("Content-Type", "application/json");
    }

    private void writeContent(String content, HttpURLConnection conn) throws IOException {
        final OutputStream os = conn.getOutputStream();
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        os.write(bytes, 0, bytes.length);
    }
}
