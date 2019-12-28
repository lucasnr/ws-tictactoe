package io.github.lucasnr.tictactoe.model;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import lombok.Data;

@Data
public class Room {
    private String code;
    private boolean started;

    private Player playerOne;
    private Player playerTwo;
    
	public boolean isFull() {
		return playerOne != null && playerTwo != null;
	}

	public void addPlayer(Session session) {
		if (! playerExists(this.playerOne))
			this.playerOne = new Player(session, "X");
		else
			this.playerTwo = new Player(session, "O");
	}
	
	private boolean playerExists(Player player) {
		if(player == null)
			return false;
		
		return player.getSession() != null;
	}

	public void removePlayer(Session session) {
		for (Player player : players()) {
		
			if(playerExists(player)) {
				if(player.getSession().getId().equals(session.getId())) {
					player = null;
					return;
				}
			}
		}
	}
	
	public Player getRemainingPlayer() {
		if(playerExists(playerOne))
			return playerOne;
		else
			return playerTwo;
	}
	
	public List<Player> players() {
		ArrayList<Player> players = new ArrayList<>();
		players.add(playerOne);
		players.add(playerTwo);
		return players;
	}

	public Room(String code) {
		super();
		this.code = code;
	}


}
