package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.stomp.Frames.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;


public class StompEncoderDecoder implements MessageEncoderDecoder<StompFrame> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public StompFrame decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\u0000') {
            //System.out.println("end of bytes!!!!" + popString());
            return popString();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(StompFrame message) {

        //region debug
        byte[] bytes1 = (message.toString() + "\u0000").getBytes();
        System.out.println("------------------send : -------------------");
        for (byte aByte : bytes1) {
            System.out.print((int) aByte + ", ");
        }
        System.out.println();
        for (byte aByte : bytes1) {
            System.out.print(b2s(aByte, true) + ", ");
        }
        System.out.println();
        for (byte aByte : bytes1) {
            System.out.print(b2s(aByte, false));
        }
        System.out.println();
        System.out.println("______-------------------________");
        //endregion
        return (message.toString() + "\u0000").getBytes(); //TODO find out about ^@
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private StompFrame popString() {
        String translate = new String(bytes, 0, len, StandardCharsets.UTF_8);
        translate=translate.replaceAll("\n ","\n");
        //region debug
        byte[] bytes1 = translate.getBytes();
        System.out.println("------------------get  : -------------------");
        for (byte aByte : bytes1) {
            System.out.print((int) aByte + ", ");
        }
        System.out.println();
        for (byte aByte : bytes1) {
            System.out.print(b2s(aByte, true) + ", ");
        }
        System.out.println();
        for (byte aByte : bytes1) {
            System.out.print(b2s(aByte, false));
        }
        System.out.println();
        System.out.println("______-------------------________");
        //endregion

        // System.out.println(translate);
        //region initialize
        StompFrame result=null;
        len = 0;
        String[] lines = translate.split("\n");

        ConcurrentHashMap<String, String> message1 = new ConcurrentHashMap<>();
        //endregion

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].contains(":")) {
                String[] header = lines[i].split(":");
                message1.put(header[0], header[1]);
            } else {
                message1.put("body", lines[i]);
            }
        }

        ConcurrentHashMap<String, String> message = new ConcurrentHashMap<>(message1);
        int size0=message.size();
        switch (lines[0]) {
            case "MESSAGE":
                result = new MESSAGE(message);
                break;
            case "CONNECT":
                result = new CONNECT(message);
                break;
            case "DISCONNECT":
                result = new DISCONNECT(message);
                break;
            case "ERROR":
                result = new ERROR(message);
                break;
            case "RECEIPT":
                result = new RECEIPT(message);
                break;
            case "SEND":
                result = new SEND(message);
                break;
            case "SUBSCRIBE":
                result = new SUBSCRIBE(message);
                break;
            case "UNSUBSCRIBE":
                result = new UNSUBSCRIBE(message);
                break;
            default:
                return null;
        }
        int size1=message.size();
        result.setHeaders(message1);
        int size2=message.size();
        int size3 = result.headers.size();
        System.out.println("AAAAAAAAAA: "+size0+", "+size1 + ", " + size2 + "," + size3);
        //System.out.println("the result issss" + result.toString());
        return result;
    }


    //region debug
    String b2s(byte b, boolean spaces) {
        byte[] bytes = {b};
        String s = new String(bytes);
        if (spaces) {
            s = " " + s;
            if (b > 99)
                s = " " + s;
        }
        switch (b) {
            case '\n':
                return "\\n";
            case '\r':
                return "\\r";
            case '\0':
                return "\\0";
            default:
                return s;
        }
    }

    //endregion
}
