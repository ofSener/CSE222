package protocols;

/*
 * SPI protocol implementation.
 */
public class SPI implements Protocol {

    @Override
    public String getProtocolName() {
        return "SPI";
    }

    @Override
    public String read() {
        return getProtocolName() + ": Reading.";
    }

    @Override
    public void write(String data) {
        System.out.println(getProtocolName() + ": Writing \"" + data + "\".");
    }
}
