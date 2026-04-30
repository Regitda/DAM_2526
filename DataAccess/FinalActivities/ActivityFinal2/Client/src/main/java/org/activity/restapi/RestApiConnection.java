package org.activity.restapi;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Scanner;

public class RestApiConnection {

    protected String server = "https://localhost:8443/";
    protected String apiPath = "restapi";

    public String getServer() { return server; }
    public String getApiPath() {
        return getServer()+apiPath;
    }

    public void setApiPath(String server, String apiPath) {
        if (server != null && server.contains("https"))
            trustAllCertificates();        this.server = server;
        this.apiPath = apiPath;
    }

    public RestApiConnection(String server, String apiPath) {
        setApiPath(server, apiPath);
    }

    public void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(
                                X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(
                                X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(
                    sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(
                    (hostname, session) -> true
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isServerAvailable() {
        try {
            URL url = new URL(getServer());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000); // Timeout en milisegundos
            conn.setReadTimeout(3000);
            int responseCode = conn.getResponseCode();
            return (responseCode == 200);
        } catch (IOException e) {
            return false; // Si no se puede conectar, se asume que el servidor no está disponible
        }
    }

    private static HttpResponse buildResponse(HttpURLConnection conn) throws IOException {
        HttpResponse response = new HttpResponse(conn.getResponseCode(), conn.getResponseMessage(), "");

        InputStream stream = null;

        if (conn.getResponseCode() == 200) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
        }

        if (stream != null) {
            Scanner scanner = new Scanner(stream, "UTF-8");
            String body = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
            response.setBody(body);
        }
        return response;
    }

    public HttpResponse getRequest(String endpoint) throws IOException {
        HttpURLConnection conn = null;

        URL url = new URL(getApiPath() + endpoint);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000); // Timeout en milisegundos
        conn.setReadTimeout(3000);
        conn.setRequestProperty("Accept", "application/json");

        HttpResponse response = buildResponse(conn);

        if (conn != null)
            conn.disconnect();

        return response;
    }

    public HttpResponse getRequest(String endPoint, String codeToRetrieve) throws IOException {
        return getRequest(endPoint + "/" + codeToRetrieve);
    }

    public HttpResponse post(String endpoint, String jsonInputString) throws IOException {
        return postOrPut(jsonInputString, endpoint, "POST");
    }

    public HttpResponse put(String endpoint, String jsonInputString) throws IOException {
        return postOrPut(jsonInputString, endpoint, "PUT");
    }

    HttpResponse postOrPut(String jsonInputString, String endpoint, String method) throws IOException {
        HttpURLConnection conn = null;

        URL url = new URL(getApiPath() + endpoint );
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(3000); // Timeout en milisegundos
        conn.setReadTimeout(3000);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        HttpResponse response = buildResponse(conn);

        if (conn != null)
             conn.disconnect();

        return response;
    }

    public void delete(String endPoint, String codeToDelete) throws IOException {
        HttpURLConnection conn = null;

        URL url = new URL(getApiPath() + endPoint + "/" + codeToDelete);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setConnectTimeout(3000); // Timeout en milisegundos
        conn.setReadTimeout(3000);
        if (conn.getResponseCode() != 200)
            throw new ConnectException(conn.getResponseMessage());

        if (conn != null)
            conn.disconnect();
    }
}
