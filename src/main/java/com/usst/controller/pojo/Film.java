package com.usst.controller.pojo;

public class Film {
  private String title;
  private String director;
  private String image;
  private double metaScore;
  private double userScore;
  private String comment;
  private String summary;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public double getMetaScore() {
    return metaScore;
  }

  public void setMetaScore(double metaScore) {
    this.metaScore = metaScore;
  }

  public double getUserScore() {
    return userScore;
  }

  public void setUserScore(double userScore) {
    this.userScore = userScore;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }
}
