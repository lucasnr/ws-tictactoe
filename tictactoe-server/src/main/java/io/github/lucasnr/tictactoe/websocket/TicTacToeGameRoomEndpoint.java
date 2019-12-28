package io.github.lucasnr.tictactoe.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.github.lucasnr.tictactoe.model.GamestartMessage;
import io.github.lucasnr.tictactoe.model.InformationalMessage;
import io.github.lucasnr.tictactoe.model.Player;
import io.github.lucasnr.tictactoe.model.PlayerMoveMessage;
import io.github.lucasnr.tictactoe.model.PlayerQuitedMessage;
import io.github.lucasnr.tictactoe.model.Room;
import io.github.lucasnr.tictactoe.model.YourTurnMessage;

@ServerEndpoint(value = "/game/{code}", 
	decoders = { PlayerMoveMessageDecoder.class }, 
	encoders = { MessageEncoder.class })
public class TicTacToeGameRoomEndpoint {

	private static Map<String, Room> rooms = new HashMap<>();
	
    @OnOpen
    public void onOpen(Session session, @PathParam("code") String code) throws IOException, EncodeException {
    	Room room = findRoomOrCreateNew(code);
    	if(room.isStarted()) {    		
    		sendInformationalMessage(session, "This game room has already started");
    		session.close();
    		return;
    	}
    	
    	if (room.isFull()) {
    		sendInformationalMessage(session, "This game room is already full");
    		session.close();
    	}
    	else
    		room.addPlayer(session);
    	
    	checkIfGameCanStart(session, room);
    }

	private void checkIfGameCanStart(Session newPlayer, Room room) throws IOException, EncodeException {
		if(room.isFull()) {
			sendGamestartMessage(room);
			room.setStarted(true);
		}
    	else
    		sendInformationalMessage(newPlayer, "Wait for other player to join");
	}

    @OnMessage
    public void onMessage(Session session, PlayerMoveMessage message, @PathParam("code") String code) throws IOException, EncodeException {
    	Room room = rooms.get(code);
    	Integer playerNumber = message.getPlayer();
    	Player otherPlayer;
    	if (playerNumber == 1)
			otherPlayer = room.getPlayerTwo();
    	else
    		otherPlayer = room.getPlayerOne();
    	
    	sendInformationalMessage(session, "Await for the other player's move");
    	sendPlayerMoveMessage(otherPlayer, message);
    	sendYourTurnMessage(otherPlayer);
    }

	@OnClose
    public void onClose(Session session, @PathParam("code") String code) throws IOException, EncodeException {
    	Room room = rooms.get(code);
		room.removePlayer(session);
    	sendPlayerQuitedMessage(room);
    }

	@OnError
    public void onError(Session session, Throwable throwable) {
    }

    private void sendGamestartMessage(Room room) throws IOException, EncodeException {
    	for (Player player : room.players()) {
    		GamestartMessage message = new GamestartMessage(player.getPlayerChar());
			player.getSession().getBasicRemote().sendObject(message);
		}
    	
    	sendYourTurnMessage(room.getPlayerOne());
    }
    
    private void sendYourTurnMessage(Player player) throws IOException, EncodeException {
    	player.getSession().getBasicRemote().sendObject(new YourTurnMessage());
	}

    private void sendPlayerMoveMessage(Player player, PlayerMoveMessage message) throws IOException, EncodeException {
    	player.getSession().getBasicRemote().sendObject(message);
	}
    
	private void sendInformationalMessage(Session session, String message) throws IOException, EncodeException {    	
    	session.getBasicRemote().sendObject(new InformationalMessage(message));
	}
    
    private void sendPlayerQuitedMessage(Room room) throws IOException, EncodeException {
    	Player remainingPlayer = room.getRemainingPlayer();
    	if(remainingPlayer.getSession().isOpen())
    		remainingPlayer.getSession().getBasicRemote().sendObject(new PlayerQuitedMessage());
  	}
    
	private Room findRoomOrCreateNew(String code) {
		Optional<Room> optional = Optional.ofNullable(rooms.get(code));
    	Room room = optional.orElseGet(() -> {
    		Room gameRoom = new Room(code);
    		rooms.put(code, gameRoom);
    		return gameRoom;
    	});
		return room;
	}

}
