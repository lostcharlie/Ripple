package ripple.common;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class UpdateMessage extends Message {
    private String applicationName;
    private String key;
    private String value;
    private int lastUpdateServerId;
    private Date lastUpdate;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLastUpdateServerId() {
        return lastUpdateServerId;
    }

    public void setLastUpdateServerId(int lastUpdateServerId) {
        this.lastUpdateServerId = lastUpdateServerId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public UpdateMessage(UUID uuid, String type) {
        super(uuid, type);
    }

    public UpdateMessage(String type) {
        super(type);
    }
}
