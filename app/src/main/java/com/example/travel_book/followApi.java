package com.example.travel_book;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class followApi {

    @JsonProperty("followerList")
    private String[] followerList;

    @JsonProperty("followingList")
    private String[] followingList;

    @JsonProperty("Result_email")
    private String[] Result_email;

    @JsonProperty("Result_to_location")
    private String[] Result_to_location;

    @JsonProperty("Search_KeyWord")
    private String[] Search_KeyWord;

    public String[] getFollowerList() {
        return followerList;
    }

    public void setFollowerList(String[] followerList) {
        this.followerList = followerList;
    }

    public String[] getFollowingList() {
        return followingList;
    }

    public void setFollowingList(String[] followingList) {
        this.followingList = followingList;
    }

    public String[] getResult_email() {
        return Result_email;
    }

    public void setResult_email(String[] result_email) {
        Result_email = result_email;
    }

    public String[] getResult_to_location() {
        return Result_to_location;
    }

    public void setResult_to_location(String[] result_to_location) {
        Result_to_location = result_to_location;
    }

    public String[] getSearch_KeyWord() {
        return Search_KeyWord;
    }

    public void setSearch_KeyWord(String[] search_KeyWord) {
        Search_KeyWord = search_KeyWord;
    }

}
