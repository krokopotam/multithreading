package threads

import spock.lang.Specification

class ThreadLocal_Spec extends Specification {


    public static class MyRunnable implements Runnable {

        private ThreadLocal<Integer> threadLocal =
                new ThreadLocal<Integer>();

        @Override
        public void run() {
            threadLocal.set( (int) (Math.random() * 100D) );

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }

            System.out.println(threadLocal.get());
        }
    }

    def "ThreadLocal example"() {
        given:
            MyRunnable sharedRunnableInstance = new MyRunnable()
            Thread thread1 = new Thread(sharedRunnableInstance)
            Thread thread2 = new Thread(sharedRunnableInstance)

        when:
            thread1.start()
            thread2.start()
            thread1.join()
            thread2.join()
        then:
            println sharedRunnableInstance.threadLocal.get()
            sharedRunnableInstance.threadLocal.get() == null
            noExceptionThrown()
    }
}
