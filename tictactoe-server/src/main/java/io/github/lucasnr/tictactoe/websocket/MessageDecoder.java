package io.github.lucasnr.tictactoe.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import io.github.lucasnr.tictactoe.model.Message;

public class MessageDecoder implements Decoder.Text<Message> {

	private static Gson gson = new Gson();
	private Class<? extends Message> type;

	public MessageDecoder(Class<? extends Message> type) {
		this.type = type;
	}

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public Message decode(String s) throws DecodeException {
		return gson.fromJson(s, type);
	}

	@Override
	public boolean willDecode(String s) {
		return s != null;
	}

}
