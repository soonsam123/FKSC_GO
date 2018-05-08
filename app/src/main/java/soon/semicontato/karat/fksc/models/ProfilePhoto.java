package soon.semicontato.karat.fksc.models;

public class ProfilePhoto {

    private String profile_img_url;

    public ProfilePhoto(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public ProfilePhoto() {

    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    @Override
    public String toString() {
        return "ProfilePhoto{" +
                "profile_img_url='" + profile_img_url + '\'' +
                '}';
    }
}
