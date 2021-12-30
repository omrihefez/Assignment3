package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

public class EncoderDecoder implements MessageEncoderDecoder {
    @Override
    public Object decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }
}
