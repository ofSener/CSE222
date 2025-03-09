package protocols;

/*
    all the protocols connected the system here.
    
 */
public interface Protocol {
    //protocol name
    String getProtocolName();
    //read sim
    String read();
    //write sim
    void write(String data);
}
