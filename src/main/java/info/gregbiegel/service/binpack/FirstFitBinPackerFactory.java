package info.gregbiegel.service.binpack;

/**
 * An implementation of the IBinPackerFactory interface that creates objects
 * than implement the first fit bin packing algorithm.
 *
 */
public class FirstFitBinPackerFactory implements IBinPackerFactory {

    /*
     * @see info.gregbiegel.service.binpack.IBinPackerFactory#createBinPacker()
     */
    @Override
    public IBinPacker createBinPacker() {
        return new FirstFitBinPacker();
    }
}
