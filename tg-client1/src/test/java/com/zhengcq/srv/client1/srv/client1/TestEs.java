package com.zhengcq.srv.client1.srv.client1;


import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@SpringBootTest
public class TestEs {

    @Test
    public void test1(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("47.105.143.13", 9200, "http")));

        GetRequest getPersonRequest = new GetRequest("item", "_doc", "0RgLZGsBOpgfgwi9hDCC");
        RequestOptions requestOptions;
        GetResponse getResponse = null;
        try {
            getResponse = client.get(getPersonRequest,RequestOptions.DEFAULT);

            System.out.println(getResponse.getSourceAsString());
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.queryStringQuery("牛奶"));
        searchRequest.source(sourceBuilder);


        try {
            SearchResponse searchResponse =  client.search(searchRequest,RequestOptions.DEFAULT);
            if(searchResponse.getHits().getTotalHits().value > 0) {
                for (SearchHit hit : searchResponse.getHits().getHits()) {
                    System.out.println(hit.getScore()+" --> "+hit.getSourceAsString());

                    Map mp = hit.getSourceAsMap();
                    String itemDesc = (String)mp.get("itemDesc");
                    System.out.println(itemDesc);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
