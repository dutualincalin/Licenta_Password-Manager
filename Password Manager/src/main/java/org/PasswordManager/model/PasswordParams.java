package org.PasswordManager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PasswordParams {
    private final String imgHash;
    private final String website;
    private String username;
    private int version;
    private int length;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordParams)) {
            return false;
        }

        PasswordParams that = (PasswordParams) o;

        if (getVersion() != that.getVersion()) {
            return false;
        }
        if (getLength() != that.getLength()) {
            return false;
        }
        if (!getImgHash().equals(that.getImgHash())) {
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
        int result = getImgHash().hashCode();
        result = 31 * result + getWebsite().hashCode();
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + getVersion();
        result = 31 * result + getLength();
        return result;
    }
}
