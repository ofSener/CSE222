package protocols;

/*
 * OneWire protocol implementation.
 */
public class OneWire implements Protocol {

    @Override
    public String getProtocolName() {
        return "OneWire";
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
