package info.gregbiegel.service.binpack;

/**
 * An implementation of the IBinPackerFactory interface that creates objects
 * than implement the next fit bin packing algorithm.
 *
 */
public class NextFitBinPackerFactory implements IBinPackerFactory {

    /*
     * @see info.gregbiegel.service.binpack.IBinPackerFactory#createBinPacker()
     */
    @Override
    public IBinPacker createBinPacker() {
        return new NextFitBinPacker();
    }
}
