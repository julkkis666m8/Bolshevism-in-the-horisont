package constants;

public class Constants {
	
	
	
	//TODO: UPDATE WHEN GOODS ADDED
	public static final int AMOUNT_OF_GOODS = 3;
	
	public static final int WHEAT = 0;
	public static final int IRON = 1;
	public static final int STEEL = 2;
	
	
	

	// genders (are binary)
	public final static int MALE = 1;
	public final static int FEMALE = 0;
	


	public static String sexToString(int sex) {

		String title = "";

		switch (sex) {
		case MALE:
			title = "male";
			break;
		case FEMALE:
			title = "female";
			break;
		default:
			title = "GENDER IS BINARY"; //can't happen
			break;
		}

		return title;
	}
	

	// jobs
	public final static int SERF = 9;
	public final static int SLAVE = 10;
	public final static int FARMER = 0;
	public final static int LABORER = 1;
	public final static int SOLDIER = 2;
	public final static int ARTISAN = 3;
	public final static int CRAFTSMAN = 4;
	public final static int CLERK = 5;
	public final static int CAPITALIST = 6;
	public final static int CLERGYMAN = 7;
	public final static int ARISTOCRAT = 8;
	public final static int OFFICER = 11;
	

	public static String JobToString(int job) {

		String title = "";

		switch (job) {
		case FARMER:
			title = "farmer";
			break;
		case LABORER:
			title = "laborer";
			break;
		case SOLDIER:
			title = "soldier";
			break;
		case ARTISAN:
			title = "artisan";
			break;
		case CRAFTSMAN:
			title = "craftsman";
			break;
		case CLERK:
			title = "clerk";
			break;
		case CAPITALIST:
			title = "capitalist";
			break;
		case CLERGYMAN:
			title = "clergyman";
			break;
		case ARISTOCRAT:
			title = "aristocrat";
			break;
		case SERF:
			title = "serf";
			break;
		case SLAVE:
			title = "slave";
			break;
		default:
			title = "invalid job";
			break;
		}

		return title;
	}
	public static int jobToClass(int job) {

		int strata = 0;

		switch (job) {
		case CAPITALIST:
			strata = UPPER_STRATA;
			break;
		case ARISTOCRAT:
			strata = UPPER_STRATA;
			break;
		case OFFICER:
			strata = MIDDLE_STRATA;
			break;
		case ARTISAN:
			strata = MIDDLE_STRATA;
			break;
		case CLERK:
			strata = MIDDLE_STRATA;
			break;
		case CLERGYMAN:
			strata = MIDDLE_STRATA;
			break;
		case FARMER:
			strata = LOWER_STRATA;
			break;
		case LABORER:
			strata = LOWER_STRATA;
			break;
		case CRAFTSMAN:
			strata = LOWER_STRATA;
			break;
		case SOLDIER:
			strata = LOWER_STRATA;
			break;
		case SERF:
			strata = LOWEST_STRATA;
			break;
		case SLAVE:
			strata = LOWEST_STRATA;
			break;
		default:
			strata = LOWEST_STRATA;
			break;
		}
		

		return strata;
	}
	
	//Strata

	public final static int UPPER_STRATA = 0;
	public final static int MIDDLE_STRATA = 1;
	public final static int LOWER_STRATA = 2;
	public final static int LOWEST_STRATA = 3;
	
	
	
	

	// religions
	// TODO: add more
	public final static int CATHOLIC = 0;
	public final static int PROTESTANT = 1;
	public final static int ORTHODOX = 2;
	public final static int JEWISH = 3;
	public final static int SUNNI = 4;
	public final static int PAGAN = 5;
	


	public static String ReligionToString(int religion) {

		String title = "";

		switch (religion) {
		case CATHOLIC:
			title = "catholic";
			break;
		case PROTESTANT:
			title = "protestant";
			break;
		case ORTHODOX:
			title = "orthodox";
			break;
		case JEWISH:
			title = "jewish";
			break;
		case SUNNI:
			title = "sunni";
			break;
		case PAGAN:
			title = "pagan";
			break;
		default:
			title = "invalid religion";
			break;
		}

		return title;
	}

	// race
	// TODO: add more
	public final static int GERMANIC = 0;
	public final static int NORDIC = 1;
	public final static int FINNIC = 2;
	public final static int MEDETERANIAN = 3;
	public final static int SLAV = 4;
	public final static int ASHKERNAZI = 5;
	public final static int EAST_ASIAN = 6;
	public final static int SOUTH_ASIAN = 7;
	public final static int ARABIAN = 8;
	public final static int BLACK = 9;
	public final static int NATIVE_AMERICAN = 10;

	public static String raceToString(int race) {

		String title = "";

		switch (race) {
		case GERMANIC:
			title = "Germanic";
			break;
		case NORDIC:
			title = "Nordic";
			break;
		case FINNIC:
			title = "Finnic";
			break;
		case MEDETERANIAN:
			title = "Medeteranian";
			break;
		case SLAV:
			title = "Slav";
			break;
		case ASHKERNAZI:
			title = "Ashkenazi";
			break;
		case EAST_ASIAN:
			title = "East Asian";
			break;
		case SOUTH_ASIAN:
			title = "South Asian";
			break;
		case ARABIAN:
			title = "Arabian";
			break;
		case BLACK:
			title = "Black";
			break;
		case NATIVE_AMERICAN:
			title = "Native American";
			break;
		default:
			title = "Alien";
			break;
		}

		return title;
	}
	
}
