package org.PasswordManager.model;

import lombok.Getter;
import lombok.Setter;
import org.PasswordManager.utility.Utils;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PasswordConfiguration {
    private String id;
    private final String website;
    private String username;
    private int version;
    private int length;
    private Date creationDate;

    public PasswordConfiguration(String website, String username, int version, int length,
                                 Date creationDate) {
        this.id = UUID.randomUUID().toString();
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
        if (!(o instanceof PasswordConfiguration)) {
            return false;
        }

        PasswordConfiguration that = (PasswordConfiguration) o;

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
        return id + website + ((username != null) ? username : "") + version + length
            + Utils.DATE_FORMAT.format(creationDate);
    }
}
