////import joptsimple.OptionParser;
////import joptsimple.OptionSet;
////
////import static java.util.Arrays.asList;
////
////public class httpc {
////    public static void runClient()
////    {
////        System.out.println("Hi");
////    }
////
////    public static void main(String[] args){
////        OptionParser parser = new OptionParser();
////        parser.acceptsAll(asList("host", "h"),"Hostname")
////                .withOptionalArg()
////                .defaultsTo("localhost");
////
////        parser.acceptsAll(asList("port", "p"), "port number")
////                .withOptionalArg()
////                .defaultsTo("80");
////
////        OptionSet opts = parser.parse(args);
////
////        String host = (String) opts.valueOf("host");
////        int port = Integer.parseInt((String)opts.valueOf("port"));
////        System.out.println(host);
////        System.out.println(port);
////        runClient();
////    }
////}

import java.io.IOException;
import java.net.URISyntaxException;

public class httpc {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: httpc (get|post) [-v] (-h 'k:v')* [-d inline-data] [-f file] URL");
            return;
        }

        String method = args[0];
        boolean verbose = false;
        String inlineData = null;
        String fileData = null;
        String url = null;
        String headers = "";

        // Parse command-line arguments
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-v":
                    verbose = true;
                    break;
                case "-d":
                    i++;
                    if (i < args.length) {
                        inlineData = args[i];
                    }
                    break;
                case "-f":
                    i++;
                    if (i < args.length) {
                        fileData = args[i];
                    }
                    break;
                case "-h":
                    i++;
                    if (i < args.length) {
                        headers += args[i] + "\r\n";
                    }
                    break;
                default:
                    url = arg;
                    break;
            }
        }

        if (method.equals("get") && (inlineData != null || fileData != null)) {
            System.out.println("GET request should not have -d or -f options.");
            return;
        }

        if (method.equals("post") && inlineData != null && fileData != null) {
            System.out.println("POST request should have either -d or -f, but not both.");
            return;
        }

        if (url == null) {
            System.out.println("URL is required.");
            return;
        }

        try {
            HttpClient httpClient = new HttpClient();

            if (method.equals("get")) {
                if (verbose) {
                    System.out.println("HTTP GET Request:");
                    System.out.println("URL: " + url);
                    System.out.println("Headers: " + headers);
                    System.out.println();
                }
                httpClient.httpGET(url, 80, verbose);
            } else if (method.equals("post")) {
                if (verbose) {
                    System.out.println("HTTP POST Request:");
                    System.out.println("URL: " + url);
                    System.out.println("Headers: " + headers);
                    System.out.println("Data: " + (inlineData != null ? inlineData : fileData));
                    System.out.println();
                }
                httpClient.httpPOST(url, (inlineData != null ? inlineData : fileData), headers, 80, verbose);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
