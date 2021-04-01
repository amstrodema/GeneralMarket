package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Notification;

/**
 * Created by ELECTRON on 05/12/2020.
 */

public class NotificationModel {
    private String id, fromId, toId,body, isSeen, dateTime, picsId;
    private int channelNo;

    public NotificationModel() {
    }

    public NotificationModel(String id, String fromId, String toId, String body, String isSeen, String dateTime, String picsId, int channelNo) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.body = body;
        this.isSeen = isSeen;
        this.dateTime = dateTime;
        this.picsId = picsId;
        this.channelNo = channelNo;
    }

    public int getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getPicsId() {
        return picsId;
    }

    public void setPicsId(String picsId) {
        this.picsId = picsId;
    }
}
