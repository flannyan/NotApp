package appliance;

/**
 * Created by Devon on 12/9/2017
 * Represents air cleaner/purifier machines.
 */
public class Dryer extends Appliance {

    private String applianceType = "Dryer";

    /**
     * Author: Devon on 12/9/2017
     * See Appliance for 'super' method. applianceType is pre-defined and does not change,
     * so no additional constructor input is necessary
     **/
    public Dryer(String inModel, String inBrand, double inEnergy) {
        super(inModel, inBrand, inEnergy);
    }

    /**
     * Author: Devon on 12/9/2017
     * See super()
     */
    public Dryer() {
        super();
    }

    /**
     * Author: Devon on 12/9/2017
     * See super()
     */
    public Dryer getCopy() {
        return new Dryer(this.getModel(), this.getBrand(), this.getEnergy());
    }

    /**
     * Author: Devon on 12/9/2017
     * Returns appliance type.
     **/
    public String getApplianceType() {
        return applianceType;
    }

    @Override
    public String toString() {
        return applianceType + " " + model + " " + brand + " " + energy;
    }
}

