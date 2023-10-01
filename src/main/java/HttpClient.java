import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    public static String httpGET(String url, int port, boolean verbose) throws IOException, URISyntaxException {

        URI uri = new URI(url);
        String host = uri.getHost();
        String path = uri.getRawPath();
        String queryParameters = uri.getRawQuery();
        Socket socket = new Socket(host, port);

        //request line
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(String.format("GET %s?%s HTTP/1.0", path, queryParameters));
        out.println("Host:" + host);
        out.println("User-Agent:" + "Concordia-HTTP/1.0");
        out.println("Accept:" + "application/json");
        out.println("");
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String responseLine = in.readLine();
        String line;
        StringBuilder responseHeaders = new StringBuilder(responseLine).append("\n");

        while (!(line = in.readLine()).isEmpty()) {
            responseHeaders.append(line).append("\n");
        }

        StringBuilder responseBody = new StringBuilder();
        while ((line = in.readLine()) != null) {
            responseBody.append(line).append("\n");
        }

        in.close();
        out.close();
        socket.close();

        if (verbose) {
            return responseHeaders.append(responseBody).toString();
        } else {
            return responseBody.toString();
        }
    }

    public static String httpPOST(String URL, String parameters, String headers, int port, boolean verbose) throws IOException, URISyntaxException {
        URI uri = new URI(URL);
        String host = uri.getHost();
        String path = uri.getRawPath();
        Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        //request line
        out.println(String.format("POST %s HTTP/1.0", path));
        out.println("Host: " + host);
        out.println(headers);
        out.println("Content-Length: " + parameters.length());
        out.println("User-agent: " + "Concordia-HTTP/1.0");
        out.println();
        out.println(parameters);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String responseLine = in.readLine();
        String line;
        StringBuilder responseHeaders = new StringBuilder(responseLine).append("\n");

        while (!(line = in.readLine()).isEmpty()) {
            responseHeaders.append(line).append("\n");
        }

        StringBuilder responseBody = new StringBuilder();
        while ((line = in.readLine()) != null) {
            responseBody.append(line).append("\n");
        }

        in.close();
        out.close();
        socket.close();

        if (verbose) {
            return responseHeaders.append(responseBody).toString();
        } else {
            return responseBody.toString();
        }
    }
}
