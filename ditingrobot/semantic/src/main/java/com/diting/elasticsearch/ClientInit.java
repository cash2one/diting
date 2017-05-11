package com.diting.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;

/**
 * Created by sunhao on 2016/6/17.
 */
public class ClientInit {
    private static Client client;

    private static Settings settings = Settings.settingsBuilder()
            .put("cluster.name", "diting")
            .put("client.transport.sniff", true)
            .put("client.transport.ping_timeout", "100s")
            .build();

    static {
        try {
            client = TransportClient.builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("101.200.153.164"), 9300))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("101.201.55.38"), 9300));//正式ES服务器
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("60.205.59.176"), 9300));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized Client getClient() {
        return client;
    }

    public static synchronized void close() {
        client.close();
    }
}
