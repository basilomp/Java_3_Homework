public class ABC {

    static Object lock = new Object();
    static final int it = 5;
    static volatile char letter = 'A';



    public static void main(String[] args) {
        new Thread(() -> {
            try {
                for (int i = 0; i < it; i++) {
                    synchronized (lock) {
                        while (letter != 'A') {
                            lock.wait();
                        }
                        System.out.print((i+1) + ":" + 'A');
                        letter = 'B';
                        lock.notifyAll();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < it; i++) {
                    synchronized (lock) {
                        while (letter != 'B') {
                            lock.wait();
                        }
                        System.out.print('B');
                        letter = 'C';
                        lock.notifyAll();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < it; i++) {
                    synchronized (lock) {
                        while (letter != 'C') {
                            lock.wait();
                        }
                        System.out.println('C' + System.lineSeparator());
                        letter = 'A';
                        lock.notifyAll();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();




    }
}
