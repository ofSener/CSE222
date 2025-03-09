package devices;

import protocols.Protocol;

/*
 * Abstract Sensor class.
 * getsenstype()/getDevtype only can be TempSensor or IMUSensor.
 *  
 *
 */
public abstract class Sensor extends Device {

    public Sensor(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    public abstract String getSensType();
    public abstract String data2String();

    @Override
    public String getDevType() {
        return getSensType() + " Sensor";
    }
}
