package com.jmc.test.lang;

import com.jmc.lang.Tries;
import com.jmc.net.R;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TriesTest {
    @Test
    public void tryRunTest() {
        Tries.tryRun(() -> System.out.println(666));

        Assert.assertThrows(
                RuntimeException.class,
                () -> Tries.tryRun(
                        () -> { throw new RuntimeException("666"); }
                )
        );
    }

    @Test
    public void tryRunOrHandlerTest() {
        Tries.tryRunOrHandle(
                () -> System.out.println(666),
                // 预期不能走到这里
                e -> { throw new RuntimeException(e); }
        );

        Tries.tryRunOrHandle(
                () -> { throw new RuntimeException("777"); },
                // 预期走到这里
                Throwable::printStackTrace
        );
    }

    @Test
    public void tryRunOrThrowTest() {
        Assert.assertThrows(
                RuntimeException.class,
                () -> Tries.tryRunOrThrow(
                    () -> { throw new Exception("666"); },
                    RuntimeException::new
                )
        );
    }

    @Test
    public void tryRunOrCaptureTest() {
        Optional<Throwable> e = Tries.tryRunOrCapture(
                () -> System.out.println(666)
        );
        Assert.assertTrue(e.isEmpty());

        Optional<Throwable> e2 = Tries.tryRunOrCapture(
                () -> { throw new Exception("777"); }
        );
        Assert.assertTrue(e2.isPresent());
        Assert.assertEquals("777", e2.get().getMessage());
    }

    @Test
    public void tryGetTest() {
        int result = Tries.tryGet(() -> 666);
        Assert.assertEquals(666, result);

        Assert.assertThrows(
                RuntimeException.class,
                () -> Tries.tryGet(() -> { throw new Exception("777"); })
        );
    }

    @Test
    public void tryGetOrHandleTest() {
        Integer result = Tries.tryGetOrHandle(
                () -> 666,
                // 预期不能走到这里
                e -> { throw new RuntimeException(e); }
        );
        Assert.assertEquals(Integer.valueOf(666), result);

        Integer result2 = Tries.tryGetOrHandle(
                () -> { throw new RuntimeException("777"); },
                // 预期走到这里
                Throwable::printStackTrace
        );
        Assert.assertNull(result2);
    }

    @Test
    public void tryGetOrThrowTest() {
        Integer result = Tries.tryGetOrThrow(
             () -> 666,
             RuntimeException::new
        );
        Assert.assertEquals(Integer.valueOf(666), result);

        Assert.assertThrows(
                RuntimeException.class,
                () -> Tries.tryGetOrThrow(
                    () -> { throw new RuntimeException("777"); },
                    RuntimeException::new
                )
        );
    }

    @Test
    public void tryGetOptionalOrThrowTest() {
        Optional<Integer> result = Tries.tryGetOptionalOrThrow(
                () -> 666,
                RuntimeException::new
        );
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(Integer.valueOf(666), result.get());

        Assert.assertThrows(
                RuntimeException.class,
                () -> Tries.tryGetOptionalOrThrow(
                        () -> { throw new RuntimeException("777"); },
                        RuntimeException::new
                )
        );
    }

    @Test
    public void uncheckedTest() {
        Stream.generate(() -> (Runnable) () -> System.out.println(666))
                .limit(3)
                .map(Thread::new)
                .peek(Thread::start)
                // 简写抛出Stream中的异常
                .forEach(Tries.unchecked(Thread::join));
    }
}
