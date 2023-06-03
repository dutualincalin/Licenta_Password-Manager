package org.PasswordManager.model;

import lombok.Getter;
import lombok.Setter;
import org.PasswordManager.utility.Utils;

import java.util.Date;

@Getter
@Setter
public class PasswordMetadata {
    private final String website;
    private String username;
    private int version;
    private int length;
    private Date creationDate;

    public PasswordMetadata(String website, String username, int version, int length,
                            Date creationDate) {
        this.website = website;
        this.username = username;
        this.version = version;
        this.length = length;
        this.creationDate = (creationDate == null) ? new Date() : creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordMetadata)) {
            return false;
        }

        PasswordMetadata that = (PasswordMetadata) o;

        if (getVersion() != that.getVersion()) {
            return false;
        }

        if (!getWebsite().equals(that.getWebsite())) {
            return false;
        }

        return getUsername() != null ? getUsername().equals(that.getUsername()) :
            that.getUsername() == null;
    }

    @Override
    public int hashCode() {
        int result = getWebsite().hashCode();
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + getVersion();
        result = 31 * result + getLength();
        result = 31 * result + getCreationDate().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return website + ((username != null) ? username : "") + version + length
            + Utils.DATE_FORMAT.format(creationDate);
    }
}
