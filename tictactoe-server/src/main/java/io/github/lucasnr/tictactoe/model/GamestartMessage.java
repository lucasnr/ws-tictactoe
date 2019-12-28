package io.github.lucasnr.tictactoe.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class GamestartMessage extends Message {

	@SerializedName(value = "player_char")
	private final String playerChar;
	
	public GamestartMessage(String playerChar) {
		super("Start!", MessageCode.GAMESTART);
		this.playerChar = playerChar;
	}

}
