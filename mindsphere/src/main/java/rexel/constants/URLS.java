package rexel.constants;

public class URLS {
    private final static String ADDRESS = "https://gateway.cn1.mindsphere-in.cn";
    private final static String TOKEN_BASE_PATH = ADDRESS + "/api/technicaltokenmanager/v3";
    private final static String ASSET_BASE_PATH = ADDRESS + "/api/assetmanagement/v3";
    private final static String TIME_SERIES_BASE_PATH = ADDRESS + "/api/iottimeseries/v3";
    private final static String AGGREGATES_BASE_PATH = ADDRESS + "/api/iottsaggregates/v3";
    private final static String DEVICE_MANAGEMENT = ADDRESS + "/api/devicemanagement/v3";

    // Token Management
    public final static String OAUTH_TOKEN = TOKEN_BASE_PATH + "/oauth/token";

    // IoT Time Series
    public final static String TIME_SERIES = TIME_SERIES_BASE_PATH + "/timeseries";

    // Asset Management
    public final static String ASSET_TYPES = ASSET_BASE_PATH + "/assettypes";
    public final static String ASSETS = ASSET_BASE_PATH + "/assets";

    // IoT Time Series Aggregates
    public final static String AGGREGATES = AGGREGATES_BASE_PATH + "/aggregates";

    // Device Management
    public final static String DEVICE_TYPES = DEVICE_MANAGEMENT + "/deviceTypes";
    public final static String DEVICES = DEVICE_MANAGEMENT + "/devices";
}