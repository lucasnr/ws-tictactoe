package io.github.lucasnr.tictactoe.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class PlayerMoveMessage extends Message {

	private Integer player;
	private Integer position;
	@SerializedName("player_char")
	private char playerChar;
	
	public PlayerMoveMessage(String text) {
		super(text, MessageCode.PLAYER_MOVE);
	}

}
