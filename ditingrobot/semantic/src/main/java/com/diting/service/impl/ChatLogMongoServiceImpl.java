package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.WeChatMapper;
import com.diting.dao.mongo.ChatLogMapper;
import com.diting.model.StatisticalData;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.options.WeChatAccountOptions;
import com.diting.model.result.Results;
import com.diting.model.views.ChatLogViews;
import com.diting.model.views.QuestionAndAnswerUserViews;
import com.diting.service.ChatLogMongoService;
import com.diting.service.mongo.BaseMongoServiceImpl;
import com.diting.util.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.diting.util.Constants.DATE_FORMAT;
import static com.diting.util.Utils.isEmpty;

@SuppressWarnings("ALL")
@Service("chatLogMongoService")
@Transactional
public class ChatLogMongoServiceImpl extends BaseMongoServiceImpl<ChatLog> implements ChatLogMongoService {
    @Autowired
    private ChatLogMapper chatLogMapper;
    @Autowired
    private WeChatMapper weChatMapper;

    @Override
    public Results<ChatLog> searchForPage(ChatLogOptions options) {
        return chatLogMapper.searchForPage(options);
    }

    @Override
    public List<ChatLog> getChatLogs(ChatLogOptions options) {
        return chatLogMapper.search(options);
    }

    @Override
    public Results<ChatLog> searchGroupForPage(ChatLogOptions options) {
        return chatLogMapper.searchGroupForPage(options);
    }

    @Override
    public List<ChatLog> getAllRanking() {
        return chatLogMapper.getAllRank();
    }

    @Override
    public List<ChatLog> getTopFiftyRanking() {
        return chatLogMapper.getTopFiftyRanking();
    }

    @Override
    public List<ChatLog> getYesterdayRanking() {
        return chatLogMapper.getYesterdayRank();
    }

    @Override
    public void updateByUsername(ChatLog chatLog) {
        chatLogMapper.updateByUsername(chatLog.getOldUsername(), chatLog.getNewUsername());
    }

    @Override
    public List<ChatLog> getAllChatlog() {
        return chatLogMapper.getAll();
    }

    @Override
    public int searchYesterdayVisitorsNum() {
        return chatLogMapper.searchYesterdayVisitorsNum();
    }

    @Override
    public int getYesterdayAskNum() {
        return chatLogMapper.getYesterdayAskNum();
    }

    @Override
    public int getYesterdayUnknownQuestionNum() {
        return chatLogMapper.getYesterdayUnknownQuestionNum();
    }


    @Override
    public StatisticalData searchAllQuestionCountByUsername(String username) {
        StatisticalData statisticalData = new StatisticalData();
        statisticalData.setQuestionNumber((int) chatLogMapper.searchAllQuestionCountByUsername(username));
        statisticalData.setNumberOfVisitors(chatLogMapper.searchAllCountGroupByUUIDWhereByUsername(username));
        return statisticalData;
    }

    @Override
    public int searchAllCountGroupByUUIDWhereByUsername(String username) {
        return chatLogMapper.searchAllCountGroupByUUIDWhereByUsername(username);
    }

    @Override
    public List<Map<String, Object>> searchAllCountGroupByUUIDWhereByUsernameAndTime(ChatLogOptions chatRecordOptions) {
        List<Map<String, Object>> results = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String type = chatRecordOptions.getType();
        String beginTime = chatRecordOptions.getStartTime();
        String endTime = chatRecordOptions.getEndTime();
        String username = Universe.current().getUserName();
        if (type != null && type != "" && type.trim().length() > 0) {
            if ("yesterday".equals(type)) {
                results = statisticalQuantityYesterday(username, 1);
            } else if ("week".equals(type)) {
                results = statisticalQuantityWeek(username, 7);
            } else if ("month".equals(type)) {
                results = statisticalQuantityMonth(username, 30);
            }
        } else if (beginTime != null && !"".equals(beginTime)) {
            if (endTime == null || "".equals(endTime)) {
                endTime = sdf.format(new Date());
            }
            try {
                int days = Utils.differentDays(sdf.parse(beginTime), sdf.parse(endTime));
                String time = endTime;
                for (int i = days; i >= 0; i--) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("beginTime", sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time), i))));
                    beginTime = sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time), i))) + " 00:00:00";
                    endTime = sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time), i))) + " 24:00:00";
                    map.put("guestCount", chatLogMapper.searchAllCountGroupByUUIDWhereByUsernameAndTime(username, simpleDateFormat.parse(beginTime), simpleDateFormat.parse(endTime)));
                    map.put("suestionStatisticsCount", chatLogMapper.searchAllCountByUsernameAndTime(username, simpleDateFormat.parse(beginTime), simpleDateFormat.parse(endTime)));
                    results.add(map);
                }
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> searchAllCountByUsernameAndTime(ChatLogOptions chatRecordOptions) {
        List<Map<String, Object>> results = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String type = chatRecordOptions.getType();
        String beginTime = chatRecordOptions.getStartTime();
        String endTime = chatRecordOptions.getEndTime();
        String username = Universe.current().getUserName();
        if (type != null && type != "" && type.trim().length() > 0) {
            if ("yesterday".equals(type)) {
                results = statisticalQuantityYesterday(username, 1);
            } else if ("week".equals(type)) {
                results = statisticalQuantityWeek(username, 7);
            } else if ("month".equals(type)) {
                results = statisticalQuantityMonth(username, 30);
            }
        } else if (beginTime != null && !"".equals(beginTime)) {
            if (endTime == null || "".equals(endTime)) {
                endTime = sdf.format(new Date());
            }
            try {
                int days = Utils.differentDays(sdf.parse(beginTime), sdf.parse(endTime));
                String time = endTime;
                for (int i = days; i >= 0; i--) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("beginTime", sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time), i))));
                    beginTime = sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time), i))) + " 00:00:00";
                    endTime = sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time), i))) + " 24:00:00";
                    map.put("count", chatLogMapper.searchAllCountByUsernameAndTime(username, simpleDateFormat.parse(beginTime), simpleDateFormat.parse(endTime)));
                    results.add(map);
                }
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return results;
    }

    @Override
    public List<ChatLogViews> searchAllCountByTime(String type) {
        List<ChatLog> chatLogs = new ArrayList<>();
        List<ChatLogViews> chatLogViewses=new ArrayList<>();
        if (type.equals("today")) {
            chatLogs = chatLogMapper.searchAllChatLogByCreatedTime();
            chatLogViewses=searchAllCountByYesterdayOrToday(chatLogs, 0);
        } else if (type.equals("yesterday")) {
            chatLogs = chatLogMapper.searchAllChatLogByCreatedTime(1);
            chatLogViewses=searchAllCountByYesterdayOrToday(chatLogs, 1);
        } else if (type.equals("week")) {
            chatLogs = chatLogMapper.searchAllChatLogByCreatedTime(7);
            chatLogViewses=searchAllCountByWeek(chatLogs, 7);
        } else if (type.equals("month")) {
            chatLogs = chatLogMapper.searchAllChatLogByCreatedTime(30);
            chatLogViewses=searchAllCountByMonth(chatLogs, 30);
        }
        return chatLogViewses;
    }

    @Override
    public List<QuestionAndAnswerUserViews> searchQuestionAndAnswerUserByTime(String type) {
        List<QuestionAndAnswerUserViews> chatLogs = new ArrayList<>();
        List<QuestionAndAnswerUserViews> chatLogViewses=new ArrayList<>();
        if (type.equals("today")) {
            BasicDBList basicDBListQuestion  = chatLogMapper.searchQuestionAndAnswerUserByTime(0);
            BasicDBList basicDBListAnswer  = chatLogMapper.searchQuestionAndAnswerUserByTime(0);
            chatLogViewses=searchQuestionAndAnswerUserNumForYesterdayOrtoday(basicDBListQuestion,basicDBListAnswer, 0);
        } else if (type.equals("yesterday")) {
            BasicDBList basicDBListQuestion  = chatLogMapper.searchQuestionAndAnswerUserByTime(1);
            BasicDBList basicDBListAnswer  = chatLogMapper.searchQuestionAndAnswerUserByTime(1);
            chatLogViewses=searchQuestionAndAnswerUserNumForYesterdayOrtoday(basicDBListQuestion,basicDBListAnswer, 1);
        } else if (type.equals("week")) {
            BasicDBList basicDBListQuestion  = chatLogMapper.searchQuestionAndAnswerUserByTime(7);
            BasicDBList basicDBListAnswer  = chatLogMapper.searchQuestionAndAnswerUserByTime(7);
            chatLogViewses=searchQuestionAndAnswerUserNumForWeek(basicDBListQuestion,basicDBListAnswer, 7);
        } else if (type.equals("month")) {
            BasicDBList basicDBListQuestion  = chatLogMapper.searchQuestionAndAnswerUserByTime(30);
            BasicDBList basicDBListAnswer  = chatLogMapper.searchQuestionAndAnswerUserByTime(30);
            chatLogViewses=searchQuestionAndAnswerUserNumForMonth(basicDBListQuestion,basicDBListAnswer, 30);
        }
        return chatLogViewses;
    }

    @Override
    public Map<String, Object> staWeChatChatLog() {
        Map<String, Object> map = new HashMap<>();
        Long weChatAllCount = chatLogMapper.searchCountByExtra4AndTime(null, "2", null, null);
        Long weChatValidAllCount = chatLogMapper.searchCountByExtra4AndTime("0", "2", null, null);
        Long weChatInvalidAllCount = chatLogMapper.searchCountByExtra4AndTime("1", "2", null, null);
        map.put("weChatAllCount", weChatAllCount);
        map.put("weChatValidAllCount", weChatValidAllCount);
        map.put("weChatInvalidAllCount", weChatInvalidAllCount);
        Date dateBegin = Utils.getDayStart(Utils.daysBefore(new Date(), 1));
        Date dateEnd = Utils.getDayStart(new Date());
        Long weChatDayCount = chatLogMapper.searchCountByExtra4AndTime(null, "2", dateBegin, dateEnd);
        Long weChatValidDayCount = chatLogMapper.searchCountByExtra4AndTime("0", "2", dateBegin, dateEnd);
        String dayRate = "0";
        String allRate = "0";
        DecimalFormat df = new DecimalFormat("0.000");
        if (weChatDayCount != null && weChatValidDayCount != null) {
            dayRate = Double.valueOf(df.format((float) weChatValidDayCount.intValue() / weChatDayCount.intValue())) * 100 + "%";
        }
        if (weChatAllCount != null && weChatValidAllCount != null) {
            allRate = Double.valueOf(df.format((float) weChatValidAllCount.intValue() / weChatAllCount.intValue())) * 100 + "%";
        }
        map.put("weChatDayRate", dayRate);
        map.put("weChatAllRate", allRate);
        map.put("weChatAccountCount", weChatMapper.searchAllCountByTime(new WeChatAccountOptions()));
        return map;
    }

    public List<Map<String, Object>> statisticalQuantity(String username, int days) {
        List<Map<String, Object>> results = new ArrayList<>();
        if (days == 1) {
            results = statisticalQuantityYesterday(username, days);
        } else if (days == 7) {
            results = statisticalQuantityWeek(username, days);
        }
        return results;
    }

    public List<Map<String, Object>> statisticalQuantityYesterday(String username, int days) {
        List<ChatLog> chatLogs = chatLogMapper.searchChatLogByCreatedTime(username, 1);
        BasicDBList basicDBList = chatLogMapper.searchGroupByUuidAndUsername(username, 1);
        List<Map<String, Object>> results = new ArrayList<>();
        Date begin = Utils.daysBefore(Utils.today(), 1);
        String strDate = DATE_FORMAT.format(begin);
        String endTime = null;
        int one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, guest1, guest2, guest3, guest4, guest5, guest6, guest7, guest8, guest9, guest10, guest11, guest12;
        one = two = three = four = five = six = seven = eight = nine = ten = eleven = twelve = guest1 = guest2 = guest3 = guest4 = guest5 = guest6 = guest7 = guest8 = guest9 = guest10 = guest11 = guest12 = 0;
        for (ChatLog chatLog : chatLogs) {
            Date date = chatLog.getCreatedTime();
            int n = (int) ((date.getTime() - begin.getTime()) / (2 * 60 * 60 * 1000));
            if (n == 0) {
                one += 1;
            } else if (n == 1) {
                two += 1;
            } else if (n == 2) {
                three += 1;
            } else if (n == 3) {
                four += 1;
            } else if (n == 4) {
                five += 1;
            } else if (n == 5) {
                six += 1;
            } else if (n == 6) {
                seven += 1;
            } else if (n == 7) {
                eight += 1;
            } else if (n == 8) {
                nine += 1;
            } else if (n == 9) {
                ten += 1;
            } else if (n == 10) {
                eleven += 1;
            } else if (n == 11) {
                twelve += 1;
            }
        }
        for (int n = 0; n < basicDBList.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBList.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (2 * 60 * 60 * 1000));
            if (m == 0) {
                guest1 += 1;
            } else if (m == 1) {
                guest2 += 1;
            } else if (m == 2) {
                guest3 += 1;
            } else if (m == 3) {
                guest4 += 1;
            } else if (m == 4) {
                guest5 += 1;
            } else if (m == 5) {
                guest6 += 1;
            } else if (m == 6) {
                guest7 += 1;
            } else if (n == 7) {
                guest8 += 1;
            } else if (m == 8) {
                guest9 += 1;
            } else if (m == 9) {
                guest10 += 1;
            } else if (m == 10) {
                guest11 += 1;
            } else if (m == 11) {
                guest12 += 1;
            }
        }
        for (int i = 0; i < 12; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest1);
                map.put("suestionStatisticsCount", one);
            } else if (i == 1) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest2);
                map.put("suestionStatisticsCount", two);
            } else if (i == 2) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest3);
                map.put("suestionStatisticsCount", three);
            } else if (i == 3) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest4);
                map.put("suestionStatisticsCount", four);
            } else if (i == 4) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest5);
                map.put("suestionStatisticsCount", five);
            } else if (i == 5) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest6);
                map.put("suestionStatisticsCount", six);
            } else if (i == 6) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest7);
                map.put("suestionStatisticsCount", seven);
            } else if (i == 7) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest8);
                map.put("suestionStatisticsCount", eight);
            } else if (i == 8) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest9);
                map.put("suestionStatisticsCount", nine);
            } else if (i == 9) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest10);
                map.put("suestionStatisticsCount", ten);
            } else if (i == 10) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest11);
                map.put("suestionStatisticsCount", eleven);
            } else if (i == 11) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                map.put("endTime", endTime);
                map.put("guestCount", guest12);
                map.put("suestionStatisticsCount", twelve);
            }
            results.add(map);
        }
        return results;
    }

    public List<Map<String, Object>> statisticalQuantityWeek(String username, int days) {
        List<ChatLog> chatLogs = chatLogMapper.searchChatLogByCreatedTime(username, 7);
        BasicDBList basicDBList = chatLogMapper.searchGroupByUuidAndUsername(username, 7);
        List<Map<String, Object>> results = new ArrayList<>();
        Date begin = Utils.daysBefore(Utils.today(), 7);
        String strDate = DATE_FORMAT.format(begin);
        String endTime = null;
        String beginTime = null;
        int one, two, three, four, five, six, seven, guest1, guest2, guest3, guest4, guest5, guest6, guest7;
        one = two = three = four = five = six = seven = guest1 = guest2 = guest3 = guest4 = guest5 = guest6 = guest7 = 0;
        for (ChatLog chatLog : chatLogs) {
            Date date = chatLog.getCreatedTime();
            int n = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (n == 0) {
                one += 1;
            } else if (n == 1) {
                two += 1;
            } else if (n == 2) {
                three += 1;
            } else if (n == 3) {
                four += 1;
            } else if (n == 4) {
                five += 1;
            } else if (n == 5) {
                six += 1;
            } else if (n == 6) {
                seven += 1;
            }
        }
        for (int n = 0; n < basicDBList.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBList.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (m == 0) {
                guest1 += 1;
            } else if (m == 1) {
                guest2 += 1;
            } else if (m == 2) {
                guest3 += 1;
            } else if (m == 3) {
                guest4 += 1;
            } else if (m == 4) {
                guest5 += 1;
            } else if (m == 5) {
                guest6 += 1;
            } else if (m == 6) {
                guest7 += 1;
            }
        }
        for (int i = 0; i < 7; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 7)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest1);
                map.put("suestionStatisticsCount", one);
            } else if (i == 1) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 6)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest2);
                map.put("suestionStatisticsCount", two);
            } else if (i == 2) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 5)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest3);
                map.put("suestionStatisticsCount", three);
            } else if (i == 3) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 4)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest4);
                map.put("suestionStatisticsCount", four);
            } else if (i == 4) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 3)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest5);
                map.put("suestionStatisticsCount", five);
            } else if (i == 5) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 2)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest6);
                map.put("suestionStatisticsCount", six);
            } else if (i == 6) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 1)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest7);
                map.put("suestionStatisticsCount", seven);
            }
            results.add(map);
        }
        return results;
    }

    public List<Map<String, Object>> statisticalQuantityMonth(String username, int days) {
        List<ChatLog> chatLogs = chatLogMapper.searchChatLogByCreatedTime(username, 30);
        BasicDBList basicDBList = chatLogMapper.searchGroupByUuidAndUsername(username, 30);
        List<Map<String, Object>> results = new ArrayList<>();
        Date begin = Utils.daysBefore(Utils.today(), 30);
        String strDate = DATE_FORMAT.format(begin);
        String endTime = null;
        String beginTime = null;
        int suestion1, suestion2, suestion3, suestion4, suestion5, suestion6, suestion7, suestion8, suestion9, suestion10, suestion11, suestion12, suestion13, suestion14, suestion15, suestion16, suestion17, suestion18, suestion19, suestion20, suestion21, suestion22, suestion23, suestion24, suestion25, suestion26, suestion27, suestion28, suestion29, suestion30, guest1, guest2, guest3, guest4, guest5, guest6, guest7, guest8, guest9, guest10, guest11, guest12, guest13, guest14, guest15, guest16, guest17, guest18, guest19, guest20, guest21, guest22, guest23, guest24, guest25, guest26, guest27, guest28, guest29, guest30;
        suestion1 = suestion2 = suestion3 = suestion4 = suestion5 = suestion6 = suestion7 = suestion8 = suestion9 = suestion10 = suestion11 = suestion12 = suestion13 = suestion14 = suestion15 = suestion16 = suestion17 = suestion18 = suestion19 = suestion20 = suestion21 = suestion22 = suestion23 = suestion24 = suestion25 = suestion26 = suestion27 = suestion28 = suestion29 = suestion30 =
                guest1 = guest2 = guest3 = guest4 = guest5 = guest6 = guest7 = guest8 = guest9 = guest10 = guest11 = guest12 = guest13 = guest14 = guest15 = guest16 = guest17 = guest18 = guest19 = guest20 = guest21 = guest22 = guest23 = guest24 = guest25 = guest26 = guest27 = guest28 = guest29 = guest30 = 0;
        for (ChatLog chatLog : chatLogs) {
            Date date = chatLog.getCreatedTime();
            int n = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (n == 0) {
                suestion1 += 1;
            } else if (n == 1) {
                suestion2 += 1;
            } else if (n == 2) {
                suestion3 += 1;
            } else if (n == 3) {
                suestion4 += 1;
            } else if (n == 4) {
                suestion5 += 1;
            } else if (n == 5) {
                suestion6 += 1;
            } else if (n == 6) {
                suestion7 += 1;
            } else if (n == 7) {
                suestion8 += 1;
            } else if (n == 8) {
                suestion9 += 1;
            } else if (n == 9) {
                suestion10 += 1;
            } else if (n == 10) {
                suestion10 += 1;
            } else if (n == 11) {
                suestion12 += 1;
            } else if (n == 12) {
                suestion13 += 1;
            } else if (n == 13) {
                suestion14 += 1;
            } else if (n == 14) {
                suestion15 += 1;
            } else if (n == 15) {
                suestion16 += 1;
            } else if (n == 16) {
                suestion17 += 1;
            } else if (n == 17) {
                suestion18 += 1;
            } else if (n == 18) {
                suestion19 += 1;
            } else if (n == 19) {
                suestion20 += 1;
            } else if (n == 20) {
                suestion21 += 1;
            } else if (n == 21) {
                suestion22 += 1;
            } else if (n == 22) {
                suestion23 += 1;
            } else if (n == 23) {
                suestion24 += 1;
            } else if (n == 24) {
                suestion25 += 1;
            } else if (n == 25) {
                suestion26 += 1;
            } else if (n == 26) {
                suestion27 += 1;
            } else if (n == 27) {
                suestion28 += 1;
            } else if (n == 28) {
                suestion29 += 1;
            } else if (n == 29) {
                suestion30 += 1;
            }
        }
        for (int n = 0; n < basicDBList.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBList.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (m == 0) {
                guest1 += 1;
            } else if (m == 1) {
                guest2 += 1;
            } else if (m == 2) {
                guest3 += 1;
            } else if (m == 3) {
                guest4 += 1;
            } else if (m == 4) {
                guest5 += 1;
            } else if (m == 5) {
                guest6 += 1;
            } else if (m == 6) {
                guest7 += 1;
            } else if (m == 7) {
                guest8 += 1;
            } else if (m == 8) {
                guest9 += 1;
            } else if (m == 9) {
                guest10 += 1;
            } else if (m == 10) {
                guest11 += 1;
            } else if (m == 11) {
                guest12 += 1;
            } else if (m == 12) {
                guest13 += 1;
            } else if (m == 13) {
                guest14 += 1;
            } else if (m == 14) {
                guest15 += 1;
            } else if (m == 15) {
                guest16 += 1;
            } else if (m == 16) {
                guest17 += 1;
            } else if (m == 17) {
                guest18 += 1;
            } else if (m == 18) {
                guest19 += 1;
            } else if (m == 19) {
                guest20 += 1;
            } else if (m == 20) {
                guest21 += 1;
            } else if (m == 21) {
                guest22 += 1;
            } else if (m == 22) {
                guest23 += 1;
            } else if (m == 23) {
                guest24 += 1;
            } else if (m == 24) {
                guest25 += 1;
            } else if (m == 25) {
                guest26 += 1;
            } else if (m == 26) {
                guest27 += 1;
            } else if (m == 27) {
                guest28 += 1;
            } else if (m == 28) {
                guest29 += 1;
            } else if (m == 29) {
                guest30 += 1;
            }
        }
        for (int i = 0; i < 30; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 30)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest1);
                map.put("suestionStatisticsCount", suestion1);
            } else if (i == 1) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 29)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest2);
                map.put("suestionStatisticsCount", suestion2);
            } else if (i == 2) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 28)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest3);
                map.put("suestionStatisticsCount", suestion3);
            } else if (i == 3) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 27)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest4);
                map.put("suestionStatisticsCount", suestion4);
            } else if (i == 4) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 26)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest5);
                map.put("suestionStatisticsCount", suestion5);
            } else if (i == 5) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 25)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest6);
                map.put("suestionStatisticsCount", suestion6);
            } else if (i == 6) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 24)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest7);
                map.put("suestionStatisticsCount", suestion7);
            } else if (i == 7) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 23)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest8);
                map.put("suestionStatisticsCount", suestion8);
            } else if (i == 8) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 22)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest9);
                map.put("suestionStatisticsCount", suestion9);
            } else if (i == 9) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 21)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest10);
                map.put("suestionStatisticsCount", suestion10);
            } else if (i == 10) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 20)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest11);
                map.put("suestionStatisticsCount", suestion11);
            } else if (i == 11) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 19)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest12);
                map.put("suestionStatisticsCount", suestion12);
            } else if (i == 12) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 18)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest13);
                map.put("suestionStatisticsCount", suestion13);
            } else if (i == 13) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 17)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest14);
                map.put("suestionStatisticsCount", suestion14);
            } else if (i == 14) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 16)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest15);
                map.put("suestionStatisticsCount", suestion15);
            } else if (i == 15) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 15)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest16);
                map.put("suestionStatisticsCount", suestion16);
            } else if (i == 16) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 14)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest17);
                map.put("suestionStatisticsCount", suestion17);
            } else if (i == 17) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 13)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest18);
                map.put("suestionStatisticsCount", suestion18);
            } else if (i == 18) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 12)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest19);
                map.put("suestionStatisticsCount", suestion19);
            } else if (i == 19) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 11)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest20);
                map.put("suestionStatisticsCount", suestion20);
            } else if (i == 20) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 10)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest21);
                map.put("suestionStatisticsCount", suestion21);
            } else if (i == 21) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 9)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest22);
                map.put("suestionStatisticsCount", suestion22);
            } else if (i == 22) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 8)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest23);
                map.put("suestionStatisticsCount", suestion23);
            } else if (i == 23) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 7)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest24);
                map.put("suestionStatisticsCount", suestion24);
            } else if (i == 24) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 6)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest25);
                map.put("suestionStatisticsCount", suestion25);
            } else if (i == 25) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 5)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest26);
                map.put("suestionStatisticsCount", suestion26);
            } else if (i == 26) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 4)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest27);
                map.put("suestionStatisticsCount", suestion27);
            } else if (i == 27) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 3)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest28);
                map.put("suestionStatisticsCount", suestion28);
            } else if (i == 28) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 2)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest29);
                map.put("suestionStatisticsCount", suestion29);
            } else if (i == 29) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 1)));
                map.put("beginTime", beginTime);
                map.put("guestCount", guest30);
                map.put("suestionStatisticsCount", suestion30);
            }
            results.add(map);
        }
        return results;
    }

    public List<ChatLogViews> searchAllCountByYesterdayOrToday(List<ChatLog> chatLogs, int type) {
        List<ChatLogViews> results = new ArrayList<>();
        Date begin = Utils.daysBefore(Utils.today(), 1);
        if (type == 0)
            begin = Utils.today();
        String strDate = DATE_FORMAT.format(begin);
        String endTime = null;
        int one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, effective1, effective2, effective3, effective4, effective5, effective6, effective7, effective8, effective9, effective10, effective11, effective12;
        one = two = three = four = five = six = seven = eight = nine = ten = eleven = twelve = effective1 = effective2 = effective3 = effective4 = effective5 = effective6 = effective7 = effective8 = effective9 = effective10 = effective11 = effective12 = 0;
        for (ChatLog chatLog : chatLogs) {
            Date date = chatLog.getCreatedTime();
            int n = (int) ((date.getTime() - begin.getTime()) / (2 * 60 * 60 * 1000));
            if (!isEmpty(chatLog.getExtra1()) && chatLog.getExtra1().equals("0")) {
                if (n == 0) {
                    one += 1;
                } else if (n == 1) {
                    two += 1;
                } else if (n == 2) {
                    three += 1;
                } else if (n == 3) {
                    four += 1;
                } else if (n == 4) {
                    five += 1;
                } else if (n == 5) {
                    six += 1;
                } else if (n == 6) {
                    seven += 1;
                } else if (n == 7) {
                    eight += 1;
                } else if (n == 8) {
                    nine += 1;
                } else if (n == 9) {
                    ten += 1;
                } else if (n == 10) {
                    eleven += 1;
                } else if (n == 11) {
                    twelve += 1;
                }
            } else {
                if (n == 0) {
                    effective1 += 1;
                } else if (n == 1) {
                    effective2 += 1;
                } else if (n == 2) {
                    effective3 += 1;
                } else if (n == 3) {
                    effective4 += 1;
                } else if (n == 4) {
                    effective5 += 1;
                } else if (n == 5) {
                    effective6 += 1;
                } else if (n == 6) {
                    effective7 += 1;
                } else if (n == 7) {
                    effective8 += 1;
                } else if (n == 8) {
                    effective9 += 1;
                } else if (n == 9) {
                    effective10 += 1;
                } else if (n == 10) {
                    effective11 += 1;
                } else if (n == 11) {
                    effective12 += 1;
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            ChatLogViews chatLogViews = new ChatLogViews();
            if (i == 0) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(one);
                chatLogViews.setInvalidQuestionNum(effective1);
            } else if (i == 1) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(two);
                chatLogViews.setInvalidQuestionNum(effective2);
            } else if (i == 2) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(three);
                chatLogViews.setInvalidQuestionNum(effective3);
            } else if (i == 3) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(four);
                chatLogViews.setInvalidQuestionNum(effective4);
            } else if (i == 4) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(five);
                chatLogViews.setInvalidQuestionNum(effective5);
            } else if (i == 5) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(six);
                chatLogViews.setInvalidQuestionNum(effective6);
            } else if (i == 6) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(seven);
                chatLogViews.setInvalidQuestionNum(effective7);
            } else if (i == 7) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(eight);
                chatLogViews.setInvalidQuestionNum(effective8);
            } else if (i == 8) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(nine);
                chatLogViews.setInvalidQuestionNum(effective9);
            } else if (i == 9) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(ten);
                chatLogViews.setInvalidQuestionNum(effective10);
            } else if (i == 10) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(eleven);
                chatLogViews.setInvalidQuestionNum(effective11);
            } else if (i == 11) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                chatLogViews.setTime(endTime);
                chatLogViews.setEffectiveQuestionNum(twelve);
                chatLogViews.setInvalidQuestionNum(effective12);
            }
            results.add(chatLogViews);
        }
        return results;
    }

    public List<ChatLogViews> searchAllCountByWeek(List<ChatLog> chatLogs, int type) {
        List<ChatLogViews> results = new ArrayList<>();
        Date begin = Utils.daysBefore(Utils.today(), type);
        String beginTime = null;
        int one, two, three, four, five, six, seven, effective1, effective2, effective3, effective4, effective5, effective6, effective7;
        one = two = three = four = five = six = seven = effective1 = effective2 = effective3 = effective4 = effective5 = effective6 = effective7 = 0;
        for (ChatLog chatLog : chatLogs) {
            Date date = chatLog.getCreatedTime();
            int n = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (!isEmpty(chatLog.getExtra1()) && chatLog.getExtra1().equals("0")) {
                if (n == 0) {
                    one += 1;
                } else if (n == 1) {
                    two += 1;
                } else if (n == 2) {
                    three += 1;
                } else if (n == 3) {
                    four += 1;
                } else if (n == 4) {
                    five += 1;
                } else if (n == 5) {
                    six += 1;
                } else if (n == 6) {
                    seven += 1;
                }
            } else {
                if (n == 0) {
                    effective1 += 1;
                } else if (n == 1) {
                    effective2 += 1;
                } else if (n == 2) {
                    effective3 += 1;
                } else if (n == 3) {
                    effective4 += 1;
                } else if (n == 4) {
                    effective5 += 1;
                } else if (n == 5) {
                    effective6 += 1;
                } else if (n == 6) {
                    effective7 += 1;
                }
            }

        }

        for (int i = 0; i < 7; i++) {
            ChatLogViews chatLogViews = new ChatLogViews();
            if (i == 0) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 7)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(one);
                chatLogViews.setInvalidQuestionNum(effective1);
            } else if (i == 1) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 6)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(two);
                chatLogViews.setInvalidQuestionNum(effective2);
            } else if (i == 2) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 5)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(three);
                chatLogViews.setInvalidQuestionNum(effective3);
            } else if (i == 3) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 4)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(four);
                chatLogViews.setInvalidQuestionNum(effective4);
            } else if (i == 4) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 3)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(five);
                chatLogViews.setInvalidQuestionNum(effective5);
            } else if (i == 5) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 2)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(six);
                chatLogViews.setInvalidQuestionNum(effective6);
            } else if (i == 6) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 1)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(seven);
                chatLogViews.setInvalidQuestionNum(effective7);
            }
            results.add(chatLogViews);
        }
        return results;
    }

    public List<ChatLogViews> searchAllCountByMonth(List<ChatLog> chatLogs, int type) {
        List<ChatLogViews> results = new ArrayList<>();
        Date begin = Utils.daysBefore(Utils.today(), type);
        String beginTime = null;
        int invalid1, invalid2, invalid3, invalid4, invalid5, invalid6, invalid7, invalid8, invalid9, invalid10, invalid11, invalid12, invalid13, invalid14, invalid15, invalid16, invalid17, invalid18, invalid19, invalid20, invalid21, invalid22, invalid23, invalid24, invalid25, invalid26, invalid27, invalid28, invalid29, invalid30,
                effective1, effective2, effective3, effective4, effective5, effective6, effective7, effective8, effective9, effective10, effective11, effective12, effective13, effective14, effective15, effective16, effective17, effective18, effective19, effective20, effective21, effective22, effective23, effective24, effective25, effective26, effective27, effective28, effective29, effective30;
        invalid1 = invalid2 = invalid3 = invalid4 = invalid5 = invalid6 = invalid7 = invalid8 = invalid9 = invalid10 = invalid11 = invalid12 = invalid13 = invalid14 = invalid15 =
                invalid16 = invalid17 = invalid18 = invalid19 = invalid20 = invalid21 = invalid22 = invalid23 = invalid24 = invalid25 = invalid26 = invalid27 = invalid28 =
                        invalid29 = invalid30 = effective1 = effective2 = effective3 = effective4 = effective5 = effective6 = effective7 = effective8 = effective9 = effective10 =
                                effective11 = effective12 = effective13 = effective14 = effective15 = effective16 = effective17 = effective18 = effective19 = effective20 =
                                        effective21 = effective22 = effective23 = effective24 = effective25 = effective26 = effective27 = effective28 = effective29 = effective30 = 0;
        for (ChatLog chatLog : chatLogs) {
            Date date = chatLog.getCreatedTime();
            int n = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (!isEmpty(chatLog.getExtra1()) && chatLog.getExtra1().equals("0")) {
                if (n == 0) {
                    effective1 += 1;
                } else if (n == 1) {
                    effective2 += 1;
                } else if (n == 2) {
                    effective3 += 1;
                } else if (n == 3) {
                    effective4 += 1;
                } else if (n == 4) {
                    effective5 += 1;
                } else if (n == 5) {
                    effective6 += 1;
                } else if (n == 6) {
                    effective7 += 1;
                } else if (n == 7) {
                    effective8 += 1;
                } else if (n == 8) {
                    effective9 += 1;
                } else if (n == 9) {
                    effective10 += 1;
                } else if (n == 10) {
                    effective11 += 1;
                } else if (n == 11) {
                    effective12 += 1;
                } else if (n == 12) {
                    effective13 += 1;
                } else if (n == 13) {
                    effective14 += 1;
                } else if (n == 14) {
                    effective15 += 1;
                } else if (n == 15) {
                    effective16 += 1;
                } else if (n == 16) {
                    effective17 += 1;
                } else if (n == 17) {
                    effective18 += 1;
                } else if (n == 18) {
                    effective19 += 1;
                } else if (n == 19) {
                    effective20 += 1;
                } else if (n == 20) {
                    effective1 += 1;
                } else if (n == 21) {
                    effective22 += 1;
                } else if (n == 22) {
                    effective23 += 1;
                } else if (n == 23) {
                    effective24 += 1;
                } else if (n == 24) {
                    effective5 += 1;
                } else if (n == 25) {
                    effective26 += 1;
                } else if (n == 26) {
                    effective27 += 1;
                } else if (n == 27) {
                    effective28 += 1;
                } else if (n == 28) {
                    effective29 += 1;
                } else if (n == 29) {
                    effective30 += 1;
                }
            } else {
                if (n == 0) {
                    invalid1 += 1;
                } else if (n == 1) {
                    invalid2 += 1;
                } else if (n == 2) {
                    invalid3 += 1;
                } else if (n == 3) {
                    invalid4 += 1;
                } else if (n == 4) {
                    invalid5 += 1;
                } else if (n == 5) {
                    invalid6 += 1;
                } else if (n == 6) {
                    invalid7 += 1;
                } else if (n == 7) {
                    invalid8 += 1;
                } else if (n == 8) {
                    invalid9 += 1;
                } else if (n == 9) {
                    invalid10 += 1;
                } else if (n == 10) {
                    invalid11 += 1;
                } else if (n == 11) {
                    invalid12 += 1;
                } else if (n == 12) {
                    invalid13 += 1;
                } else if (n == 13) {
                    invalid14 += 1;
                } else if (n == 14) {
                    invalid15 += 1;
                } else if (n == 15) {
                    invalid16 += 1;
                } else if (n == 16) {
                    invalid17 += 1;
                } else if (n == 17) {
                    invalid18 += 1;
                } else if (n == 18) {
                    invalid19 += 1;
                } else if (n == 19) {
                    invalid20 += 1;
                } else if (n == 20) {
                    invalid1 += 1;
                } else if (n == 21) {
                    invalid22 += 1;
                } else if (n == 22) {
                    invalid23 += 1;
                } else if (n == 23) {
                    invalid24 += 1;
                } else if (n == 24) {
                    invalid5 += 1;
                } else if (n == 25) {
                    invalid26 += 1;
                } else if (n == 26) {
                    invalid27 += 1;
                } else if (n == 27) {
                    invalid28 += 1;
                } else if (n == 28) {
                    invalid29 += 1;
                } else if (n == 29) {
                    invalid30 += 1;
                }
            }

        }

        for (int i = 0; i < 30; i++) {
            ChatLogViews chatLogViews = new ChatLogViews();
            if (i == 0) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 30)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective1);
                chatLogViews.setInvalidQuestionNum(invalid1);
            } else if (i == 1) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 29)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective2);
                chatLogViews.setInvalidQuestionNum(invalid2);
            } else if (i == 2) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 28)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective3);
                chatLogViews.setInvalidQuestionNum(invalid3);
            } else if (i == 3) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 27)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective4);
                chatLogViews.setInvalidQuestionNum(invalid4);
            } else if (i == 4) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 26)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective5);
                chatLogViews.setInvalidQuestionNum(invalid5);
            } else if (i == 5) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 25)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective6);
                chatLogViews.setInvalidQuestionNum(invalid6);
            } else if (i == 6) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 24)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective7);
                chatLogViews.setInvalidQuestionNum(invalid7);
            } else if (i == 7) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 23)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective8);
                chatLogViews.setInvalidQuestionNum(invalid8);
            } else if (i == 8) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 22)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective9);
                chatLogViews.setInvalidQuestionNum(invalid9);
            } else if (i == 9) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 21)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective10);
                chatLogViews.setInvalidQuestionNum(invalid10);
            } else if (i == 10) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 20)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective11);
                chatLogViews.setInvalidQuestionNum(invalid11);
            } else if (i == 11) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 19)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective12);
                chatLogViews.setInvalidQuestionNum(invalid12);
            } else if (i == 12) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 18)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective13);
                chatLogViews.setInvalidQuestionNum(invalid13);
            } else if (i == 13) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 17)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective14);
                chatLogViews.setInvalidQuestionNum(invalid14);
            } else if (i == 14) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 16)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective15);
                chatLogViews.setInvalidQuestionNum(invalid15);
            } else if (i == 15) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 15)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective16);
                chatLogViews.setInvalidQuestionNum(invalid16);
            } else if (i == 16) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 14)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective17);
                chatLogViews.setInvalidQuestionNum(invalid17);
            } else if (i == 17) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 13)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective18);
                chatLogViews.setInvalidQuestionNum(invalid18);
            } else if (i == 18) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 12)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective19);
                chatLogViews.setInvalidQuestionNum(invalid19);
            } else if (i == 19) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 11)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective20);
                chatLogViews.setInvalidQuestionNum(invalid20);
            } else if (i == 20) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 10)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective21);
                chatLogViews.setInvalidQuestionNum(invalid21);
            } else if (i == 21) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 9)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective22);
                chatLogViews.setInvalidQuestionNum(invalid22);
            } else if (i == 22) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 8)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective23);
                chatLogViews.setInvalidQuestionNum(invalid23);
            } else if (i == 23) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 7)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective24);
                chatLogViews.setInvalidQuestionNum(invalid24);
            } else if (i == 24) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 6)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective25);
                chatLogViews.setInvalidQuestionNum(invalid25);
            } else if (i == 25) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 5)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective26);
                chatLogViews.setInvalidQuestionNum(invalid26);
            } else if (i == 26) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 4)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective27);
                chatLogViews.setInvalidQuestionNum(invalid27);
            } else if (i == 27) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 3)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective28);
                chatLogViews.setInvalidQuestionNum(invalid28);
            } else if (i == 28) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 2)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective29);
                chatLogViews.setInvalidQuestionNum(invalid29);
            } else if (i == 29) {
                beginTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 1)));
                chatLogViews.setTime(beginTime);
                chatLogViews.setEffectiveQuestionNum(effective30);
                chatLogViews.setInvalidQuestionNum(invalid30);
            }
            results.add(chatLogViews);
        }
        return results;
    }

    public List<QuestionAndAnswerUserViews> searchQuestionAndAnswerUserNumForYesterdayOrtoday(BasicDBList basicDBListQuestion,BasicDBList basicDBListAnswer,int type){
        int  q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, q11, q12, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12;
        q1 = q2 = q3 = q4 = q5 = q6 = q7 = q8 = q9 = q10 = q11 = q12 =a1 = a2 = a3 = a4 = a5 = a6 = a7 = a8 = a9 = a10 = a11 = a12= 0;
        Date begin = Utils.daysBefore(Utils.today(), type);
        String strDate = DATE_FORMAT.format(begin);
        String endTime = null;
        List<QuestionAndAnswerUserViews> questionAndAnswerUserViewsList=new ArrayList<>();
        for (int n = 0; n < basicDBListQuestion.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBListQuestion.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (2 * 60 * 60 * 1000));
            if (m == 0) {
                q1 += 1;
            } else if (m == 1) {
                q2 += 1;
            } else if (m == 2) {
                q3 += 1;
            } else if (m == 3) {
                q4 += 1;
            } else if (m == 4) {
                q5 += 1;
            } else if (m == 5) {
                q6 += 1;
            } else if (m == 6) {
                q7 += 1;
            } else if (n == 7) {
                q8 += 1;
            } else if (m == 8) {
                q9 += 1;
            } else if (m == 9) {
                q10 += 1;
            } else if (m == 10) {
                q11 += 1;
            } else if (m == 11) {
                q12 += 1;
            }
        }
        for (int n = 0; n < basicDBListAnswer.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBListAnswer.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (2 * 60 * 60 * 1000));
            if (m == 0) {
                a1 += 1;
            } else if (m == 1) {
                a2 += 1;
            } else if (m == 2) {
                a3 += 1;
            } else if (m == 3) {
                a4 += 1;
            } else if (m == 4) {
                a5 += 1;
            } else if (m == 5) {
                a6 += 1;
            } else if (m == 6) {
                a7 += 1;
            } else if (n == 7) {
                a8 += 1;
            } else if (m == 8) {
                a9 += 1;
            } else if (m == 9) {
                a10 += 1;
            } else if (m == 10) {
                a11 += 1;
            } else if (m == 11) {
                a12 += 1;
            }
        }
        for (int i = 0; i < 12; i++) {
            QuestionAndAnswerUserViews questionAndAnswerUserViews=new QuestionAndAnswerUserViews();
            if (i == 0) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q1);
                questionAndAnswerUserViews.setAnswerUserNum(a1);
            } else if (i == 1) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q2);
                questionAndAnswerUserViews.setAnswerUserNum(a2);
            } else if (i == 2) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q3);
                questionAndAnswerUserViews.setAnswerUserNum(a3);
            } else if (i == 3) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q4);
                questionAndAnswerUserViews.setAnswerUserNum(a4);
            } else if (i == 4) {
                endTime = strDate + " 0" + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q5);
                questionAndAnswerUserViews.setAnswerUserNum(a5);
            } else if (i == 5) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q6);
                questionAndAnswerUserViews.setAnswerUserNum(a6);
            } else if (i == 6) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q7);
                questionAndAnswerUserViews.setAnswerUserNum(a7);
            } else if (i == 7) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q8);
                questionAndAnswerUserViews.setAnswerUserNum(a8);
            } else if (i == 8) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q9);
                questionAndAnswerUserViews.setAnswerUserNum(a9);
            } else if (i == 9) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q10);
                questionAndAnswerUserViews.setAnswerUserNum(a10);
            } else if (i == 10) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q11);
                questionAndAnswerUserViews.setAnswerUserNum(a11);
            } else if (i == 11) {
                endTime = strDate + " " + ((i + 1) * 2) + ":00:00";
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q12);
                questionAndAnswerUserViews.setAnswerUserNum(a12);
            }
            questionAndAnswerUserViewsList.add(questionAndAnswerUserViews);
        }
        return questionAndAnswerUserViewsList;
    }
    public List<QuestionAndAnswerUserViews> searchQuestionAndAnswerUserNumForWeek(BasicDBList basicDBListQuestion,BasicDBList basicDBListAnswer,int type){
        int  q1, q2, q3, q4, q5, q6, q7,  a1, a2, a3, a4, a5, a6, a7;
        q1 = q2 = q3 = q4 = q5 = q6 = q7 =a1 = a2 = a3 = a4 = a5 = a6 = a7 = 0;
        Date begin = Utils.daysBefore(Utils.today(), type);
        String endTime = null;
        List<QuestionAndAnswerUserViews> questionAndAnswerUserViewsList=new ArrayList<>();
        for (int n = 0; n < basicDBListQuestion.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBListQuestion.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (m == 0) {
                q1 += 1;
            } else if (m == 1) {
                q2 += 1;
            } else if (m == 2) {
                q3 += 1;
            } else if (m == 3) {
                q4 += 1;
            } else if (m == 4) {
                q5 += 1;
            } else if (m == 5) {
                q6 += 1;
            } else if (m == 6) {
                q7 += 1;
            }
        }
        for (int n = 0; n < basicDBListAnswer.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBListAnswer.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (m == 0) {
                a1 += 1;
            } else if (m == 1) {
                a2 += 1;
            } else if (m == 2) {
                a3 += 1;
            } else if (m == 3) {
                a4 += 1;
            } else if (m == 4) {
                a5 += 1;
            } else if (m == 5) {
                a6 += 1;
            } else if (m == 6) {
                a7 += 1;
            }
        }
        for (int i = 0; i < 7; i++) {
            QuestionAndAnswerUserViews questionAndAnswerUserViews=new QuestionAndAnswerUserViews();
            if (i == 0) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 7)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q1);
                questionAndAnswerUserViews.setAnswerUserNum(a1);
            } else if (i == 1) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 6)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q2);
                questionAndAnswerUserViews.setAnswerUserNum(a2);
            } else if (i == 2) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 5)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q3);
                questionAndAnswerUserViews.setAnswerUserNum(a3);
            } else if (i == 3) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 4)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q4);
                questionAndAnswerUserViews.setAnswerUserNum(a4);
            } else if (i == 4) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 3)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q5);
                questionAndAnswerUserViews.setAnswerUserNum(a5);
            } else if (i == 5) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 2)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q6);
                questionAndAnswerUserViews.setAnswerUserNum(a6);
            } else if (i == 6) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 1)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q7);
                questionAndAnswerUserViews.setAnswerUserNum(a7);
            }
            questionAndAnswerUserViewsList.add(questionAndAnswerUserViews);
        }
        return questionAndAnswerUserViewsList;
    }
    public List<QuestionAndAnswerUserViews> searchQuestionAndAnswerUserNumForMonth(BasicDBList basicDBListQuestion,BasicDBList basicDBListAnswer,int type){
        int q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, q11, q12, q13, q14, q15, q16, q17, q18, q19, q20, q21, q22, q23, q24, q25, q26, q27, q28, q29, q30 ,
        a1, a2, a3, a4, a5, a6, a7,a8,a9,a10, a11, a12, a13, a14, a15, a16, a17,a18,a19,a20, a21, a22, a23, a24, a25, a26, a27,a28,a29,a30;
        q1=q2=q3=q4=q5=q6=q7=q8=q9=q10=q11=q12=q13=q14=q15=q16=q17=q18=q19=q20=q21=q22=q23=q24=q25=q26=q27=q28=q29=q30 =a1=a2=a3=a4=a5=a6=a7=a8=a9=a10=a11=a12=a13=a14=a15=a16=a17=a18=a19=a20=a21=a22=a23=a24=a25=a26=a27=a28=a29=a30 = 0;
        Date begin = Utils.daysBefore(Utils.today(), type);
        String endTime = null;
        List<QuestionAndAnswerUserViews> questionAndAnswerUserViewsList=new ArrayList<>();
        for (int n = 0; n < basicDBListQuestion.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBListQuestion.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (m == 0) {
                q1 += 1;
            } else if (m == 1) {
                q2 += 1;
            } else if (m == 2) {
                q3 += 1;
            } else if (m == 3) {
                q4 += 1;
            } else if (m == 4) {
                q5 += 1;
            } else if (m == 5) {
                q6 += 1;
            } else if (m == 6) {
                q7 += 1;
            }else if (m == 7) {
                q8 += 1;
            } else if (n == 8) {
                q9 += 1;
            } else if (n == 9) {
                q10 += 1;
            } else if (n == 10) {
                q11 += 1;
            } else if (n == 11) {
                q12 += 1;
            } else if (n == 12) {
                q13 += 1;
            } else if (n == 13) {
                q14 += 1;
            } else if (n == 14) {
                q15 += 1;
            } else if (n == 15) {
                q16 += 1;
            } else if (n == 16) {
                q17 += 1;
            } else if (n == 17) {
                q18 += 1;
            } else if (n == 18) {
                q19 += 1;
            } else if (n == 19) {
                q20 += 1;
            } else if (n == 20) {
                q1 += 1;
            } else if (n == 21) {
                q22 += 1;
            } else if (n == 22) {
                q23 += 1;
            } else if (n == 23) {
                q24 += 1;
            } else if (n == 24) {
                q5 += 1;
            } else if (n == 25) {
                q26 += 1;
            } else if (n == 26) {
                q27 += 1;
            } else if (n == 27) {
                q28 += 1;
            } else if (n == 28) {
                q29 += 1;
            } else if (n == 29) {
                q30 += 1;
            }
        }
        for (int n = 0; n < basicDBListAnswer.size(); n++) {
            BasicDBObject object = (BasicDBObject) basicDBListAnswer.get(n);
            Date date = object.getDate("createdTime");
            int m = (int) ((date.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
            if (m == 0) {
                a1 += 1;
            } else if (m == 1) {
                a2 += 1;
            } else if (m == 2) {
                a3 += 1;
            } else if (m == 3) {
                a4 += 1;
            } else if (m == 4) {
                a5 += 1;
            } else if (m == 5) {
                a6 += 1;
            } else if (m == 6) {
                a7 += 1;
            }else if (m == 7) {
                a8 += 1;
            } else if (n == 8) {
                a9 += 1;
            } else if (n == 9) {
                a10 += 1;
            } else if (n == 10) {
                a11 += 1;
            } else if (n == 11) {
                a12 += 1;
            } else if (n == 12) {
                a13 += 1;
            } else if (n == 13) {
                a14 += 1;
            } else if (n == 14) {
                a15 += 1;
            } else if (n == 15) {
                a16 += 1;
            } else if (n == 16) {
                a17 += 1;
            } else if (n == 17) {
                a18 += 1;
            } else if (n == 18) {
                a19 += 1;
            } else if (n == 19) {
                a20 += 1;
            } else if (n == 20) {
                a1 += 1;
            } else if (n == 21) {
                a22 += 1;
            } else if (n == 22) {
                a23 += 1;
            } else if (n == 23) {
                a24 += 1;
            } else if (n == 24) {
                a5 += 1;
            } else if (n == 25) {
                a26 += 1;
            } else if (n == 26) {
                a27 += 1;
            } else if (n == 27) {
                a28 += 1;
            } else if (n == 28) {
                a29 += 1;
            } else if (n == 29) {
                a30 += 1;
            }
        }
        for (int i = 0; i <30; i++) {
            QuestionAndAnswerUserViews questionAndAnswerUserViews=new QuestionAndAnswerUserViews();
            if (i == 0) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 30)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q1);
                questionAndAnswerUserViews.setAnswerUserNum(a1);
            } else if (i == 1) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 29)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q2);
                questionAndAnswerUserViews.setAnswerUserNum(a2);
            } else if (i == 2) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 28)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q3);
                questionAndAnswerUserViews.setAnswerUserNum(a3);
            } else if (i == 3) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 27)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q4);
                questionAndAnswerUserViews.setAnswerUserNum(a4);
            } else if (i == 4) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 26)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q5);
                questionAndAnswerUserViews.setAnswerUserNum(a5);
            } else if (i == 5) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 25)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q6);
                questionAndAnswerUserViews.setAnswerUserNum(a6);
            } else if (i == 6) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 24)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q7);
                questionAndAnswerUserViews.setAnswerUserNum(a7);
            }else if (i == 7) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 23)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q8);
                questionAndAnswerUserViews.setAnswerUserNum(a8);
            }else if (i == 8) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 22)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q9);
                questionAndAnswerUserViews.setAnswerUserNum(a9);
            }else if (i == 9) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 21)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q10);
                questionAndAnswerUserViews.setAnswerUserNum(a10);
            }else if (i == 10) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 20)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q11);
                questionAndAnswerUserViews.setAnswerUserNum(a11);
            } else if (i == 11) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 19)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q12);
                questionAndAnswerUserViews.setAnswerUserNum(a12);
            } else if (i == 12) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 18)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q13);
                questionAndAnswerUserViews.setAnswerUserNum(a13);
            } else if (i == 13) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 17)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q14);
                questionAndAnswerUserViews.setAnswerUserNum(a14);
            } else if (i == 14) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 16)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q15);
                questionAndAnswerUserViews.setAnswerUserNum(a15);
            } else if (i == 15) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 15)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q16);
                questionAndAnswerUserViews.setAnswerUserNum(a16);
            } else if (i == 16) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 14)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q7);
                questionAndAnswerUserViews.setAnswerUserNum(a7);
            }else if (i == 17) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),13)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q18);
                questionAndAnswerUserViews.setAnswerUserNum(a18);
            }else if (i == 18) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 12)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q19);
                questionAndAnswerUserViews.setAnswerUserNum(a19);
            }else if (i == 19) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 11)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q20);
                questionAndAnswerUserViews.setAnswerUserNum(a20);
            }else if (i == 20) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 10)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q21);
                questionAndAnswerUserViews.setAnswerUserNum(a21);
            } else if (i == 21) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 9)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q22);
                questionAndAnswerUserViews.setAnswerUserNum(a22);
            } else if (i == 22) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 8)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q23);
                questionAndAnswerUserViews.setAnswerUserNum(a23);
            } else if (i == 23) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 7)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q24);
                questionAndAnswerUserViews.setAnswerUserNum(a24);
            } else if (i == 24) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 6)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q25);
                questionAndAnswerUserViews.setAnswerUserNum(a25);
            } else if (i == 25) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 5)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q26);
                questionAndAnswerUserViews.setAnswerUserNum(a26);
            } else if (i == 26) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 4)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q27);
                questionAndAnswerUserViews.setAnswerUserNum(a27);
            }else if (i == 27) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 3)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q28);
                questionAndAnswerUserViews.setAnswerUserNum(a28);
            }else if (i == 28) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 2)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q29);
                questionAndAnswerUserViews.setAnswerUserNum(a29);
            }else if (i == 29) {
                endTime = DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(), 1)));
                questionAndAnswerUserViews.setTime(endTime);
                questionAndAnswerUserViews.setQuestionUserNum( q30);
                questionAndAnswerUserViews.setAnswerUserNum(a30);
            }
            questionAndAnswerUserViewsList.add(questionAndAnswerUserViews);
        }
        return questionAndAnswerUserViewsList;
    }
}
