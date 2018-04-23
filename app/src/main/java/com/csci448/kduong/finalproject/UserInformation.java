package com.csci448.kduong.finalproject;

/**
 * Created by The Ngo on 4/22/2018.
 */

public class UserInformation {
    public String name;
    public int age;
    public String bio;

    public UserInformation() {

    }

    public UserInformation(String name, int age, String bio) {
        this.name = name;
        this.age = age;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
