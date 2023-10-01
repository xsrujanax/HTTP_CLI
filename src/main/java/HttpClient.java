//import java.io.*;
//import java.net.*;
//import java.nio.charset.StandardCharsets;
//
//public class HttpClient {
//
//    public static void httpGET(String url, int port) throws IOException, URISyntaxException {
//
//        URI uri = new URI(url);
//        String host = uri.getHost();
//        String path = uri.getRawPath();
//        String queryParameters = uri.getRawQuery();
//        Socket socket = new Socket(host, port);
//
//        //request line
//        PrintWriter out = new PrintWriter(socket.getOutputStream());
//        out.println(String.format("GET %s?%s HTTP/1.0",path,queryParameters));
//        out.println("Host:" + host);
//        out.println("User-Agent:" + "Concordia-HTTP/1.0");
//        out.println("Accept:"+ "application/json");
//        out.println("");
//        out.flush();
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//        String responseLine = in.readLine();
//        System.out.println(responseLine);
//        String line;
//
//        //read the response headers
//        while (!(line = in.readLine()).isEmpty()) {
//            System.out.println(line);
//        }
//
//        // Read the response body from the server, if any
//        while ((line = in.readLine()) != null) {
//            System.out.println(line);
//        }
//
//        in.close();
//        out.close();
//        socket.close();
//    }
//
//    public static void httpPOST(String URL, String parameters, String headers, int port) throws IOException, URISyntaxException
//    {
//        URI uri = new URI(URL);
//        String host = uri.getHost();
//        System.out.println(host);
//        String path = uri.getRawPath();
//        Socket socket = new Socket(host, port);
//        PrintWriter out = new PrintWriter(socket.getOutputStream());
//
//        //request line
//        out.println(String.format("POST %s HTTP/1.0",path));
//        out.println("Host: " + host);
//        out.println(headers);
//        out.println("Content-Length: " + parameters.toString().length());
//        out.println("User-agent: " + "Concordia-HTTP/1.0");
//        out.println();
//        out.println(parameters.toString());
//        out.flush();
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//
//        String responseLine = in.readLine();
//        System.out.println(responseLine);
//        String line;
//
//        // Read the response headers
//        while(!(line =in.readLine()).isEmpty())
//        {
//            System.out.println(line);
//        }
//
//        // Read the response body
//        while((line =in.readLine())!=null)
//        {
//            System.out.println(line);
//        }
//        in.close();
//        out.close();
//        socket.close();
//    }
//
//    public static void main(String[] args) throws IOException, URISyntaxException {
//        httpGET("http://httpbin.org/get?course=networking&assignment=1",80);
//        httpPOST("http://httpbin.org/post","{\"Assignment\": 1,\"Course}","Content-Type: application/json", 80);
//    }
//}


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpClient {

    public static void httpGET(String url, int port, boolean verbose) throws IOException, URISyntaxException {

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
        if (verbose) {
            System.out.println(responseLine);
        }
        String line;

        //read the response headers
        while (!(line = in.readLine()).isEmpty()) {
            if (verbose) {
                System.out.println(line);
            }
        }

        // Read the response body from the server, if any
        StringBuilder responseBody = new StringBuilder();
        while ((line = in.readLine()) != null) {
            responseBody.append(line).append("\n");
        }

        in.close();
        out.close();
        socket.close();

        // Print the response body
        System.out.println(responseBody.toString());
    }

    public static void httpPOST(String URL, String parameters, String headers, int port, boolean verbose) throws IOException, URISyntaxException {
        URI uri = new URI(URL);
        String host = uri.getHost();
        System.out.println(host);
        String path = uri.getRawPath();
        Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        //request line
        out.println(String.format("POST %s HTTP/1.0", path));
        out.println("Host: " + host);
        out.println(headers);
        out.println("Content-Length: " + parameters.toString().length());
        out.println("User-agent: " + "Concordia-HTTP/1.0");
        out.println("");
        out.println(parameters.toString());
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String responseLine = in.readLine();
        if (verbose) {
            System.out.println(responseLine);
        }
        String line;

        // Read the response headers
        while (!(line = in.readLine()).isEmpty()) {
            if (verbose) {
                System.out.println(line);
            }
        }

        // Read the response body
        StringBuilder responseBody = new StringBuilder();
        while ((line = in.readLine()) != null) {
            responseBody.append(line).append("\n");
        }

        in.close();
        out.close();
        socket.close();

        // Print the response body
        System.out.println(responseBody.toString());
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String url = "http://httpbin.org/get?course=networking&assignment=1";
        int port = 80;
        boolean verbose = false;

        // Check if verbose flag (-v) is provided
        for (String arg : args) {
            if (arg.equals("-v")) {
                verbose = true;
                break;
            }
        }

        // Send the GET request with or without verbose output
        httpGET(url, port, verbose);

        String postUrl = "http://httpbin.org/post";
        String postParameters = "{\"Assignment\": 1,\"Course\": \"networking\"}";
        String postHeaders = "Content-Type: application/json";

        // Send the POST request with or without verbose output
        httpPOST(postUrl, postParameters, postHeaders, port, verbose);
    }
}
