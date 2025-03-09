package Main;

import java.io.File;

import java.util.ArrayList;
import java.util.Scanner;

import devices.*;
import protocols.*;  // Protocol, I2C, SPI, ...

/*
 * Top of system
 * manage all components 
 */
public class HWSystem {
    // A small inner class for ports
    private class Port {
        Protocol protocol;
        Device attachedDevice;
        //if null port is empty.

        Port(Protocol p) {
            protocol = p;
            attachedDevice = null;
        }

        boolean isEmpty() {
            return (attachedDevice == null);
        }
    }

    private ArrayList<Port> ports = new ArrayList<>();
    //define max
    private int maxSensors;
    private int maxDisplays;
    private int maxWireless;
    private int maxMotors;

    //  avoid downcasting , keep type of devise different list
    private ArrayList<Sensor> sensorList = new ArrayList<>();
    private ArrayList<Display> displayList = new ArrayList<>();
    private ArrayList<WirelessIO> wirelessList = new ArrayList<>();
    private ArrayList<MotorDriver> motorList = new ArrayList<>();
    
    
    //AI generated without java.io.FileNotFoundException

public HWSystem(String configFilePath) {
    try {
        // Burada da import java.io.File kaldırmadıysanız, File sınıfını kullanabilirsiniz.
        // Sadece FileNotFoundException yerine Exception yakalıyoruz.
        Scanner sc = new Scanner(new File(configFilePath));

        // 1) Port Configuration
        String line = sc.nextLine().trim();
        String portCfg = line.substring(line.indexOf(':') + 1).trim();
        String[] protos = portCfg.split(",");
        for (String pr : protos) {
            pr = pr.trim();
            Protocol p = createProtocol(pr);
            if (p != null) {
                ports.add(new Port(p));
            }
        }

        // 2) # of sensors
        line = sc.nextLine().trim();
        this.maxSensors = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());

        // 3) # of displays
        line = sc.nextLine().trim();
        this.maxDisplays = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());

        // 4) # of wireless
        line = sc.nextLine().trim();
        this.maxWireless = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());

        // 5) # of motors
        line = sc.nextLine().trim();
        this.maxMotors = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());

        sc.close();

    } catch (Exception e) {
        // Burada tüm hataları (dosya bulunamadı, format hatası vs.) tek seferde yakalıyoruz
        System.out.println("Error reading config file: " + e.getMessage());
    }
}

//  simple switch to create protolcos.
//AI generated
    private Protocol createProtocol(String name) {
        switch(name) {
            case "UART": return new UART();
            case "SPI": return new SPI();
            case "I2C": return new I2C();
            case "OneWire": return new OneWire();
            default:
                System.out.println("Warning: Unknown protocol -> " + name);
                return null;
        }
    }

    // Create device from devName + protocol
    //AI generated
    private Device createDevice(String devName, String devID, Protocol p) {
        switch(devName) {
            case "DHT11":
                if (p instanceof OneWire) return new DHT11(devID, p);
                break;
            case "BME280":
                if (p instanceof I2C || p instanceof SPI) return new BME280(devID, p);
                break;
            case "MPU6050":
                if (p instanceof I2C) return new MPU6050(devID, p);
                break;
            case "GY951":
                if (p instanceof SPI || p instanceof UART) return new GY951(devID, p);
                break;
            case "LCD":
                if (p instanceof I2C) return new LCD(devID, p);
                break;
            case "OLED":
                if (p instanceof SPI) return new OLED(devID, p);
                break;
            case "Bluetooth":
                if (p instanceof UART) return new Bluetooth(devID, p);
                break;
            case "Wifi":
                if (p instanceof SPI || p instanceof UART) return new Wifi(devID, p);
                break;
            case "PCA9685":
                if (p instanceof I2C) return new PCA9685(devID, p);
                break;
            case "SparkFunMD":
                if (p instanceof SPI) return new SparkFunMD(devID, p);
                break;
            default:
                return null;
        }
        return null;
    }

    // addDev <devName> <portID> <devID>
    
    public void addDev(String devName, int portID, String devID) {
        if (!checkPortID(portID)) return;
        Port port = ports.get(portID);
        if (!port.isEmpty()) {
            System.out.println("Error: Port " + portID + " is already occupied.");
            return;
        }
        Device newDev = createDevice(devName, devID, port.protocol);
        if (newDev == null) {
            System.out.println("Error: Incompatible device/protocol or unknown device -> " + devName);
            return;
        }
        // check devID not used, and type limits
        if (newDev instanceof Sensor) {
            for (Sensor s : sensorList) {
                if (s.getDevID().equals(devID)) {
                    System.out.println("Error: devID " + devID + " already used by a Sensor.");
                    return;
                }
            }
            if (sensorList.size() >= maxSensors) {
                System.out.println("Error: Sensor limit exceeded!");
                return;
            }
        } 
        else if (newDev instanceof Display) {
            for (Display d : displayList) {
                if (d.getDevID().equals(devID)) {
                    System.out.println("Error: devID " + devID + " already used by a Display.");
                    return;
                }
            }
            if (displayList.size() >= maxDisplays) {
                System.out.println("Error: Display limit exceeded!");
                return;
            }
        }
        else if (newDev instanceof WirelessIO) {
            for (WirelessIO w : wirelessList) {
                if (w.getDevID().equals(devID)) {
                    System.out.println("Error: devID " + devID + " already used by a Wireless device.");
                    return;
                }
            }
            if (wirelessList.size() >= maxWireless) {
                System.out.println("Error: WirelessIO limit exceeded!");
                return;
            }
        }
        else if (newDev instanceof MotorDriver) {
            for (MotorDriver m : motorList) {
                if (m.getDevID().equals(devID)) {
                    System.out.println("Error: devID " + devID + " already used by a MotorDriver.");
                    return;
                }
            }
            if (motorList.size() >= maxMotors) {
                System.out.println("Error: MotorDriver limit exceeded!");
                return;
            }
        }
        // attach
        port.attachedDevice = newDev;
        // add to correct list
        if (newDev instanceof Sensor) {
            sensorList.add((Sensor)newDev);
        } else if (newDev instanceof Display) {
            displayList.add((Display)newDev);
        } else if (newDev instanceof WirelessIO) {
            wirelessList.add((WirelessIO)newDev);
        } else if (newDev instanceof MotorDriver) {
            motorList.add((MotorDriver)newDev);
        }
        System.out.println("Device " + devName + " devID=" + devID + " added to port " + portID);
    }

    // rmDev <portID>, fail if device is ON
    public void rmDev(int portID) {
        if (!checkPortID(portID)) return;
        Port port = ports.get(portID);
        if (port.isEmpty()) {
            System.out.println("Error: Port " + portID + " is empty.");
            return;
        }
        Device dev = port.attachedDevice;
        if (dev.getState() == State.ON) {
            // match the example
            System.out.println("Device is active.");
            System.out.println("Command failed.");
            return;
        }
        // remove from type list
        if (dev instanceof Sensor) {
            sensorList.remove(dev);
        } else if (dev instanceof Display) {
            displayList.remove(dev);
        } else if (dev instanceof WirelessIO) {
            wirelessList.remove(dev);
        } else if (dev instanceof MotorDriver) {
            motorList.remove(dev);
        }
        port.attachedDevice = null;
    }

    // turnON <portID>
    public void turnOnDevice(int portID) {
        if (!checkPortOccupied(portID)) return;
        Device dev = ports.get(portID).attachedDevice;
        dev.turnON(); 
        // example output: "DHT11: Turning ON" + "OneWire: Writing "turnON"."
    }

    // turnOFF <portID>
    public void turnOffDevice(int portID) {
        if (!checkPortOccupied(portID)) return;
        Device dev = ports.get(portID).attachedDevice;
        dev.turnOFF();
    }

    private boolean checkPortID(int portID) {
        if (portID < 0 || portID >= ports.size()) {
            System.out.println("Error: Invalid portID " + portID);
            return false;
        }
        return true;
    }

    private boolean checkPortOccupied(int portID) {
        if (!checkPortID(portID)) return false;
        if (ports.get(portID).isEmpty()) {
            System.out.println("Error: Port " + portID + " is empty.");
            return false;
        }
        return true;
    }

    // list ports
    public void listPorts() {
        System.out.println("list of ports");
        for (int i = 0; i < ports.size(); i++) {
            Port p = ports.get(i);
            if (p.isEmpty()) {
                System.out.println(i + " " + p.protocol.getProtocolName() + " empty");
            } else {
                Device d = p.attachedDevice;
                System.out.println(i + " " + p.protocol.getProtocolName() + " occupied "
                    + d.getName() + " " + d.getDevType() + " " + d.getDevID() + " " + d.getState());
            }
        }
    }

    // list <devType>
    public void listDevices(String devType) {
        switch(devType) {
            case "Sensor":
                System.out.println("list of Sensors");
                for (Sensor s : sensorList) {
                    int pid = findPortOfDevice(s);
                    System.out.println(s.getName() + " " + s.getDevID() + " " + pid + " " + s.getProtocol().getProtocolName());
                }
                break;
            case "Display":
                System.out.println("list of Displays");
                for (Display d : displayList) {
                    int pid = findPortOfDevice(d);
                    System.out.println(d.getName() + " " + d.getDevID() + " " + pid + " " + d.getProtocol().getProtocolName());
                }
                break;
            case "WirelessIO":
                System.out.println("list of WirelessIOs");
                for (WirelessIO w : wirelessList) {
                    int pid = findPortOfDevice(w);
                    System.out.println(w.getName() + " " + w.getDevID() + " " + pid + " " + w.getProtocol().getProtocolName());
                }
                break;
            case "MotorDriver":
                System.out.println("list of MotorDrivers");
                for (MotorDriver m : motorList) {
                    int pid = findPortOfDevice(m);
                    System.out.println(m.getName() + " " + m.getDevID() + " " + pid + " " + m.getProtocol().getProtocolName());
                }
                break;
            default:
                System.out.println("Error: Unknown device type -> " + devType);
        }
    }

    private int findPortOfDevice(Device dev) {
        for (int i = 0; i < ports.size(); i++) {
            if (ports.get(i).attachedDevice == dev) {
                return i;
            }
        }
        return -1;
    }

    // readSensor <devID>
    // If OFF error messagr
    public void readSensor(String devID) {
        for (Sensor s : sensorList) {
            if (s.getDevID().equals(devID)) {
                if (s.getState() == State.OFF) {
                    System.out.println("Device is not active.");
                    System.out.println("Command failed.");
                    return;
                }
                // e.g. "OneWire: Reading." + "DHT11 TempSensor Sensor: Temp: 0.83"
                System.out.println(s.getName() + " " + s.getDevType() + ": " + s.data2String());
                return;
            }
        }
        System.out.println("Error: No Sensor found with devID=" + devID);
    }

    // printDisplay <devID> <String>
    public void printDisplay(String devID, String text) {
        for (Display d : displayList) {
            if (d.getDevID().equals(devID)) {
                if (d.getState() == State.OFF) {
                    System.out.println("Device is not active.");
                    System.out.println("Command failed.");
                    return;
                }
                d.printData(text);
                return;
            }
        }
        System.out.println("Error: No Display found with devID=" + devID);
    }

    // setMotorSpeed <devID> <speed>
    public void setMotorSpeed(String devID, int speed) {
        for (MotorDriver m : motorList) {
            if (m.getDevID().equals(devID)) {
                if (m.getState() == State.OFF) {
                    System.out.println("Device is not active.");
                    System.out.println("Command failed.");
                    return;
                }
                m.setMotorSpeed(speed);
                return;
            }
        }
        System.out.println("Error: No MotorDriver found with devID=" + devID);
    }

    // readWireless <devID>
    public void readWireless(String devID) {
        for (WirelessIO w : wirelessList) {
            if (w.getDevID().equals(devID)) {
                if (w.getState() == State.OFF) {
                    System.out.println("Device is not active.");
                    System.out.println("Command failed.");
                    return;
                }
                String r = w.recvData();
                System.out.println("Wireless Received: " + r);
                return;
            }
        }
        System.out.println("Error: No Wireless device found with devID=" + devID);
    }

    // writeWireless <devID> <String>
    public void writeWireless(String devID, String text) {
        for (WirelessIO w : wirelessList) {
            if (w.getDevID().equals(devID)) {
                if (w.getState() == State.OFF) {
                    System.out.println("Device is not active.");
                    System.out.println("Command failed.");
                    return;
                }
                w.sendData(text);
                return;
            }
        }
        System.out.println("Error: No Wireless device found with devID=" + devID);
    }
}
