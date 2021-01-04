package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 10/04/2019.
 */

public class ArchiveMainModel {
    private String id,creatorId;
    private String archiveName,archiveDesc,archiveLink,archiveDetails,archiveSig,archiveImage,archiveAuthor;

    public ArchiveMainModel() {
    }

    public ArchiveMainModel(String id, String creatorId, String archiveName, String archiveDesc, String archiveLink, String archiveDetails, String archiveSig, String archiveImage, String archiveAuthor) {
        this.id = id;
        this.creatorId = creatorId;
        this.archiveName = archiveName;
        this.archiveDesc = archiveDesc;
        this.archiveLink = archiveLink;
        this.archiveDetails = archiveDetails;
        this.archiveSig = archiveSig;
        this.archiveImage = archiveImage;
        this.archiveAuthor = archiveAuthor;
    }

    public String getArchiveAuthor() {
        return archiveAuthor;
    }

    public void setArchiveAuthor(String archiveAuthor) {
        this.archiveAuthor = archiveAuthor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public String getArchiveDesc() {
        return archiveDesc;
    }

    public void setArchiveDesc(String archiveDesc) {
        this.archiveDesc = archiveDesc;
    }

    public String getArchiveLink() {
        return archiveLink;
    }

    public void setArchiveLink(String archiveLink) {
        this.archiveLink = archiveLink;
    }

    public String getArchiveDetails() {
        return archiveDetails;
    }

    public void setArchiveDetails(String archiveDetails) {
        this.archiveDetails = archiveDetails;
    }

    public String getArchiveSig() {
        return archiveSig;
    }

    public void setArchiveSig(String archiveSig) {
        this.archiveSig = archiveSig;
    }

    public String getArchiveImage() {
        return archiveImage;
    }

    public void setArchiveImage(String archiveImage) {
        this.archiveImage = archiveImage;
    }
}
