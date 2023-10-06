import java.io.*;
import java.net.URISyntaxException;

/**
 * Command line application for HTTP POST and GET requests
 */
public class httpc {
    public static String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        StringBuilder str = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String lines = null;
        str.append(lines = br.readLine());
        while((lines = br.readLine())!= null)
        {
            str.append("\n").append(lines);
        }
        br.close();
        return str.toString();
    }

    public static void writeToFile(String fileName , String output) throws IOException {
        File file = new File(fileName);
        BufferedWriter bw = new BufferedWriter( new FileWriter(file));
        bw.flush();
        bw.write(output);
        bw.close();
        System.out.println("Response has been saved to file:" + fileName);
    }

    public static String processInlineData(String data) {
        String[] keyValuePairs = data.replaceAll("[{}]", "").split(",");
        StringBuilder jsonBuilder = new StringBuilder("{");

        for (String keyValue : keyValuePairs) {
            String[] parts = keyValue.trim().split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                jsonBuilder.append("\"").append(key).append("\":").append(value).append(",");
            }
        }

        if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
            jsonBuilder.setCharAt(jsonBuilder.length() - 1, '}');
        } else {
            jsonBuilder.append("}");
        }

        String jsonString = jsonBuilder.toString();
        return jsonString;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            Messages.printHelpMessage();
            return;
        }

        boolean verbose = false;
        String method = args[0];
        String inlineData = null;
        String fileData = null;
        String newFile = null;
        String url = null;
        StringBuilder headers = new StringBuilder();
        String output = "";

        if (args.length == 2 && method.equals("help")) {
            if (args[1].equals("get")) {
                Messages.printHelpGETMessage();
            } else if (args[1].equals("post")) {
                Messages.printHelpPOSTMessage();
            } else {
                Messages.printHelpMessage();
            }
            return;
        }

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
                        inlineData =processInlineData(args[i]);
                    }
                    break;
                case "-f":
                    i++;
                    if (i < args.length) {
                        fileData = readFile(args[i]);
                    }
                    break;
                case "-h":
                    i++;
                    if (i < args.length) {
                        headers.append(args[i]).append(",");
                    }
                    break;
                case "-o":
                    i++;
                    if(i < args.length) {
                        newFile = args[i];
                    }
                    break;
                default:
                    url = arg;
                    break;
            }
        }
        if (method.equals("get") && (inlineData != null || fileData != null)) {
            System.out.println("GET request should not have -d or -f options.");
            Messages.printHelpGETMessage();
            return;
        }
        if (method.equals("post") && inlineData != null && fileData != null) {
            System.out.println("POST request should have either -d or -f, but not both.");
            Messages.printHelpPOSTMessage();
            return;
        }
        if (url == null) {
            System.out.println("URL is required.");
            Messages.printHelpMessage();
            return;
        }

        try {
            HttpClient httpClient;
            if (method.equals("get")) {
                output = HttpClient.httpGET(url, 80, verbose, headers.toString());
            } else if (method.equals("post")) {
                output = HttpClient.httpPOST(url, (inlineData != null ? inlineData : fileData), headers.toString(), 80, verbose);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        if(newFile!=null)
            writeToFile(newFile, output);
        else
            System.out.println(output);
    }
}