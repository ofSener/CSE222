package protocols;

/*
 * I2C protocol implementation.
 */
public class I2C implements Protocol {

    @Override
    public String getProtocolName() {
        return "I2C";
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
