import java.util.LinkedList;

import world.State;

public class Germany {

	LinkedList<State> states;
	
	
	public Germany(int states) {
		for (int i = 0; i < states; i++) {
			//this.states.add(new State("GermanState "+i/*, Nation*/));
		}
	}
	public String getInfo() {
		String string = "";
		
		for (int i = 0; i < states.size(); i++) {
			string += states.get(i).getInfo();
		}
		
		
		
		return string;
	}
}
