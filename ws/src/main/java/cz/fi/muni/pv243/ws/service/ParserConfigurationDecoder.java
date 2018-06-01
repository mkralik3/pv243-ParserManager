package cz.fi.muni.pv243.ws.service;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import cz.fi.muni.pv243.entity.Parser;

/**
 * This class is responsible to "decode" the received String from clients in a Message object
 * @author Michaela Bocanova
 *
 */
public class ParserConfigurationDecoder implements Decoder.Text<Parser> {

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public Parser decode(String msg) throws DecodeException {
		// It uses the JSON-P API to parse JSON content
        /*JsonReader reader = Json.createReader(new StringReader(msg));
        JsonObject jsonObject = reader.readObject();
        String command = jsonObject.getString("command");
        Long parserId = null;
        if (jsonObject.containsKey("parserId")) {
            parserId = jsonObject.getJsonNumber("parserId").longValue();
        }
        String xpath = jsonObject.getString("xpath");*/
        
        return null;
	}

	@Override
	public boolean willDecode(String s) {
		return true;
	}

}
