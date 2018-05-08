package soon.semicontato.karat.fksc.models;

/**
 * Created by karat on 09/03/2018.
 */

public class UserSettings {

    private String belt_color;
    private String birth_date;
    private String user_id;

    public UserSettings(String belt_color, String birth_date, String user_id) {
        this.belt_color = belt_color;
        this.birth_date = birth_date;
        this.user_id = user_id;
    }


    public UserSettings() {

    }

    public String getBelt_color() {
        return belt_color;
    }

    public void setBelt_color(String belt_color) {
        this.belt_color = belt_color;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "belt_color='" + belt_color + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
