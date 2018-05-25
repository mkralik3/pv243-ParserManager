package cz.fi.muni.pv243.ws.service;

import java.io.IOException;
import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.ParserConfiguration;

/**
 * This class is responsible to encode the Parser object in a String that will be sent to clients
 * @author windows_sucks
 *
 */
public class ParserConfigurationEncoder implements Encoder.Text<List<ParserConfiguration>> {

	private ObjectMapper objectMapper;
	
	@Override
	public void init(EndpointConfig config) {
		objectMapper = new ObjectMapper();
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(List<ParserConfiguration> object) throws EncodeException {
		try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EncodeException(object, e.getMessage(), e);
        } catch (IOException e) {
        	throw new EncodeException(object, e.getMessage(), e);
		}
	}

}
