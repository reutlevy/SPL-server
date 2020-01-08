package bgu.spl.net.srv;

public class Connectionsimpl implements Connections {
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
