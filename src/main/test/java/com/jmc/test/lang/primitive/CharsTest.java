package com.jmc.test.lang.primitive;

import com.jmc.lang.primitive.Chars;
import org.junit.Assert;
import org.junit.Test;

public class CharsTest {
    @Test
    public void test() {
        Assert.assertFalse(Chars.orEquals('a', 'b', 'c', 'd'));
    }
}
