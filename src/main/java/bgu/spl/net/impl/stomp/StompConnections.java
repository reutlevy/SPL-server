package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class StompConnections implements Connections {
    @Override
    public boolean send(int connectionId, Object msg) {
        return false;
    }

    @Override
    public void send(String channel, Object msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
