import goods.Wheat;
import org.junit.jupiter.api.Test;
import world.State;

import static org.junit.jupiter.api.Assertions.assertSame;

public class GoodOriginTest {

    @Test
    public void goodsExposeTheStateTheyWereCreatedIn() {
        State origin = new State();
        Wheat wheat = new Wheat(1, origin);

        assertSame(origin, wheat.getOriginState());
    }
}