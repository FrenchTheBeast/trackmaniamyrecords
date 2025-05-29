import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TrackmaniaController {

    public static class RecordsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
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

            try {
                String query = exchange.getRequestURI().getQuery();
                String[] params = query.split("=");
                String pseudo = params.length > 1 ? params[1] : null;

                if (pseudo == null || pseudo.isBlank()) {
                    exchange.sendResponseHeaders(400, 0);
                    exchange.getResponseBody().write("{\"error\":\"Missing username\"}".getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String accountId = TrackmaniaService.getAccountIdFromPseudo(pseudo);
                String email = System.getenv("UBI_EMAIL");
                String password = System.getenv("UBI_PASSWORD");
                String nadeoToken = AuthUtils.getCachedNadeoToken(email,password);

                if (accountId == null || nadeoToken == null) {
                    exchange.sendResponseHeaders(500, 0);
                    exchange.getResponseBody().write("{\"error\":\"Failed to get accountId or token\"}".getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                List<Record> records = TrackmaniaService.getAllRecordsFromAllGroups(accountId, nadeoToken);

                JSONArray jsonArray = new JSONArray();
                for (Record r : records) {
                    JSONObject obj = new JSONObject();
                    obj.put("mapName", r.getMapName());
                    obj.put("score", r.getScore());
                    obj.put("timestamp", r.getTimestamp());
                    obj.put("mapId", r.getMapId());
                    jsonArray.put(obj);
                }

                byte[] response = jsonArray.toString().getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
                exchange.getResponseBody().close();

            } catch (Exception e) {
                e.printStackTrace();
                String error = "{\"error\":\"Unexpected server error\"}";
                exchange.sendResponseHeaders(500, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    }


    public static class AccountIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String pseudo = null;

            if (query != null && query.startsWith("pseudo=")) {
                pseudo = URLDecoder.decode(query.substring("pseudo=".length()), StandardCharsets.UTF_8);
            }

            if (pseudo == null || pseudo.isBlank()) {
                String error = "{\"error\":\"Username missing\"}";
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(400, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            try {
                String bearerToken = AuthUtils.getTrackmaniaBearerToken();
                System.out.println("Bearer Token : " + bearerToken);
                String accountId = TrackmaniaService.getAccountIdFromPseudo(pseudo);

                JSONObject result = new JSONObject();
                result.put("pseudo", pseudo);
                result.put("accountId", accountId);

                String json = result.toString();
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                exchange.getResponseBody().write(json.getBytes());
                exchange.getResponseBody().close();

            } catch (Exception e) {
                e.printStackTrace();
                String error = "{\"error\":\"Internal server error\"}";
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(500, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    }

    private static void applyCORSHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }
}
