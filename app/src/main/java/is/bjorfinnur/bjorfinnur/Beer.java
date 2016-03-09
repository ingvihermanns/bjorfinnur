package is.bjorfinnur.bjorfinnur;


public class Beer {

    private String beerName;
    private String manufacturer;
    private String type;

    public Beer(String beerName, String manufacturer, String type) {
        this.beerName = beerName;
        this.manufacturer = manufacturer;
        this.type = type;
    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
