package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Setting;

public class SettingsModel {
    private String settingName, valueText ="", dataType;
    private int valueInt= 0;
    private double valueDouble =0;

    public SettingsModel() {
    }

    public SettingsModel(String settingName, String valueText, String dataType, int valueInt, double valueDouble) {
        this.settingName = settingName;
        this.valueText = valueText;
        this.dataType = dataType;
        this.valueInt = valueInt;
        this.valueDouble = valueDouble;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public double getValueDouble() {
        return valueDouble;
    }

    public void setValueDouble(double valueDouble) {
        this.valueDouble = valueDouble;
    }
}
