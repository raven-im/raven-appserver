package com.tim.appserver.user.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;


/**
 * @author: bbpatience
 * @date: 2018/10/20
 * @description: InputUserCreate
 **/
public class InputUserCreate {

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("user_type")
    private Integer userType;

    @JsonProperty("password")
    private String password;

    @JsonProperty("birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    @JsonProperty("gender")
    private Integer gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
