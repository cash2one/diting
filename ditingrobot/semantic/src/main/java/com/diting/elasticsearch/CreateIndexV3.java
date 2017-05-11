package com.diting.elasticsearch;

import com.diting.util.DBHelper;
import com.diting.model.Knowledge;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by liufei on 2016/6/14.
 */
public class CreateIndexV3 {

    private static Client client;
    private static final String INDEX_NAME = "diting";
    private static final String MYSELF_KNOWLEDGE = "tb_knowledge";

    private static final Object VOID = new Object();

    private int spiderThreads;

    private int monitorThreads;

    private int maxTableCount = 1000000;

    private int monitorThreshold;

    IndexRequestBuilder requestBuilder;

    private ExecutorService executorService;

    private ScheduledExecutorService monitorExecutorService;

    private List<Knowledge> allKnowledge;

    private BlockingQueue<Knowledge> knowledgeQueue;


    public CreateIndexV3() {
        client = ClientInit.getClient();
    }

    public static void main(String[] args) {
        try {
            CreateIndexV3 clientIndex = new CreateIndexV3();
            System.out.print("开始创建索引...");
            Long start = (new Date()).getTime();
            clientIndex.init();
            Long end = new Date().getTime() - start;
            System.out.println("消耗时间为：" + end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        // convert seconds to milliseconds
        monitorThreshold = 600 * 1000;

        //init
        //todo:To prevent interference
        spiderThreads = 40;
        monitorThreads = 10;
        knowledgeQueue = new ArrayBlockingQueue<>(maxTableCount);
        executorService = Executors.newFixedThreadPool(spiderThreads);
        monitorExecutorService = Executors.newScheduledThreadPool(monitorThreads);

        //init index name
        createIndex();
        requestBuilder  = createIndexLibrary();

        // init customer info
        loading();

        // start fetch and process threads
        plunder();
    }

    public void plunder() {
        for (int i = 0; i < spiderThreads; i++) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Knowledge myKnowledge = null;
                        try {
                            myKnowledge = fetch();
                            final Knowledge finalMyKnowledge = myKnowledge;
//                            executeWithTimeout(new Callable<Object>() {
//                                public Object call() throws Exception {
                                    process(finalMyKnowledge);
//                                    return VOID;
//                                }
//                            }, monitorThreshold);
                        } catch (Exception e) {
                        } finally {
                            if (myKnowledge != null) {
//                                enqueue(table);
                            }
                        }
                    }
                }
            });
        }

        executorService.shutdown();

//        while (!executorService.isTerminated()) {
//        }
    }


    private void process(Knowledge knowledge) {
        index(requestBuilder, knowledge);
        System.out.println("index id ["+knowledge.getId()+"]");
    }


    private <T> T executeWithTimeout(Callable<T> callable, Integer timeout)
            throws ExecutionException, InterruptedException {
        final Future<T> handler = monitorExecutorService.submit(callable);

        ScheduledFuture monitor = monitorExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                if (!handler.isDone()) {
//                    LOGGER.warn("The executing thread maybe hung, force kill it.");
                    handler.cancel(true);
                }
            }
        }, timeout, TimeUnit.MILLISECONDS);

        // blocking wait
        T result = handler.get();
        try {
//            LOGGER.debug("Cancel monitor thread.");
            monitor.cancel(true);
        } catch (Exception e) {
            //silently ignore
        }

        return result;
    }

    private void loading() {
        allKnowledge = getAllKnowledge();
        for (Knowledge knowledge : allKnowledge) {
            knowledgeQueue.add(knowledge);
        }
    }

    private Knowledge fetch() throws InterruptedException {
        return knowledgeQueue.take();
    }

    private void createIndex() {
        IndicesExistsResponse indicesExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(new String[]{CreateIndexV3.INDEX_NAME}))
                .actionGet();
        if (!indicesExistsResponse.isExists()) {
            client.admin().indices().create(new CreateIndexRequest(CreateIndexV3.INDEX_NAME))
                    .actionGet();
        }
    }

    /**
     * 转换成json对象
     *
     * @param knowledge
     * @return
     */
    private static String parseJson2Str(Knowledge knowledge) {
        String json = "";
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                    .startObject();
            contentBuilder.field("id", knowledge.getId() + "");
            contentBuilder.field("question", knowledge.getQuestion());
            contentBuilder.field("question_analyzed", knowledge.getQuestion());
            contentBuilder.field("answer", knowledge.getAnswer());
            contentBuilder.field("answer_analyzed", knowledge.getAnswer());
            contentBuilder.field("kw1", knowledge.getKw1().replace("$", ""));
            contentBuilder.field("kw2", knowledge.getKw2().replace("$", ""));
            contentBuilder.field("kw3", knowledge.getKw3().replace("$", ""));
            contentBuilder.field("kw4", knowledge.getKw4().replace("$", ""));
            contentBuilder.field("kw5", knowledge.getKw5().replace("$", ""));
            contentBuilder.field("cj", knowledge.getScene());
            contentBuilder.field("emotion", knowledge.getEmotion());
            json = contentBuilder.endObject().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private List<String> getCustomerNames() {
        List<String> customerNames = new ArrayList<>();
        try {
            String sql = "SELECT c_user_name from tb_customer";
            Connection conn = DBHelper.getConn();
            if (conn == null) {
                throw new Exception("数据库连接失败！");
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                customerNames.add(rs.getString("c_user_name"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return customerNames;
    }


    private String parseTableName(String userName) {
        String tableName;
        if (MYSELF_KNOWLEDGE.equals(userName)) {
            tableName = MYSELF_KNOWLEDGE;
        } else {
            StringBuffer bf = new StringBuffer();
            bf.append("tb_");
            bf.append(userName);
            bf.append("_knowledge");
            tableName = bf.toString();
        }
        return tableName;
    }

    private List<Knowledge> getAllKnowledge() {
        List<Knowledge> allKnowledge = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = DBHelper.getConn();
            if (conn == null) {
                throw new Exception("数据库连接失败！");
            }
            String sql = "SELECT question,answer,id,kw1,kw2,kw3,kw4,kw5,cj,emotion from " + CreateIndexV3.MYSELF_KNOWLEDGE;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Knowledge knowledge = new Knowledge();
                knowledge.setId(rs.getInt("id"));
                knowledge.setQuestion(rs.getString("question"));
                knowledge.setAnswer(rs.getString("answer"));
                knowledge.setKw1(rs.getString("kw1").replace("$", ""));
                knowledge.setKw2(rs.getString("kw2").replace("$", ""));
                knowledge.setKw3(rs.getString("kw3").replace("$", ""));
                knowledge.setKw4(rs.getString("kw4").replace("$", ""));
                knowledge.setKw5(rs.getString("kw5").replace("$", ""));
                knowledge.setEmotion(rs.getInt("emotion"));
                knowledge.setScene(rs.getString("scene"));
                allKnowledge.add(knowledge);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return allKnowledge;
    }

    private IndexRequestBuilder createIndexLibrary() {
        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
        IndexRequestBuilder requestBuilder = client.prepareIndex(CreateIndexV3.INDEX_NAME, CreateIndexV3.MYSELF_KNOWLEDGE).setRefresh(true);
        try {
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(CreateIndexV3.MYSELF_KNOWLEDGE)
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "integer")
                    .endObject()
                    .startObject("question")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("question_analyzed")
                    .field("type", "string")
                    .endObject()
                    .startObject("answer")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("answer_analyzed")
                    .field("type", "string")
                    .endObject()
                    .startObject("kw1")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw2")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw3")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw4")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw5")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("cj")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("emotion")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            //省略读取mapping文件的java代码，内容保存在mapping_json中。
            client.admin().indices().preparePutMapping(CreateIndexV3.INDEX_NAME).setType(CreateIndexV3.MYSELF_KNOWLEDGE).setSource(content).execute().actionGet();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return requestBuilder;
    }

    private void index(IndexRequestBuilder requestBuilder, Knowledge knowledge) {
        requestBuilder.setId(knowledge.getId().toString()).setSource(parseJson2Str(knowledge)).execute().actionGet();
    }

    private void createIndex(IndexRequestBuilder requestBuilder, List<Knowledge> knowledgeList) {
        System.out.println("start process type :" + requestBuilder.request().type());
        for (Knowledge knowledge : knowledgeList) {
            index(requestBuilder, knowledge);
        }
        System.out.println("end process type :" + requestBuilder.request().type());
    }

}
