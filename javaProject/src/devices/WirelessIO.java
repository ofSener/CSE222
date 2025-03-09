package devices;

import protocols.Protocol;

/*
 * Abstract WirelessIO: sendData -> protocol.write, recvData -> protocol.read.
 * getDevType() -> "WirelessIO".
 */
public abstract class WirelessIO extends Device {

    public WirelessIO(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    public abstract void sendData(String data);
    public abstract String recvData();

    @Override
    public String getDevType() {
        return "WirelessIO";
    }
}
