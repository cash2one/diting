package com.diting;

import com.diting.dao.mongo.ChatLogMapper;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.service.ChatLogMongoService;
import com.diting.util.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.diting.util.Utils.isEmpty;

/**
 * TestChatLog.
 */
public class TestChatLog extends BaseTest {

    static String sql = null;
    static DBHelper db1 = null;
    static ResultSet ret = null;

    @Autowired
    private ChatLogMongoService chatLogMongoService;

    @Autowired
    private ChatLogMapper chatLogMapper;

    @Test(enabled = false)
    public void testBVT() {
        try {
            String path = "d:\\log.txt";
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                ChatLogOptions options = new ChatLogOptions();
                options.setUserName("13510415408");

                List<ChatLog> logs = chatLogMongoService.getChatLogs(options);
                for (ChatLog log : logs) {
                    bw.write(log.getQuestion() + "/" + log.getAnswer() + "\r\n");
                    bw.flush();
                }
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void chatLogMongoSearchPage() {
        ChatLogOptions options = new ChatLogOptions();
        chatLogMongoService.searchForPage(options);
    }

    @Test(enabled = false)
    public void searchGroupForPage() {
        ChatLogOptions options = new ChatLogOptions();
        options.setUserName("13510415408");
        options.setPageNo(1);
        options.setPageSize(15);
        options.setReduceFunction("function(doc, prev){prev.count+=1,prev.question=doc.question,prev.answer=doc.answer,prev.username=doc.username}");
        chatLogMongoService.searchGroupForPage(options);
    }

    @Test(enabled = false)
    public void searchGroupForUuid() {
        chatLogMapper.searchQuestionAndAnswerNumber();
    }

    @Test(enabled = false)
    public void updateByusername() {
        chatLogMapper.updateByUsername("135521412710000", "13552141271");
    }

    @Test(enabled = false)
    public void createChatLog() {
        List<ChatLog> chatLogs = new ArrayList<>();
        sql = "select * from chatlog";//SQL语句
        db1 = new DBHelper(sql);//创建DBHelper对象

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                ChatLog chatLog = new ChatLog();
                String question = ret.getString("question");
                String answer = ret.getString("answer");
                String username = ret.getString("username");
                String app_key = ret.getString("app_key");
                String ip = ret.getString("ip");
                String extra1 = ret.getString("extra1");
                String extra4 = ret.getString("extra4");
                String createdTime = ret.getString("createdTime");
                String updatedTime = ret.getString("updatedTime");
                String uuid = ret.getString("uuid");

                chatLog.setUuid(uuid);
                chatLog.setQuestion(question);
                chatLog.setAnswer(answer);
                chatLog.setUsername(username);
                chatLog.setApp_key(app_key);
                chatLog.setIp(ip);
                chatLog.setExtra1(extra1);
                chatLog.setExtra4(extra4);
                chatLog.setDeleted(false);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (!isEmpty(createdTime) && sdf.parse(createdTime) != null) {
                    chatLog.setCreatedTime(sdf.parse(createdTime));
                } else {
                    chatLog.setCreatedTime(new Date());
                }
                if (!isEmpty(updatedTime) && sdf.parse(updatedTime) != null) {
                    chatLog.setUpdatedTime(sdf.parse(updatedTime));
                } else {
                    chatLog.setUpdatedTime(new Date());
                }
                chatLogs.add(chatLog);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        ChatLog chatLog = new ChatLog();
//        List<ChatLog> chatLogs=
        int i = 0;
        for (ChatLog chatLog : chatLogs) {
            i++;
            chatLogMongoService.create(chatLog);
            System.out.println(i);
        }
//        chatLogMongoService.create(chatLog);
    }

    private static int insert(ChatLog chatLog) {
        Connection conn = DBHelper.getConn();
        int i = 0;
        String sql = "insert into chatlog (question,answer,username,app_key,ip,extra1,extra4,createdTime,updatedTime,uuid) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, chatLog.getQuestion());
            pstmt.setString(2, chatLog.getAnswer());
            pstmt.setString(3, chatLog.getUsername());
            pstmt.setString(4, chatLog.getApp_key());
            pstmt.setString(5, chatLog.getIp());
            pstmt.setString(6, chatLog.getExtra1());
            pstmt.setString(7, chatLog.getExtra4());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pstmt.setString(8,  sf.format(chatLog.getCreatedTime()));
            pstmt.setString(9,  sf.format(chatLog.getUpdatedTime()));
            pstmt.setString(10, chatLog.getUuid());
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Test(enabled = false)
    public void searchAllChatlog() {
        List<ChatLog> chatLogs = chatLogMongoService.getAllChatlog();
        for (int i=0;i<chatLogs.size()-1;i++) {
            if (i<1395)
                continue;
            insert(chatLogs.get(i));
            System.out.println(i);
        }
    }
    @Test(enabled = false)
    public void searchChatLogByChargeMode(){
        chatLogMapper.searchChatLogByChargeMode("13552141271");
    }
}
