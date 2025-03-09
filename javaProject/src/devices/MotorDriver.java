package devices;

import protocols.Protocol;

/*
 * Abstract MotorDriver: setMotorSpeed(int) -> protocol.write(...).
 * getDevType() -> "MotorDriver".
 */
public abstract class MotorDriver extends Device {

    public MotorDriver(String devID, Protocol protocol) {
        super(devID, protocol);
    }

    public abstract void setMotorSpeed(int speed);

    @Override
    public String getDevType() {
        return "MotorDriver";
    }
}
