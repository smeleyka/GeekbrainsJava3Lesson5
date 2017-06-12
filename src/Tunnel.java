import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_sv;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private static Semaphore carsLimit = new Semaphore(MainClass.CARS_COUNT/2);
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                carsLimit.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            carsLimit.release();
        }
    }
}
