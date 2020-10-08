package com.moodtoday.dto;

public class DiaryDTO {
    int _id;
    String date;
    int weather;
    String music;
    String contents;
    String wording;
    int iconNum;
    String member;

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public int getIconNum() {
        return iconNum;
    }

    public void setIconNum(int iconNum) {
        this.iconNum = iconNum;
    }
}

