package devices;

import protocols.Protocol;

public class GY951 extends IMUSensor {

    public GY951(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    @Override
    public void turnON() {
        System.out.println(getName() + ": Turning ON");
        this.state = State.ON;
        this.protocol.write("turnON");
    }

    @Override
    public void turnOFF() {
        System.out.println(getName() + ": Turning OFF");
        this.state = State.OFF;
        this.protocol.write("turnOFF");
    }

    @Override
    public String getName() {
        return "GY951";
    }
}
