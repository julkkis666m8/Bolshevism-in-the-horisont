package nationalEconomyManagers;

import java.util.List;

import constants.Constants;
import world.Nation;
import world.Pop;
import world.State;

public class CapitalistPayPool extends AbstractPayPool{

	public CapitalistPayPool(Nation nation, State state) {
		super(nation, state);
	}

	@Override
	void updateTargetPop() {
		
		List<Pop> targetPops = this.state.getJob(Constants.CAPITALIST);
		targetPop = 0;
		
		for (Pop persons : targetPops) {
			targetPop += persons.population;
		}
	}

}
