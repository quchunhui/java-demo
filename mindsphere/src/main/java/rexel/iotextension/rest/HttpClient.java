package rexel.iotextension.rest;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import rexel.iotextension.ExtensionConst;

class HttpClient {
    static CloseableHttpClient getHttpClient() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(ExtensionConst.username, ExtensionConst.password));
        return HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
    }
}
