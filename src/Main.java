import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.lang.RuntimeException;



public class Main {

    public static void main(String[] args) throws IOException {
        // Set the port on which the server will run
        int port = 8080;

        // Create an HTTP server and set the request handler
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.createContext("/info", new MyHandlerSecond());
        server.createContext("/new", new MyHandlerReply());
        server.createContext("/stop", new MyHandlerStop());
        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server is running at http://localhost:" + port);
    }

    public static String masterResponse = "";
    public static String reply = "Hello world!";

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Log the details of the incoming HTTP request
            logRequestDetails(exchange);

            // Send a response to the client
            exchange.sendResponseHeaders(200, reply.length());
            OutputStream os = exchange.getResponseBody();
            os.write(reply.getBytes());
            os.close();
        }

        // Helper method to log request details
        private void logRequestDetails(HttpExchange exchange) throws IOException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = dateFormat.format(new Date());
            String requestURI = String.format("%-64s", exchange.getRequestURI().toString());
            String clientIP = String.format("%-15s", exchange.getRemoteAddress().getAddress().getHostAddress());
            masterResponse = masterResponse + "New HTTP request     TIME: " + timestamp + "  IP: " + clientIP + "  URI: " + requestURI + "  REPLY: " + reply + "\n";
            System.out.println("New HTTP request  TIME: " + timestamp + "  IP: " + clientIP + "  URI: " + requestURI);
        }
    }
    static class MyHandlerSecond implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, masterResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(masterResponse.getBytes());
            os.close();
        }
    }

    static class MyHandlerReply implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = getQueryMap(query);
            String value = params.get("newreply");
            String pwd = params.get("pwd");
            String correct_password = "qwerty_penis_2237";
            if (value != null && pwd != null) {
                if (pwd.equals(correct_password)) {
                    reply = value;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String timestamp = dateFormat.format(new Date());
                    String requestURI = String.format("%-64s", exchange.getRequestURI().toString());
                    String clientIP = String.format("%-15s", exchange.getRemoteAddress().getAddress().getHostAddress());

                    masterResponse = masterResponse + "New reply            TIME: " + timestamp + "  IP: " + clientIP  + "  REPLY: " + reply + "\n";

                }
            }

            exchange.sendResponseHeaders(200, reply.length());
            OutputStream os = exchange.getResponseBody();
            os.write(reply.getBytes());
            os.close();
        }

        public static Map<String, String> getQueryMap(String query) {
            String[] params = query.split("&");
            Map<String, String> map = new HashMap<String, String>();
            for (String param : params) {
                String [] p=param.split("=");
                String name = p[0];
                if (p.length > 1) {
                    String value = p[1];
                    map.put(name, value);
                }
            }
            return map;
        }
    }

    static class MyHandlerStop implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String r = "stop server";
            exchange.sendResponseHeaders(200, r.length());
            OutputStream os = exchange.getResponseBody();
            os.write(r.getBytes());
            os.close();
            System.exit(1);
        }
    }
}
