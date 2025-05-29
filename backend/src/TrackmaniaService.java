import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackmaniaService {

    /**
     * Using api.trackmania.com to convert the username of the player to the accountID
     * @param pseudo Player's name
     * @return AccountID
     */
    public static String getAccountIdFromPseudo(String pseudo) {
        try {
            String bearerToken = AuthUtils.getTrackmaniaBearerToken();
            if (bearerToken == null) {
                System.err.println("OAuth2 Token not found");
                return null;
            }

            String url = "https://api.trackmania.com/api/display-names/account-ids?displayName[]=" + URLEncoder.encode(pseudo, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + bearerToken)
                    .header("Accept", "application/json")
                    .header("User-Agent", "Trackmania Records")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            JSONObject json = new JSONObject(response.body());

            if (json.has(pseudo)) {
                return json.getString(pseudo);
            } else {
                System.out.println("No result for : " + pseudo);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Since we need the records of the specific player, Trackmania doesn't make it easy for us. So we have to find a loophole, which in our case
     * we use every Trackmania Groups to recall the API of Trackmania with every single groups
     * We hosted on Railway with 'NocoDB' and 'Postgres'. If you want to host your own, just have to put your database_URL and database_user_token on your env.
     * This method will call our database to get access to all possible GROUP_UID
     */
    public static List<Group> getAllTrackmaniaGroups() {
        try {
            String apiUrl = System.getenv("DATABASE_URL");
            String apiToken = System.getenv("DATABASE_USER_TOKEN");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("xc-token", apiToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            JSONArray records = json.getJSONArray("list");
            List<Group> groups = new ArrayList<>();

            for (int i = 0; i < records.length(); i++) {
                JSONObject obj = records.getJSONObject(i);
                if (!obj.isNull("name") && !obj.isNull("uid")) {
                    Group g = new Group();
                    g.setId(String.valueOf(obj.get("Id")));
                    g.setUid(obj.getString("uid"));
                    g.setName(obj.getString("name"));
                    groups.add(g);
                }
            }

            return groups;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * This method is a bit robust, will make request for every groupID on DB (Not recommended).
     * By the time, my recommandation would either be to have a robust database with already player's record or ask for an OAUTH2 w/ Ubisoft
     * and use the API documented on Openplanet to get personal best from a registrated account, if player want to show them public -> store it
     * in your database.
     * @param accountId AccountID
     * @param nadeoToken nadeoToken
     * @return Player's Record
     */
    public static List<Record> getAllRecordsFromAllGroups(String accountId, String nadeoToken) {
        List<Record> allRecords = new ArrayList<>();
        List<Group> groups = getAllTrackmaniaGroups();

        for (Group group : groups) {
            try {
                String groupUid = group.getUid();
                String url = "https://live-services.trackmania.nadeo.live/api/token/leaderboard/group/" + groupUid + "/player/" + accountId;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "nadeo_v1 t=" + nadeoToken)
                        .header("User-Agent", "Trackmania Records")
                        .GET()
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                JSONObject json = new JSONObject(response.body());

                if (!json.has("records")) continue;

                JSONArray recordsArray = json.getJSONArray("records");

                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject recordJson = recordsArray.getJSONObject(i);
                    Record record = new Record(
                            recordJson.optString("mapName", "Inconnue"),
                            recordJson.getDouble("score") / 1000.0
                    );
                    record.setMapId(recordJson.optString("mapId"));
                    record.setTimestamp(recordJson.optString("timestamp"));
                    allRecords.add(record);
                }

            } catch (Exception e) {
                System.err.println("Error for the group: " + group.getUid());
                e.printStackTrace();
            }
        }

        return allRecords;
    }


}
