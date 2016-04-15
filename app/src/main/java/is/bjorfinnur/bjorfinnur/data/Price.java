package is.bjorfinnur.bjorfinnur.data;


public class Price {

    private final String currency;
    private final int units;
    private final int thousands;

    public Price(String currency, int units){
        this.currency = currency;
        this.units = units;
        this.thousands = 0;
    }

    public Price(String currency, int units, int thousands){
        this.currency = currency;
        this.units = units;
        this.thousands = thousands;
    }

    public String getCurrency() {
        return currency;
    }

    public int getUnits() {
        return units;
    }


    public int getThousands() {
        return thousands;
    }

}
