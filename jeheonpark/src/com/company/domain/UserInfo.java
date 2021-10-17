package com.company.domain;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private List<User> user_info = new ArrayList<>();


    public UserInfo() {
    }

    public UserInfo(List<User> user_info) {
        this.user_info = user_info;
    }

    public List<User> getUser_info() {
        return user_info;
    }

    public void setUser_info(List<User> user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user_info=" + user_info +
                '}';
    }
}
