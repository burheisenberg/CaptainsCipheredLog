package com.burheisenberg.captainslog.loginauthentication;

import java.util.Objects;

public class User {

    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return userName;
    }

    /**
     * couldn't succeed to override equals method due to the implementation of Serializable interface
     *
     *
     * @param user
     * @return
     */
    public boolean matches(User user) {

        if((this.userName == (user.getUserName())) && (this.password == (user.getPassword()))) {
            return true;
        }

        return false;
    }


    /**
     * it's done
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if((this.userName == user.getUserName()) && (this.password == user.getPassword())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password) + 83;
    }
}
