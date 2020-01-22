const path = 'ws://192.168.0.103:8080/tictactoe';

export const initSocket = () => {
  const gameRoomCode = document.location.pathname;
  const url = `${path}/game${gameRoomCode}`;

  return new WebSocket(url);
}
