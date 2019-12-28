import React from 'react';

// import { Container } from './styles';

export default class Board extends React.Component {

  constructor() {
    super();
    this.state = {
      pointerEvents: "none",
      otherPlayerMoves: [],
    }

    const code = document.location.pathname;
    this.ws = new WebSocket(`ws://192.168.0.103:8080/tictactoe/game${code}`);
    this.ws.onmessage = ({ data }) => {
      console.log(data); // famoso logger
      const response = JSON.parse(data);
      if(response.code === "INFORMATIONAL")
        this.setState({ message: response.text });
      else if(response.code === "GAMESTART")
        this.setState({ 
          message: response.text, 
          char: response.player_char, 
          player: response.player_char === "X" ? 1 : 2
        });
      else if(response.code === "PLAYER_QUITED")
        this.setState({ message: response.text, pointerEvents: "none" })
      else if(response.code === "YOUR_TURN")
        this.setState({ message: response.text, pointerEvents: "auto" })
      else if(response.code === "PLAYER_MOVE") {
        let { otherPlayerMoves } = this.state;
        otherPlayerMoves.push(response.position);
        this.setState({ otherPlayerMoves }); // say what now?
      }

    };
  }

  clickAction(position) {
    const { player, char } = this.state;
    this.ws.send(JSON.stringify({
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

      let avaliable = true;
      otherPlayerMoves.forEach(otherPlayerMove => {
        if ( (index + 1) === otherPlayerMove)
          avaliable = false;
      });
      squares[index] = (
        <Square key={index} avaliable={avaliable} 
          callback={() => this.clickAction(index+1)} char={char} />
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

class Square extends React.Component {

  constructor(props) {
    super(props);
    this.state = { pressed: false };
  }

  action() {
    if(this.state.pressed || ! this.props.avaliable)
      return;

    this.props.callback();
    this.setState({ pressed: true });
  }

  render() {
    let { char, avaliable } = this.props;
    
    if(! avaliable) {
      char = char === "X" ? "O" : "X";
    }
    
    return (
      <button onClick={this.action.bind(this)} className="square">
        {( this.state.pressed || ! avaliable ) && 
          <span className="squareContent">{char}</span>
        }
      </button>
    );
  }
}