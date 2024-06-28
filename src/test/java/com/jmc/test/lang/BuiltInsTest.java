package com.jmc.test.lang;

import org.junit.Test;

import static com.jmc.lang.BuiltIns.*;

public class BuiltInsTest {
    @Test
    public void test() {
      print(666);
      println();
      printf("%d + %d = %d\n", 3, 4, 3 + 4);

      println(sqrt(9));
      println(pow(2, 5));
      println(round(3.4f));
      println(round(4.7d));
      println(abs(-3));
      println(max(3, 4));
      println(min(-1, 2));
    }
}
