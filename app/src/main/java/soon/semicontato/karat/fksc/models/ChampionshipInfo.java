package soon.semicontato.karat.fksc.models;

/**
 * Created by karat on 17/03/2018.
 */

public class ChampionshipInfo {

    private String name;
    private String city;
    private String date_hour;
    private String address;
    private String cover_img_url;

    public ChampionshipInfo(String name, String city, String date_hour, String address, String cover_img_url) {
        this.name = name;
        this.city = city;
        this.date_hour = date_hour;
        this.address = address;
        this.cover_img_url = cover_img_url;
    }


    public ChampionshipInfo() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_hour() {
        return date_hour;
    }

    public void setDate_hour(String date_hour) {
        this.date_hour = date_hour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCover_img_url() {
        return cover_img_url;
    }

    public void setCover_img_url(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }

    @Override
    public String toString() {
        return "ChampionshipInfo{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", date_hour='" + date_hour + '\'' +
                ", address='" + address + '\'' +
                ", cover_img_url='" + cover_img_url + '\'' +
                '}';
    }
}
