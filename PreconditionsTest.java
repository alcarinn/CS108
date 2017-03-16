package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;

import org.junit.Test;

public class PreconditionsTest {
    ////////// checkArgument (1 argument)

    @Test
    public void checkArgument1SucceedsForTrue() {
        checkArgument(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkArgument1ThrowsForFalse() {
        checkArgument(false);
    }

    ////////// checkArgument (2 arguments)

    @Test
    public void checkArgument2SucceedsForTrue() {
        checkArgument(true, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkArgument2ThrowsForFalse() {
        checkArgument(false, "");
    }
}
