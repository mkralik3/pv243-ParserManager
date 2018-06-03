package cz.fi.muni.pv243.ws.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fi.muni.pv243.entity.Parser;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for encoding the Parser object in a String that will be sent to clients
 * @author Michaela Bocanova
 *
 */
public class ParsersEncoder implements Encoder.Text<List<Parser>> {

    private ObjectMapper objectMapper;

    @Override
    public void init(EndpointConfig config) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(List<Parser> object) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EncodeException(object, e.getMessage(), e);
        } catch (IOException e) {
            throw new EncodeException(object, e.getMessage(), e);
        }
    }

}
