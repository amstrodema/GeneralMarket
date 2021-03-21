package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Setting;

/**
 * Created by ELECTRON on 08/10/2020.
 */

public class SettingsModel {
    private String id, settingType, settingValue;
    private Double settingValueDouble;
    public SettingsModel() {

    }

    public SettingsModel(String id, String settingType, String settingValue, Double settingValueInt) {
        this.id = id;
        this.settingType = settingType;
        this.settingValue = settingValue;
        this.settingValueDouble = settingValueInt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public Double getSettingValueDouble() {
        return settingValueDouble;
    }

    public void setSettingValueDouble(Double settingValueDouble) {
        this.settingValueDouble = settingValueDouble;
    }
}
