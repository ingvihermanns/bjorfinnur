package is.bjorfinnur.bjorfinnur;

public class Bar {

    private String barName;
    private String address;
    private String description;
    private String latitude;
    private String longitude;

    public Bar(String barName, String address, String description, String latitude, String longitude){
        this.barName = barName;
        this.address = address;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
