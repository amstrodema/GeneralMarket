package comgalaxyglotech.confirmexperts.generalmarket.Trash;

/**
 * Created by ELECTRON on 10/06/2019.
 */

public class ArchiveRecipeDisplayModel {
    String id, title, desc, more,extra,type;

    public ArchiveRecipeDisplayModel(String id, String title, String desc, String more, String extra, String type) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.more = more;
        this.extra = extra;
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
