package devices;

import protocols.Protocol;

public class BME280 extends TempSensor {

    public BME280(String devID, Protocol protocol) {
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
        return "BME280";
    }
}
