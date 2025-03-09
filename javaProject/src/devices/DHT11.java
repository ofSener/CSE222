package devices;

import protocols.Protocol;

/*
 * DHT11 (OneWire only). Must implement turnON, turnOFF, getName.
 * For the example: prints "<name>: Turning ON/OFF" then protocol.write("turnON"/"turnOFF").
 */
public class DHT11 extends TempSensor {

    public DHT11(String devID, Protocol protocol) {
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
        return "DHT11";
    }
}
