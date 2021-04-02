package nationalEconomyManagers;

import constants.Constants;
import market.MerchantHandler;
import world.Nation;
import world.Pop;
import world.PopSellHandler;
import world.State;

public class PopJobHandler {
	
	public PopJobHandler() {
		
	}
	
	
	

	/**
	 * "ticks" a pop to make them do what their "jobs" do.
	 * @param nation
	 * @param state
	 */
	public void jobCounter(Nation nation, State state, Pop pop) {
		
		double income = 0;
		
		if (pop.job == Constants.CAPITALIST) {
			income = 10*pop.population;
		}
		else if (pop.job == Constants.CLERGYMAN) {
			double neededInc = nation.getCleregymanPay()*pop.population;
			income = nation.getNationCash(nation.getCleregymanPay()*pop.population);
			double effect = income/neededInc;
			state.AddEducation(pop.population, effect);
			state.convert(pop.population, effect);
		}
		else if (pop.job == Constants.MERCHANT) {
			income = MerchantHandler.wrangle(state, pop, nation);
		}
		else if (pop.job == Constants.CRAFTSMAN) {
			income = state.getCraftsmanPay(state.getCraftsmankWage()*pop.population);
		}
		else if (pop.job == Constants.ARTISAN) {
			//goodAdder(ArtesanJobs.artesanJob(this, state));
			main.Main.controller.jobDoer.doJob(pop, state, main.Main.controller.jobChoser.choseNeededJob(pop, state, main.Main.controller.artesanJobs));
			income = PopSellHandler.sell(pop, state.localMarket, nation);
		}
		else if (pop.job == Constants.SOLDIER) {
			income = nation.getSoldierPay().paySoldier(pop.population);
		}
		else if (pop.job == Constants.LABORER) {
			//goods.addAll(PopSellHandler.labourerJob(this, state));
			main.Main.controller.jobDoer.doJob(pop, state, main.Main.controller.jobChoser.choseLessEfficentJob(pop, state, main.Main.controller.labourJobs, 0.5 ));
			income = PopSellHandler.sell(pop, state.localMarket, nation);
			
			
			double ariMoney = income*0.1;
			income = income-ariMoney;
			
			state.getAristocratCashPool().giveMoneyToAristocrats(ariMoney);
			
		}
		else if (pop.job == Constants.FARMER) {
			//goods.addAll(PopSellHandler.farmerJob(this, state));
			main.Main.controller.jobDoer.doJob(pop, state, main.Main.controller.jobChoser.choseLessEfficentJob(pop, state, main.Main.controller.farmJobs, 0.1));
			income = PopSellHandler.sell(pop, state.localMarket, nation);
			
			
			double ariMoney = income*0.1;
			income = income-ariMoney;
			
			state.getAristocratCashPool().giveMoneyToAristocrats(ariMoney);
			
		}
		else if (pop.job == Constants.ARISTOCRAT) {
			income = state.getAristocratCashPool().payAristocrat(pop.population);
		}
		else if (pop.job == Constants.SERF) {

			//goods.addAll(PopSellHandler.serfJob(this, state));
			main.Main.controller.jobDoer.doJob(pop, state, main.Main.controller.jobChoser.choseEfficentJob(pop, state, main.Main.controller.farmJobs));
			income = PopSellHandler.sell(pop, state.localMarket, nation);
			
			
			double ariMoney = income*0.75;
			income = income-ariMoney;
			
			state.getAristocratCashPool().giveMoneyToAristocrats(ariMoney);
			
		}
		else if (pop.job == Constants.SLAVE) {

			//goods.addAll(PopSellHandler.serfJob(this, state));
			main.Main.controller.jobDoer.doJob(pop, state, main.Main.controller.jobChoser.choseEfficentJob(pop, state, main.Main.controller.farmJobs));
			income = PopSellHandler.sell(pop, state.localMarket, nation);
			
			
			double ariMoney = income*0.99;
			income = income-ariMoney;
			
			state.getAristocratCashPool().giveMoneyToAristocrats(ariMoney);
			
		}
		
		

		pop.setIncomeTaxable(income);
		pop.averageWealth = income/pop.population;
		
	}

}
