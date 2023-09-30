package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.elasticsearch.client.RestClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import javax.net.ssl.SSLContext;
import java.io.IOException;


public class ElasticsearchService {

    private final ElasticsearchClient client;
    public ElasticsearchService(){
        String hostname = System.getProperty("ELASTICSEARCH_HOSTNAME");
        int port = Integer.parseInt(System.getProperty("ELASTICSEARCH_PORT"));
        String scheme = System.getProperty("ELASTICSEARCH_SCHEME");
        String username = System.getProperty("ELASTICSEARCH_USERNAME");
        String password = System.getProperty("ELASTICSEARCH_PASSWORD");
        String fingerprint = System.getProperty("ELASTICSEARCH_CA_FINGERPRINT");

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username,password));

        SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(fingerprint);


        RestClient restClient = RestClient.builder(new HttpHost(hostname, port, scheme))
                .setHttpClientConfigCallback(
                        httpAsyncClientBuilder -> httpAsyncClientBuilder
                                .setSSLContext(sslContext)
                                .setDefaultCredentialsProvider(credentialsProvider)

                ).build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.client = new ElasticsearchClient(transport);
    }

    public void addPerson(Person person) throws IOException {
        IndexResponse response = client.index(i -> i
                .index("person")
                .id(person.getUuid().toString())
                .document(person)
                );

        if (response.result() != Result.Created){
            System.out.println("Addition failed at elasticsearch server");
        }
    }

    public void searchPersonFirstName(String firstname) throws IOException {
        SearchResponse<Person> response = client.search(s -> s
                .index("person").query(q -> q
                                .match(t -> t
                                .field("firstName")
                                .query(firstname)))
                , Person.class);

        for (Hit<Person> hit : response.hits().hits()){
            Person person = hit.source();
            if (person != null) System.out.println(person.getFullName());
        }

    }

    public void searchPersonLastName(String lastname) throws IOException {
        SearchResponse<Person> response = client.search(s -> s
                        .index("person").query(q -> q
                                .match(t -> t
                                        .field("lastName")
                                        .query(lastname)))
                , Person.class);

        for (Hit<Person> hit : response.hits().hits()){
            Person person = hit.source();
            if (person != null) System.out.println(person.getFullName());
        }

    }

}
