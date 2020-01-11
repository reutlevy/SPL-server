package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.newsfeed.FetchNewsCommand;
import bgu.spl.net.impl.newsfeed.NewsFeed;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RCIClient;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {

        
      Server.threadPerClient(
               7777, //port
                StompProtocol::new, //protocol factory
               StompEncoderDecoder::new //message encoder decoder factory
     ).serve();

      Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                 StompProtocol::new, //protocol factory
                StompEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}
