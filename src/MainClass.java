// Домашнее задание
// Организуем гонки
// Есть набор правил:
// Все участники должны стартовать одновременно, несмотря на то что на подготовку у каждого уходит разное время
// В туннель не может заехать одновременно больше половины участников(условность)
// Попробуйте все это синхронизировать
// Как только первая машина пересекает финиш, необходимо ее объявить победителем(победитель
// должен быть только один, и вообще должен быть)
// Только после того как все завершат гонку нужно выдать объявление об окончании
// Можете корректировать классы(в т.ч. конструктор машин)
// и добавлять объекты классов из пакета util.concurrent

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
    public static final int CARS_COUNT = 20;
    public static final Object mon = new Object();
    public static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), mon,lock);
        }
        Thread[] carsThread = new Thread[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            carsThread[i] = new Thread(cars[i]);
            carsThread[i].start();
        }
        lock.lock();


        System.out.println("After Lock");
        try {

            Thread.sleep(5000);

            System.out.println(lock.getQueueLength()+" getQueueLength");
            System.out.println(lock.getHoldCount()+" getHoldCount");

            synchronized (mon) {
                mon.notifyAll();
            }

            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            for (int i = 0; i < cars.length; i++) carsThread[i].join();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");





        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
