package is.bjorfinnur.bjorfinnur.data;

public class GpsCoordinates {

    private final float latitude;
    private final float longitude;

    public GpsCoordinates(float latitude, float longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongtitude() {
        return longitude;
    }


}
