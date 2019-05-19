package info.gregbiegel.service.binpack;

/**
 * A typesafe enumeration of the different bin packing (scheduling) 
 * algorithms available within the system.
 *
 */
public enum BinPackType {
    NEXT_FIT(1), 
    FIRST_FIT(2);

    private int id;

    BinPackType(int id) {
        this.id = id;
    }   

    public int getId() {
        return id;
    }

    public static BinPackType getForId(final int id) {
        for (BinPackType a : values()) {
            if (a.getId() == id) {
                return a;
            }
        }
        return NEXT_FIT; //Default to next fit
    }
}
