package protocols;

/*
 * UART protocol implementation.
 */
public class UART implements Protocol {

    @Override
    public String getProtocolName() {
        return "UART";
    }

    @Override
    public String read() {
        // Return the required format
        // UART: Reading returns.
        return getProtocolName() + ": Reading.";
    }

    @Override
    public void write(String data) {
        //Uart writing returns.
        System.out.println(getProtocolName() + ": Writing \"" + data + "\".");
    }
}
