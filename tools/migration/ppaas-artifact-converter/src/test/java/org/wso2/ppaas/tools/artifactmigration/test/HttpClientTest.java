/*
 * Copyright (c) 2005-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.ppaas.tools.artifactmigration.test;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.rest.endpoint.bean.autoscaler.partition.Partition;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wso2.ppaas.tools.artifactmigration.ArtifactConverterRestClient;
import org.wso2.ppaas.tools.artifactmigration.loader.Constants;
import org.wso2.ppaas.tools.artifactmigration.loader.OldArtifactLoader;

import java.io.File;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class HttpClientTest {
    public static final String ENDPOINT = System.getProperty("endpoint");
    public static final int BUFFER_SIZE = 32768;
    public static final int IDLE_TIMEOUT = 300000;
    public static final String SERVLET_CONTEXT_PATH = "/stratos400/test/api";
    private static final Log log = LogFactory.getLog(HttpClientTest.class);
    private static ArtifactConverterRestClient artifactConverterRestClient;
    private static Gson gson = new Gson();

    private static String getResourcesFolderPath() {
        String path = HttpClientTest.class.getResource("/").getPath();
        return StringUtils.removeEnd(path, File.separator);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        // Create Server
        Server server = new Server(Integer.getInteger("http.port"));
        ServletContextHandler context = new ServletContextHandler();
        ServletHolder defaultServ = new ServletHolder("default", StratosV400MockServelet.class);
        defaultServ.setInitParameter("resourceBase", System.getProperty("user.dir"));
        defaultServ.setInitParameter("dirAllowed", "true");
        defaultServ.setServlet(new StratosV400MockServelet());
        context.addServlet(defaultServ, SERVLET_CONTEXT_PATH + "/*");
        server.setHandler(context);

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(Integer.getInteger("https.port"));
        http_config.setOutputBufferSize(BUFFER_SIZE);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(Integer.getInteger("http.port"));
        http.setIdleTimeout(IDLE_TIMEOUT);

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(getResourcesFolderPath() + File.separator + "wso2carbon.jks");
        sslContextFactory.setKeyStorePassword("wso2carbon");
        sslContextFactory.setKeyManagerPassword("wso2carbon");

        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        ServerConnector https = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(https_config));
        https.setPort(Integer.getInteger("https.port"));
        https.setIdleTimeout(IDLE_TIMEOUT);

        // Set the connectors
        server.setConnectors(new Connector[] { http, https });

        // Start Server
        server.start();

        artifactConverterRestClient = new ArtifactConverterRestClient(System.getProperty("username"),
                System.getProperty("password"));
    }

    @Test(timeout = 60000)
    public void testArtifactConverterClient() throws Exception {
        artifactConverterRestClient.doGet(new URI(ENDPOINT + SERVLET_CONTEXT_PATH));
    }

    @Test(timeout = 60000)
    public void fetchPartitionListTest() throws Exception {
        Constants.BASE_URL = ENDPOINT + SERVLET_CONTEXT_PATH;
        List<Partition> partitionList = OldArtifactLoader.getInstance().fetchPartitionList();
        assertTrue(partitionList.size() > 1);
        assertTrue(partitionList.get(0).getId().equals("P1"));
    }
}
