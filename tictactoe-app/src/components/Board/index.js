import React from 'react';

import Square from '../Square';
import { initSocket } from '../../services/socket';

import './styles.css';

export default class Board extends React.Component {

  constructor() {
    super();
    this.state = {
      otherPlayerMoves: [],
      pointerEvents: "none",
    }
  }

  componentDidMount() {
    this._socket = initSocket();
    this._socket.onmessage = ({ data }) => {
      const response = JSON.parse(data);
      console.log(response); // famoso logger

      if(response.code === "INFORMATIONAL") {
        this.setState({ message: response.text });
      } else if(response.code === "GAMESTART") {
        const { text, player_char } = response;
        this.setState({ message: text, char: player_char, player: player_char === "X" ? 1 : 2 });
      } else if(response.code === "PLAYER_QUITED") {
        this.setState({ message: response.text, pointerEvents: "none" });
      } else if(response.code === "YOUR_TURN") {
        this.setState({ message: response.text, pointerEvents: "auto" });
      } else if(response.code === "PLAYER_MOVE") {
        const otherPlayerMove = response.position;
        let { otherPlayerMoves } = this.state;
        otherPlayerMoves.push(otherPlayerMove);
        this.setState({ otherPlayerMoves }); // say what now?
      }
    }
  }

  _clickAction(position) {
    const { player, char } = this.state;
    this._socket.send(JSON.stringify({
      player,
      position,
      player_char: char,
      text: `Player ${char} just played on position ${position}`,
      code: "PLAYER_MOVE"
    }));
    this.setState({ pointerEvents: "none" });
  }

  render() {
    const { message, pointerEvents, char, otherPlayerMoves } = this.state;

    const squares = [];
    for (let index = 0; index < 9; index++) {
      const avaliable = ! otherPlayerMoves.some(item => (index + 1) === item);
      squares[index] = (
        <Square key={index} avaliable={avaliable} 
          callback={() => this._clickAction(index + 1)} char={char} />
      )
    }

    return (
      <div className="board" style={{pointerEvents: pointerEvents}}>
        <div className="messages">{message}</div>
        {squares}
      </div>
    );
  }
}
