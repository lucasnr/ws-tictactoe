package io.github.lucasnr.tictactoe.websocket;

import io.github.lucasnr.tictactoe.model.PlayerMoveMessage;

public class PlayerMoveMessageDecoder extends MessageDecoder {

	public PlayerMoveMessageDecoder() {
		super(PlayerMoveMessage.class);
	}

}
