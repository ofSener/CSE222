package devices;

import protocols.Protocol;

/*
 * An abstract class representing any Device in our system.
 *
 *  - turnON(), turnOFF(), getName() 
 *    implemented only by bottom-level classes
 *  - getDevType() is implemented by each "type" class (Sensor, Display, etc.)
 *  - We store a reference to Protocol, a State (OFF by default), and a devID.
 */
public abstract class Device {
    protected Protocol protocol;
    /*
     * 1- when open it first its OFF.
     * 2-  to Identify devices we use ID
     */
    protected State state = State.OFF;
    protected String devID;

    public Device(String devID, Protocol protocol) {
        this.devID = devID;
        this.protocol = protocol;
    }

    public abstract void turnON();
    public abstract void turnOFF();
    public abstract String getName();

    public abstract String getDevType();

    public State getState() {
        return this.state;
    }

    public String getDevID() {
        return this.devID;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }
}
