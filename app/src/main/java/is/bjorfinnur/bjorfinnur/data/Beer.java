package is.bjorfinnur.bjorfinnur.data;


import android.location.Location;


public class Beer implements Comparable<Beer>{

    private final int id;
    private final String name;
    private final String manufacturer;
    private final String type;
    private final String description;
    private final String imageName;

    public Beer(int id, String name, String manufacturer, String type, String description) {
        this(id, name, manufacturer, type, description, "bjorfinnur");
    }

    public Beer(int id, String name, String manufacturer, String type, String description, String imageName) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.type = type;
        this.description = description;
        this.id = id;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String toString(){
        String s = "";
        s += "Beer: ";
        s += "id: " + id;
        s += " name: " + name;
        s += " manufacturer: " + manufacturer;
        s += " type: " + type;
        s += " description: " + description + ".";
        return s;
    }

    @Override
    public int compareTo(Beer another) {
        return (new Integer(this.id)).compareTo(another.getId());
    }

    public String getImageName() {
        return imageName;
    }


}
