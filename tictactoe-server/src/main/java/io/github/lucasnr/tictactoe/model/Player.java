package io.github.lucasnr.tictactoe.model;

import javax.websocket.Session;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {

	private Session session;
	private String playerChar;
	
}
