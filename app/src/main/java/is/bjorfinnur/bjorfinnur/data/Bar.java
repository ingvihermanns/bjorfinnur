package is.bjorfinnur.bjorfinnur.data;

public class Bar implements Comparable<Bar>{

    private final int id;
    private final String name;
    private final String address;
    private final String latitude;
    private final String longitude;
    private final String description;

    public Bar(int id, String name, String address, String latitude, String longitude, String description){
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Bar another) {
        return (new Integer(this.id)).compareTo(another.getId());
    }
}
