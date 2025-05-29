import java.time.LocalDateTime;

public class Record {

    private int score;
    private String mapName;
    private double time;
    private String mapId;
    private LocalDateTime timestamp;

    public Record(String mapName, double time) {
        this.mapName = mapName;
        this.time = time;
        this.score = score;
    }

    public Record() {

    }

    public String getMapName() {
        return mapName;
    }

    public double getTime() {
        return time;
    }

    public String getMapId() {
        return mapId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDateTime.parse(timestamp);
    }

    public void setScore(double score) {
        this.time = score;
    }

    public void setGroupUid(String groupUid) {
        this.mapId = groupUid;
    }

    public int getScore() {
        return score;
    }
}
