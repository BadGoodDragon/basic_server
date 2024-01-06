import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {

    public static void main(String[] args) throws IOException {
        // Set the port on which the server will run
        int port = 8080;

        // Create an HTTP server and set the request handler
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server is running at http://localhost:" + port);
    }

    // HTTP request handler
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Log the details of the incoming HTTP request
            logRequestDetails(exchange);

            // Send a response to the client
            String response = "Hello, World!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        // Helper method to log request details
        private void logRequestDetails(HttpExchange exchange) throws IOException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            String requestURI = exchange.getRequestURI().toString();

            System.out.println("Received HTTP request at " + timestamp + "  URI: " + requestURI);
        }
    }
}
