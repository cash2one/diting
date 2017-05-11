/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 SilkCloud and/or its affiliates. All rights reserved.
 */
package com.diting.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Constants.
 */
public class Constants {
    public static final Integer DEFAULT_OWNER_ID = 0;

    public static final String SYSTEM_INTERNAL = "SystemInternal";

    public static final Integer DEFAULT_BLACKLIST_DAYS = 21;

    public static final Integer SIGNIN_BLACKLIST_DAYS = 1;

    public static final Integer OFFER_BLACKLIST_DAYS = 8;

    public static final Integer ONBOARD_BLACKLIST_DAYS = 45;

    public static final Integer RESUME_BLACKLIST_DAYS = 180;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateFormat TIME_RANGE_FORMAT = new SimpleDateFormat("HH:mm");

    public static final DateFormat CALLRECORD_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final DateFormat TIME_MSEC_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final DateFormat TIME_APM_FORMAT = new SimpleDateFormat("HH:mm a");

    public static final String SYSTEM_ADMIN = "";

    public static final Boolean GLOBAL_RETRY_ENABLED = false;

    public static final Integer GLOBAL_RETRY_COUNT = 3;

    public static final String ROBOT_DATABASE_URL = "jdbc:mysql://rm-2zegmgvp9161777t6o.mysql.rds.aliyuncs.com:3306/dtrobot_test?useUnicode=true&characterEncoding=utf8";
    public static final String ROBOT_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    public static final String ROBOT_DATABASE_USER = "diting";
    public static final String ROBOT_DATABASE_PASSWORD = "diting";

    private Constants() {
        //private ctor
    }
}
