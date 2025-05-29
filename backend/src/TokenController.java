import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class TokenController {
    public static class TokenHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Activer CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            String email = System.getenv("UBI_EMAIL");
            String password = System.getenv("UBI_PASSWORD");

            if (email == null || password == null) {
                System.err.println("Env_not_defined");
                exchange.sendResponseHeaders(500, 0);
                exchange.getResponseBody().write("{\"error\":\"Missing credentials\"}".getBytes());
                exchange.getResponseBody().close();
                return;
            }

            try {
                String nadeoToken = AuthUtils.getCachedNadeoToken(email, password);
                JSONObject json = new JSONObject();
                json.put("nadeo_token", nadeoToken);

                byte[] response = json.toString().getBytes();
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
                String error = "{\"error\":\"Token generation failed\"}";
                exchange.sendResponseHeaders(500, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    }
}
