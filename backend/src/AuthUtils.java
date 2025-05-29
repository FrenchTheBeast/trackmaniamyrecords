import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONObject;

public class AuthUtils {
    private static String cachedToken = null;
    private static long tokenExpirationTime = 0;

    /**
     * This method will serve to cache our nadeoToken instead of recalling API everytime a user want to access another user's record.
     * We cache it for 15 min > depending on traffic > increase.
     * @param email ubisoft email
     * @param password ubisoft password
     * @return nadeoToken cached for 15min
     */
    public static String getCachedNadeoToken(String email, String password) {
        long now = System.currentTimeMillis();
        if (cachedToken != null && now < tokenExpirationTime) {
            return cachedToken;
        }

        String ticket = getUbisoftToken(email, password);
        if (ticket == null) return null;

        String newToken = getNadeoToken(ticket);
        if (newToken != null) {
            cachedToken = newToken;
            tokenExpirationTime = now + (15 * 60 * 1000); // 15 min
        }

        return cachedToken;
    }

    /**
     * Trackmania Bearer Token are obtenable at https://api.trackmania.com/
     * @return accessToken
     */
    public static String getTrackmaniaBearerToken() {
        try {
            String clientId = System.getenv("TRACKMANIA_CLIENT_ID");
            String clientSecret = System.getenv("TRACKMANIA_CLIENT_SECRET");

            if (clientId == null || clientSecret == null) {
                System.err.println("Missing TRACKMANIA_CLIENT_ID or TRACKMANIA_CLIENT_SECRET");
                return null;
            }

            String form = "grant_type=client_credentials" +
                    "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                    "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.trackmania.com/api/access_token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            return json.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Access Ubisoft API by identifying the Ubisoft account.
     * For more information refers to Openplanet documentation at https://webservices.openplanet.dev/auth/token
     * @param mail Ubisoft email
     * @param password Ubisoft password
     * @return Ubisoft Token Key
     */
    public static String getUbisoftToken(String mail, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String auth = mail + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://public-ubiservices.ubi.com/v3/profiles/sessions"))
                    .header("Content-Type", "application/json")
                    .header("Ubi-AppId", "86263886-327a-4328-ac69-527f0d20a237")
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("User-Agent", "Trackmania Records")
                    .POST(BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            if (json.has("ticket")) {
                return json.getString("ticket");
            } else {
                System.out.println("Error (Ubisoft) : " + response.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Access Nadeo (Trackmania API) using the fresh API we got from the Ubisoft Account
     * More informations about Nadeo API access token at https://webservices.openplanet.dev/auth/ubi
     * @param ubisoftToken Ubisoft Token from ubisoft account
     * @return AccessToken for API requests
     */
    public static String getNadeoToken(String ubisoftToken) {
        try {
            String jsonBody = """
                    { "audience": "NadeoLiveServices" }
                    """;
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://prod.trackmania.core.nadeo.online/v2/authentication/token/ubiservices"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "ubi_v1 t=" + ubisoftToken)
                    .header("User-Agent", "Trackmania Records")
                    .POST(BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            if (json.has("accessToken")) {
                return json.getString("accessToken");
            } else {
                System.out.println("Erreur (Nadeo) : " + response.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
