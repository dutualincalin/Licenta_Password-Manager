package org.PasswordManager.utility;

import java.text.SimpleDateFormat;

public class Utils {
    public static final int PBKDF2_HASH_ITERATIONS = 500;
    public static final int PBKDF2_HASH_SIZE = 256;

    public static final int ARGON2_HASH_ITERATIONS = 10;
    public static final int ARGON2_HASH_SIZE = 64;
    public static final int ARGON2_THREAD_NUM = 4;
    public static final int ARGON2_MEMORY = 1048576;

    public static final int QR_CODE_SIZE = 500;

    public static final String CONFIG_FILE_NAME = "config.txt";
    public static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("E dd.MM.yyyy HH:mm:ss.SSS");
    public static final SimpleDateFormat QR_DATE_FORMAT =
        new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS");

    public static final int QR_MAX_STORAGE = 4000;
}
