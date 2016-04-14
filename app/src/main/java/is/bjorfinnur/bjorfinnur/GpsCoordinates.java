package is.bjorfinnur.bjorfinnur;

/**
 * Created by Eva Thor on 4/10/2016.
 */
public class GpsCoordinates {

    private float latitude;
    private float longtitude;

    public GpsCoordinates(float latitude, float longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

}
