package is.bjorfinnur.bjorfinnur;


public class Price {

    private String currency;
    private int units;
    private int thousands;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getThousands() {
        return thousands;
    }

    public void setThousands(int thousands) {
        this.thousands = thousands;
    }
}
