package devices;

import protocols.Protocol;

/*
 * Abstract Display: printData(string) -> calls protocol.write().
 * getDevType() -> "Display".
 */
public abstract class Display extends Device {

    public Display(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    public abstract void printData(String data);

    @Override
    public String getDevType() {
        return "Display";
    }
}
