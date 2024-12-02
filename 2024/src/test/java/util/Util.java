package util;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Callable;

public class Util {

    public static void timeExecution(Callable<Object> task) throws Exception {
        LocalTime startTime = LocalTime.now();

        task.call();

        LocalTime endTime = LocalTime.now();
        System.out.println(Duration.between(startTime, endTime).toMillis());
    }

}
