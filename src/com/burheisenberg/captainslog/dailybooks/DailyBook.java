package com.burheisenberg.captainslog.dailybooks;

import com.burheisenberg.captainslog.loginauthentication.User;
import com.burheisenberg.captainslog.loginauthentication.UserLogin;

import java.time.LocalDate;

public class DailyBook {
    private User owner;
    private String title;
    private String content;
    private LocalDate date;

    public DailyBook(String title, String content, LocalDate date) {
        this.title = title;
        this.content = content;
        this.date = date;
        owner = UserLogin.getInstance().getAuthorizedUser();
    }

    public DailyBook(String title, String content, LocalDate date, User owner) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.owner = owner;
    }

    public DailyBook(String content) {
        this.title = "untitled_log";
        this.content=content;
        this.date = LocalDate.now();
        owner = UserLogin.getInstance().getAuthorizedUser();
    }

    public User getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return title;
    }
}
