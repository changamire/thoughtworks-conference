package info.gregbiegel.service.binpack;

/**
 * A factory interface implemented by specific bin packer algorithm factory classes.
 *
 */
public interface IBinPackerFactory {

    /**
     * Creates an instance of a class implementing the IBinPacker interface
     * 
     * @return an object that implements the IBinPacker interface
     */
    public IBinPacker createBinPacker();
}
