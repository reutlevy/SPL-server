package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;


public class StompEncoderDecoder implements MessageEncoderDecoder<StompFrame> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public StompFrame decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\u0000') {
            return popString();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(StompFrame message) {
        return (message + "\u0000").getBytes();
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private StompFrame popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String translate = new String(bytes, 0, len, StandardCharsets.UTF_8);
        StompFrame result;
        len = 0;
        String lines[] = translate.split("\n");
        HashMap <String,String> message=new HashMap<>();
        for (int i=1 ;i<lines.length; i++) {
            while (lines[i] != "") {
                String header[] = lines[i].split(":");
                message.put(header[0], header[1]);
            }
            message.put("body",lines[i+1]);
            break;
        }
      if(lines[0].equals("MESSAGE"))
         result=new MESSAGE(lines[0],message);
      else if(lines[0].equals("CONNECT"))
          result=new CONNECT(lines[0],message);
      else if(lines[0].equals("DISCONNECT"))
          result=new DISCONNECT(lines[0],message);
      else if(lines[0].equals("ERROR"))
          result=new ERROR(lines[0],message);
      else if(lines[0].equals("RECEIPT"))
          result=new RECEIPT(lines[0],message);
      else if(lines[0].equals("SEND"))
          result=new SEND(lines[0],message);
      else if(lines[0].equals("SUBSCRIBE"))
          result=new SUBSCRIBE(lines[0],message);
      else if(lines[0].equals("UNSUBSCRIBE"))
          result=new UNSUBSCRIBE(lines[0],message);
      else
        result=null;

      return result;
    }
}
