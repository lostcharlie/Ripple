package ripple.server.core;

/**
 * @author Zhen Tang
 */
public final class Endpoint {
    private Endpoint() {

    }

    // Server
    public static final String SERVER_SUBSCRIBE = "/Api/Subscribe";
    public static final String SERVER_UNSUBSCRIBE = "/Api/Unsubscribe";
    public static final String SERVER_PUT = "/Api/Put";
    public static final String SERVER_GET = "/Api/Get";
    public static final String SERVER_SYNC = "/Api/Sync";

    // Client
    public static final String CLIENT_NOTIFY = "/Api/Notify";

    // UI
    public static final String UI_STYLE = "/Style";
    public static final String UI_HOME = "/";
    public static final String UI_GET_CONFIG = "/Config/Get";
    public static final String UI_New_CONFIG = "/Config/New";

}
