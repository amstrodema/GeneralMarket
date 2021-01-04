package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 12/02/2019.
 */

public class HelpModel {
    private String HelpTitle;
    private String HelpMessage;

    public HelpModel(String helpTitle, String helpMessage) {
        HelpTitle = helpTitle;
        HelpMessage = helpMessage;
    }

    public String getHelpTitle() {
        return HelpTitle;
    }

    public void setHelpTitle(String helpTitle) {
        HelpTitle = helpTitle;
    }

    public String getHelpMessage() {
        return HelpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        HelpMessage = helpMessage;
    }
}
