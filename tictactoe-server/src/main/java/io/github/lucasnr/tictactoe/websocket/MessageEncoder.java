package io.github.lucasnr.tictactoe.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import io.github.lucasnr.tictactoe.model.Message;

public class MessageEncoder implements Encoder.Text<Message> {

	private static Gson gson = new Gson();

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(Message object) throws EncodeException {
		return gson.toJson(object);
	}

}
