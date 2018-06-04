import React, {Component} from 'react';

export default class EditableText extends Component {
    nameInput;

    constructor(props) {
        super(props);
        this.state = {
            type: "div",
            value: this.props.value
        }
    }

    changeState() {
        if (this.state.type === "div") {
            this.setState({type: "input"});
        } else {
            this.setState({type: "div"})
        }
    }

    render() {
        if (this.state.type === "div") {
            return <div onClick={this.changeState.bind(this)}>{this.state.value}</div>
        } else {
            return <input type="text"
                          autoFocus={true}
                          onBlur={() => {
                              this.props.callback(this.state.value);
                              this.changeState();
                          }}
                          value={this.state.value}
                          onChange={(e) => this.setState({
                              value: e.target.value
                          })}
            />
        }

    }
}