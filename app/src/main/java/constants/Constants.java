package constants;

import goods.*;
import world.State;

public class Constants {
	public static final double STUPIDITY_EFFECT_CONSTANT = 10; //how much does stupidity effect stupid choises?
	
	//TODO: UPDATE WHEN GOODS ADDED
	public static final int AMOUNT_OF_GOODS = 10; //should be one larger than the last index
	
	public static final int WHEAT = 0;
	public static final int COTTON = 1;
	public static final int CLOTHING = 2;
	public static final int TIMBER = 3;
	public static final int FURNUATURE = 4;
	public static final int IRON = 5;
	public static final int STEEL = 6;
	public static final int PAPER = 7;
    public static final int ANIMAL = 8;
	public static final int COAL = 9;

	//TODO: UPDATE ME AND MY FRIEND DOWN SOUTH TOO
	public static String GoodToString(int goodIndex) {
        return switch (goodIndex) {
            case WHEAT -> "wheat";
            case COTTON -> "cotton";
            case CLOTHING -> "clothing";
            case TIMBER -> "timber";
            case FURNUATURE -> "furnuature";
            case IRON -> "iron";
            case STEEL -> "steel";
            case PAPER -> "paper";
            case ANIMAL -> "animal";
            case COAL -> "coal";
            default -> "unobtanium";
        };
	}
	
	//TODO: UPDATE ME TOO
	public static AbstractGood getGood(double amount, State originState, int constant) {
        return switch (constant) {
            case WHEAT -> new Wheat(amount, originState);
            case COTTON -> new Cotton(amount, originState);
            case CLOTHING -> new Clothing(amount, originState);
            case TIMBER -> new Timber(amount, originState);
            case FURNUATURE -> new Furnuature(amount, originState);
            case IRON -> new Iron(amount, originState);
            case STEEL -> new Steel(amount, originState);
            case PAPER -> new Paper(amount, originState);
            case ANIMAL -> new Animal(amount, originState);
            case COAL -> new Coal(amount, originState);
            default -> {
                System.out.println("ERROR, CONSTANTS OF GOODS ARE NOT GOOD!!!");
                yield new Wheat(amount, originState);
            }
        };
	}

	// jobs
	public final static int SERF = 9;
	public final static int SLAVE = 10;
	public final static int FARMER = 0;
	public final static int LABORER = 1;
	public final static int SOLDIER = 2;
	public final static int ARTISAN = 3;
	public final static int CRAFTSMAN = 4;
	public final static int MERCHANT = 5;
	public final static int CAPITALIST = 6;
	public final static int CLERGYMAN = 7;
	public final static int ARISTOCRAT = 8;
	public final static int OFFICER = 11;

	public static String JobToString(int job) {
        return switch (job) {
            case FARMER -> "farmer";
            case LABORER -> "laborer";
            case SOLDIER -> "soldier";
            case ARTISAN -> "artisan";
            case CRAFTSMAN -> "craftsman";
            case MERCHANT -> "merchant";
            case CAPITALIST -> "capitalist";
            case CLERGYMAN -> "clergyman";
            case ARISTOCRAT -> "aristocrat";
            case OFFICER -> "officer";
            case SERF -> "serf";
            case SLAVE -> "slave";
            default -> "invalid job";
        };
	}

	public static int jobToClass(int job) {
        return switch (job) {
            case CAPITALIST -> UPPER_STRATA;
            case ARISTOCRAT -> UPPER_STRATA;
            case OFFICER -> MIDDLE_STRATA;
            case ARTISAN -> MIDDLE_STRATA;
            case MERCHANT -> MIDDLE_STRATA;
            case CLERGYMAN -> MIDDLE_STRATA;
            case FARMER -> LOWER_STRATA;
            case LABORER -> LOWER_STRATA;
            case CRAFTSMAN -> LOWER_STRATA;
            case SOLDIER -> LOWER_STRATA;
            case SERF -> LOWEST_STRATA;
            case SLAVE -> LOWEST_STRATA;
            default -> LOWEST_STRATA;
        };
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
        return switch (religion) {
            case CATHOLIC -> "catholic";
            case PROTESTANT -> "protestant";
            case ORTHODOX -> "orthodox";
            case JEWISH -> "jewish";
            case SUNNI -> "sunni";
            case PAGAN -> "pagan";
            default -> "invalid religion";
        };
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
	public final static int SUB_SAHARAN = 9;
	public final static int NATIVE_AMERICAN = 10;

	public static final double TRADE_MARGIN_MERCHANT_CONSTANT = 0.9; //1 - this = what he's left with.

	public static String raceToString(int race) {
        return switch (race) {
            case GERMANIC -> "Germanic";
            case NORDIC -> "Nordic";
            case FINNIC -> "Finnic";
            case MEDETERANIAN -> "Medeteranian";
            case SLAV -> "Slav";
            case ASHKERNAZI -> "Ashkenazi";
            case EAST_ASIAN -> "East Asian";
            case SOUTH_ASIAN -> "South Asian";
            case ARABIAN -> "Arabian";
            case SUB_SAHARAN -> "Black";
            case NATIVE_AMERICAN -> "Native American";
            default -> "Alien";
        };
	}
}
