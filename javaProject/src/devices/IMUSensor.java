package devices;

import protocols.Protocol;

/*
 * Abstract IMUSensor: getAccel(), getRot() => calls protocol.read(), returns random floats,
 * data2String() -> "Accel: %.2f, Rot: %.2f"
 */
public abstract class IMUSensor extends Sensor {

    public IMUSensor(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    public float getAccel() {
        this.protocol.read();
        return (float)(Math.random() * 10.0);
    }

    public float getRot() {
        this.protocol.read();
        return (float)(Math.random() * 5.0);
    }

    @Override
    public String getSensType() {
        return "IMUSensor";
    }

    @Override
    public String data2String() {
        float a = getAccel();
        float r = getRot();
        return String.format("Accel: %.2f, Rot: %.2f", a, r);
    }
}
