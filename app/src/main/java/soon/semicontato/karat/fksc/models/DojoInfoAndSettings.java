package soon.semicontato.karat.fksc.models;

/**
 * Created by karat on 17/03/2018.
 */

public class DojoInfoAndSettings {

    private DojoInfo dojoInfo;
    private DojoSettings dojoSettings;

    public DojoInfoAndSettings(DojoInfo dojoInfo, DojoSettings dojoSettings) {
        this.dojoInfo = dojoInfo;
        this.dojoSettings = dojoSettings;
    }


    public DojoInfoAndSettings() {

    }

    public DojoInfo getDojoInfo() {
        return dojoInfo;
    }

    public void setDojoInfo(DojoInfo dojoInfo) {
        this.dojoInfo = dojoInfo;
    }

    public DojoSettings getDojoSettings() {
        return dojoSettings;
    }

    public void setDojoSettings(DojoSettings dojoSettings) {
        this.dojoSettings = dojoSettings;
    }

    @Override
    public String toString() {
        return "DojoInfoAndSettings{" +
                "dojoInfo=" + dojoInfo +
                ", dojoSettings=" + dojoSettings +
                '}';
    }
}
