package org.PasswordManager.utility;

import java.text.SimpleDateFormat;

public class Utils {
    public static final int PBKDF2_HASH_ITERATIONS = 500;
    public static final int PBKDF2_HASH_SIZE = 256;

    public static final int ARGON2_HASH_ITERATIONS = 10;
    public static final int ARGON2_HASH_SIZE = 64;

    public static final int SALT_SIZE = 0;
    public static final int THREAD_NUM = 4;
    public static final int MEMORY = 4096;

    public static final int QR_CODE_SIZE = 500;

    public static final String CONFIG_FILE_NAME = "config.txt";
    public static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("E dd.MM.yyyy HH:mm:ss.ns");
}
