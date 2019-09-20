package com.oseasy.util.common.utils.velocity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.UUID;

public final class IDGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDGenerator.class);

    private static long suff = 1000000;

    private static int idLength = 18;

    /**
     * @return Long (contain 18 characters)
     */
    public static synchronized Long genId4Long() {
        suff += 1;
        if (suff > 9999999)
            suff = 1000000;
        String id = (String.valueOf(new Date().getTime()).substring(0, idLength - String.valueOf(suff).length())) + suff;
        return Long.valueOf(id);
    }

    /**
     * @return String (contain 18 characters)
     */
    public static synchronized String genId4Str() {
        suff += 1;
        if (suff > 9999999)
            suff = 1000000;
        return (String.valueOf(new Date().getTime()).substring(0, idLength - String.valueOf(suff).length())) + suff;
    }

    /**
     * @return String (contain 32 characters)
     */
    public static String genId() {
        try {
            UUID uuid = UUID.randomUUID();
            if (uuid != null) {
                String id = uuid.toString();
                while (id.contains("-"))
                    id = id.replaceAll("-", "");
                return id;
            }
        } catch (Exception e) {
            LOGGER.debug("生成ID产生错误", e);
        }
        return null;
    }



    public static void main(String[] args) {
        System.out.println(IDGenerator.genId());
        System.out.println();


    }
}
