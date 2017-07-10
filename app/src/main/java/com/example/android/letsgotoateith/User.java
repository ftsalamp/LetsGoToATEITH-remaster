package com.example.android.letsgotoateith;

/**
 * Created by user on 7/8/17.
 */

public class User {

    private String name, email, uid, surname, fbLink;
    private int school;

    public User(String name, String email, String surname, String fbLink, int school) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.fbLink = fbLink;
        this.school=school;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getSurname() {
        return surname;
    }

    public String getFbLink() {
        return fbLink;
    }

    public int getSchool() {
        return school;
    }

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

}
