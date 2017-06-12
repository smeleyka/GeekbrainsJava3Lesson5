
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static AtomicBoolean gotWinner = new AtomicBoolean(false);
    public static CountDownLatch countDown;


    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CountDownLatch countDown) {
        this.countDown = countDown;
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {

        try {
            System.out.println(this.name + " готовится");

            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            countDown.countDown();

            synchronized (countDown) {
                countDown.wait();
            }

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }

            if (gotWinner.compareAndSet(false,true)) {
                System.out.println(this.name + " WINS");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startCars() {
        synchronized (countDown) {
            countDown.notifyAll();
        }
    }
}

