package cz.fi.muni.pv243.ws.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class ActionMessageDecoder implements Decoder.Text<ActionMessage> {

    private ObjectMapper objectMapper;

    @Override
    public void init(EndpointConfig config) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void destroy() {
    }

    @Override
    public ActionMessage decode(String object) throws DecodeException {
        try {
            return objectMapper.readValue(object, ActionMessage.class);
        } catch (JsonParseException e) {
            throw new DecodeException(object, e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new DecodeException(object, e.getMessage(), e);
        } catch (IOException e) {
            throw new DecodeException(object, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
