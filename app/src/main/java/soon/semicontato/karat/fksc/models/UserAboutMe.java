package soon.semicontato.karat.fksc.models;

/**
 * Created by karat on 15/03/2018.
 */

public class UserAboutMe {

    private String cover_img_url;
    private String about_me;
    private String curriculum;

    public UserAboutMe(String cover_img_url, String about_me, String curriculum) {
        this.cover_img_url = cover_img_url;
        this.about_me = about_me;
        this.curriculum = curriculum;
    }

    // Needed for Firebase.
    public UserAboutMe() {

    }

    public String getCover_img_url() {
        return cover_img_url;
    }

    public void setCover_img_url(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    @Override
    public String toString() {
        return "UserAboutMe{" +
                "cover_img_url='" + cover_img_url + '\'' +
                ", about_me='" + about_me + '\'' +
                ", curriculum='" + curriculum + '\'' +
                '}';
    }
}
