package Main;

import java.util.Scanner;

/*
 * we read config as args
 * after that start HWSystem
 * take commands and redirect to HWSystem
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -cp build Main.Main <configFile>");
            return;
        }
        String configFile = args[0];
        HWSystem system = new HWSystem(configFile);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (!sc.hasNextLine()) {
                break;
            }
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            String[] tokens = line.split(" ");
            String cmd = tokens[0];

            try {
                switch(cmd) {
                    case "turnON":
                        // turnON <portID>
                        if (tokens.length < 2) {
                            System.out.println("Error: Usage: turnON <portID>");
                            break;
                        }
                        int onID = Integer.parseInt(tokens[1]);
                        system.turnOnDevice(onID);
                        break;
                    case "turnOFF":
                        // turnOFF <portID>
                        if (tokens.length < 2) {
                            System.out.println("Error: Usage: turnOFF <portID>");
                            break;
                        }
                        int offID = Integer.parseInt(tokens[1]);
                        system.turnOffDevice(offID);
                        break;
                    case "addDev":
                        // addDev <devName> <portID> <devID>
                        if (tokens.length < 4) {
                            System.out.println("Error: Usage: addDev <devName> <portID> <devID>");
                            break;
                        }
                        String devName = tokens[1];
                        int portID = Integer.parseInt(tokens[2]);
                        String devID = tokens[3];
                        system.addDev(devName, portID, devID);
                        break;
                    case "rmDev":
                        // rmDev <portID>
                        if (tokens.length < 2) {
                            System.out.println("Error: Usage: rmDev <portID>");
                            break;
                        }
                        int rmID = Integer.parseInt(tokens[1]);
                        system.rmDev(rmID);
                        break;
                    case "list":
                        // list ports yada list <devType>
                        if (tokens.length < 2) {
                            System.out.println("Error: Usage: list <ports|Sensor|Display|WirelessIO|MotorDriver>");
                            break;
                        }
                        String what = tokens[1];
                        if (what.equalsIgnoreCase("ports")) {
                            system.listPorts();
                        } else {
                            system.listDevices(what);
                        }
                        break;
                    case "readSensor":
                        // readSensor <devID>
                        if (tokens.length < 2) {
                            System.out.println("Error: Usage: readSensor <devID>");
                            break;
                        }
                        system.readSensor(tokens[1]);
                        break;
                    case "printDisplay":
                        // printDisplay <devID> <string...>
                        if (tokens.length < 3) {
                            System.out.println("Error: Usage: printDisplay <devID> <text...>");
                            break;
                        }
                        String dispID = tokens[1];
                        StringBuilder sb = new StringBuilder();
                        for (int i = 2; i < tokens.length; i++) {
                            sb.append(tokens[i]).append(" ");
                        }
                        system.printDisplay(dispID, sb.toString().trim());
                        break;
                    case "setMotorSpeed":
                        // setMotorSpeed <devID> <integer>
                        if (tokens.length < 3) {
                            System.out.println("Error: Usage: setMotorSpeed <devID> <speed>");
                            break;
                        }
                        String mDevID = tokens[1];
                        int speed = Integer.parseInt(tokens[2]);
                        system.setMotorSpeed(mDevID, speed);
                        break;
                    case "readWireless":
                        // readWireless <devID>
                        if (tokens.length < 2) {
                            System.out.println("Error: Usage: readWireless <devID>");
                            break;
                        }
                        system.readWireless(tokens[1]);
                        break;
                    case "writeWireless":
                        // writeWireless <devID> <text...>
                        if (tokens.length < 3) {
                            System.out.println("Error: Usage: writeWireless <devID> <text...>");
                            break;
                        }
                        String wDevID = tokens[1];
                        StringBuilder sb2 = new StringBuilder();
                        for (int i = 2; i < tokens.length; i++) {
                            sb2.append(tokens[i]).append(" ");
                        }
                        system.writeWireless(wDevID, sb2.toString().trim());
                        break;
                    default:
                        System.out.println("Error: Unknown command -> " + cmd);
                }
            } catch(Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }
}
