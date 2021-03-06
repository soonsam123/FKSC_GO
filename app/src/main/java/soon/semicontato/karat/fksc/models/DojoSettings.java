package soon.semicontato.karat.fksc.models;

/**
 * Created by karat on 17/03/2018.
 */

public class DojoSettings {

    private String address;
    private String telephone;
    private String description;
    private String secret_name;
    private String user_id;
    private String dojo_number;

    public DojoSettings(String address, String telephone, String description, String secret_name, String user_id, String dojo_number) {
        this.address = address;
        this.telephone = telephone;
        this.description = description;
        this.secret_name = secret_name;
        this.user_id = user_id;
        this.dojo_number = dojo_number;
    }


    public DojoSettings() {

    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecret_name() {
        return secret_name;
    }

    public void setSecret_name(String secret_name) {
        this.secret_name = secret_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDojo_number() {
        return dojo_number;
    }

    public void setDojo_number(String dojo_number) {
        this.dojo_number = dojo_number;
    }


    @Override
    public String toString() {
        return "DojoSettings{" +
                "address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", description='" + description + '\'' +
                ", secret_name='" + secret_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", dojo_number='" + dojo_number + '\'' +
                '}';
    }
}
