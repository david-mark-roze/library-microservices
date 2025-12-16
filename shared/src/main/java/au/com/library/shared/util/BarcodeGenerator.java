package au.com.library.shared.util;

import java.util.UUID;

/**
 * Handles the generation of a barcode.
 */
public class BarcodeGenerator {

    private static final String PREFIX = "LIB-";
    private static final int MAX_NUM_LENGTH = 30;

    private BarcodeGenerator(){
    }

    /**
     * Generates the barcode.
     *
     * @return The generated barcode.
     */
    public static String generate(){
        return PREFIX + UUID.randomUUID().toString().substring(0, MAX_NUM_LENGTH).toUpperCase();
    }
}
