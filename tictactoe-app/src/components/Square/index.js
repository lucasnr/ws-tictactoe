import React from 'react';

import './styles.css';

export default class Square extends React.Component {

  constructor(props) {
    super(props);
    this.state = { pressed: false };
  }

  _action() {
    const { avaliable, callback } = this.props;
    if(this.state.pressed || ! avaliable)
      return;

    callback();
    this.setState({ pressed: true });
  }

  render() {
    let { char, avaliable } = this.props;

    if(! avaliable) { //means the other player already pressed
      char = char === "X" ? "O" : "X";
    }
    
    const { pressed } = this.state; 
    return (
      <button onClick={this._action.bind(this)} className="square"
        style={{ cursor: ( ! pressed && avaliable ) ? "pointer" : "auto" }}>
        {( pressed || ! avaliable ) && 
          <span className="square__content" style={{color: char === "X" ? "blue" : "crimson"}}>{char}</span>
        }
      </button>
    );
  }
}