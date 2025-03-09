package devices;

import protocols.Protocol;

/*
 * random temp and xx.xx temp format
 */
public abstract class TempSensor extends Sensor {

    public TempSensor(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    public float getTemp() {
        this.protocol.read(); 
        // Then produce a random temperature
        float temp = (float)(Math.random() * 2 + 0.5); // e.g. 0.5 ~ 2.5
        return temp;
    }

    @Override
    public String getSensType() {
        return "TempSensor";
    }

    @Override
    public String data2String() {
        float t = getTemp();
        // 0.53 etc.
        return String.format("Temp: %.2f", t);
    }
}
