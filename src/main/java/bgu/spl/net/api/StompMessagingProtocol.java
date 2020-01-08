package bgu.spl.net.api;

import bgu.spl.net.impl.stomp.StompFrame;
import bgu.spl.net.srv.Connections;

public interface StompMessagingProtocol<StompFrame>  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, Connections<String> connections);
    
    void process(StompFrame message);
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
