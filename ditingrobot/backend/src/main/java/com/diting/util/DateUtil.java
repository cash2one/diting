package com.diting.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/19.
 */
public class DateUtil {

    private final static String START_TIME = "2016-05-08 00:00:00";
    public final static String START_DATE = "2016-12-01 00:00:00";
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * <li>功能描述：得到开始时间
     *
     * @param startDate
     * @return String
     * @author Administrator
     */
    public static String getStartTime(String startDate) {
        String startTime;
        if (null == startDate || "".equals(startDate)) {
            startTime = START_TIME;
        } else {
            startTime = dateformatChange(startDate) + " 00:00:00";
        }
        return startTime;
    }

    /**
     * <li>功能描述：得到结束时间
     *
     * @param endDate
     * @return String
     * @author Administrator
     */
    public static String getEndTime(String endDate) {
        String endTime;
        if (null == endDate || "".equals(endDate)) {
            endTime = getTime();
        } else {
            endTime = dateformatChange(endDate) + " 24:00:00";
        }
        return endTime;
    }

    /**
     * <li>功能描述：将格式为MM/dd/yyyy改为yyyy-MM-dd
     *
     * @param dateBeforeChange
     * @return String
     * @author Administrator
     */
    public static String dateformatChange(String dateBeforeChange) {
        String[] date = dateBeforeChange.split("/");
        return date[2] + "-" + date[0] + "-" + date[1];
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    public static String getPercentage(double d) {
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();

        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);

        return nt.format(d);
    }

    public static int compare_date(String DATE1, String DATE2) {

        try {
            System.out.println(DATE1);
            Date dt1 = sdfTime.parse(DATE1);
            Date dt2 = sdfTime.parse(DATE2);
            int timeDifference= (int) ((dt1.getTime()-dt2.getTime())/(1000 * 60 * 60 * 24));
            if (timeDifference>0) {
                return 1;
            } else if (timeDifference<0) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
