import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static AtomicBoolean gotWinner = new AtomicBoolean(false);
    private Object mon;
    private ReentrantLock lock;


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

    public Car(Race race, int speed, Object mon, ReentrantLock lock) {
        this.lock= lock;
        this.mon = mon;
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
            //System.out.println("После ЛОк");

            lock.lock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        synchronized (lock) {
            try {
                lock.wait();
                //mon.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        if (!gotWinner.get()) {
            gotWinner.set(true);
            System.out.println(this.name + " WINS");
        }

    }

//    public static int getBarier() throws BrokenBarrierException, InterruptedException {
//        //return barier.await();
//    }

//    public static void setBarier(CyclicBarrier barier) {
//        Car.barier = barier;
//    }

    public static void startCars() {
        //mon.notify();
    }
}

