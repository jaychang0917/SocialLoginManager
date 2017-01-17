package com.jaychang.slm;

import java.io.Serializable;

public class SocialUser implements Serializable {

  public String userId;
  public String accessToken;
  public String photoUrl;
  public Profile profile;

  public SocialUser() {
  }

  public SocialUser(SocialUser other) {
    this.userId = other.userId;
    this.accessToken = other.accessToken;
    this.photoUrl = other.photoUrl;
    if (other.profile != null) {
      this.profile = new Profile(other.profile);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SocialUser that = (SocialUser) o;

    return userId != null ? userId.equals(that.userId) : that.userId == null;

  }

  @Override
  public int hashCode() {
    return userId != null ? userId.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SocialUser{");
    sb.append("userId='").append(userId).append('\'');
    sb.append(", accessToken='").append(accessToken).append('\'');
    sb.append(", photoUrl='").append(photoUrl).append('\'');
    sb.append(", profile=").append(profile);
    sb.append('}');
    return sb.toString();
  }

  public static class Profile implements Serializable {
    public String name;
    public String fullName;
    public String email;
    public String pageLink;

    public Profile() {
    }

    public Profile(Profile other) {
      this.name = other.name;
      this.email = other.email;
      this.pageLink = other.pageLink;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("Profile{");
      sb.append("name='").append(name).append('\'');
      sb.append(", email='").append(email).append('\'');
      sb.append(", pageLink='").append(pageLink).append('\'');
      sb.append('}');
      return sb.toString();
    }
  }

}
