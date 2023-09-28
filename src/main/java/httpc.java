import joptsimple.OptionParser;
import joptsimple.OptionSet;

import static java.util.Arrays.asList;

public class httpc {
    public static void runClinet()
    {
        System.out.println("Hi");
    }

    public static void main(String[] args){
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("host", "h"),"Hostname")
                .withOptionalArg()
                .defaultsTo("localhost");

        parser.acceptsAll(asList("port", "p"), "port number")
                .withOptionalArg()
                .defaultsTo("80");

        OptionSet opts = parser.parse(args);

        String host = (String) opts.valueOf("host");
        int port = Integer.parseInt((String)opts.valueOf("port"));
        System.out.println(host);
        System.out.println(port);
        runClinet();
    }
}
