package is.bjorfinnur.bjorfinnur;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eva Thor on 4/14/2016.
 */
public class CalculateCoordinates {

    List<Gpscordinates> gpslist = new ArrayList<>();

    public static float[] calculateDistance(List<Gpscordinates> gpslist) {

        float[] results = new float[gpslist.size()];

        Location mylocation = MapsActivity.getMyLocation();
        Location loc1 = new Location("");
        loc1.setLatitude(mylocation.getLatitude());
        loc1.setLongitude(mylocation.getLongitude());

        for (int i = 0; i < gpslist.size(); i++) {
            Gpscordinates gpscord = gpslist.get(i);
            Location loc2 = new Location("");
            loc2.setLatitude(gpscord.getLatitude());
            loc2.setLongitude(gpscord.getLongtitude());
            float distanceInMeters = loc1.distanceTo(loc2);
            results[i] = distanceInMeters;
        }

        return results;

    }




}
