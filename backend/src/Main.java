import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/accountId", new TrackmaniaController.AccountIdHandler());
        server.createContext("/api/token", new TokenController.TokenHandler());
        server.createContext("/api/records", new TrackmaniaController.RecordsHandler());
        server.createContext("/", exchange -> {
            String uri = exchange.getRequestURI().getPath();
            String filePathStr = "public" + (uri.equals("/") ? "/index.html" : uri);
            Path filePath = Paths.get(filePathStr);

            if (Files.exists(filePath)) {
                String contentType = "text/plain";
                if (filePathStr.endsWith(".html")) contentType = "text/html";
                else if (filePathStr.endsWith(".css")) contentType = "text/css";
                else if (filePathStr.endsWith(".js")) contentType = "application/javascript";

                byte[] fileBytes = Files.readAllBytes(filePath);
                exchange.getResponseHeaders().add("Content-Type", contentType);
                exchange.sendResponseHeaders(200, fileBytes.length);
                exchange.getResponseBody().write(fileBytes);
            } else {
                String error = "<h1>404 - File not found</h1>";
                exchange.sendResponseHeaders(404, error.length());
                exchange.getResponseBody().write(error.getBytes());
            }

            exchange.getResponseBody().close();
        });
        server.start();
        System.out.println("Backend started");
        }
}
