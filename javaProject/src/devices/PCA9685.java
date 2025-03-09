package devices;

import protocols.Protocol;

public class PCA9685 extends MotorDriver {

    public PCA9685(String devID, Protocol protocol) {
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
    public void setMotorSpeed(int speed) {
        this.protocol.write("SetSpeed " + speed);
    }

    @Override
    public String getName() {
        return "PCA9685";
    }
}
