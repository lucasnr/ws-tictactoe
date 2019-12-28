package io.github.lucasnr.tictactoe.model;

public class PlayerQuitedMessage extends Message {

	public PlayerQuitedMessage() {
		super("The other played just quited", MessageCode.PLAYER_QUITED);
	}

}
