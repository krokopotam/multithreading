package threads

import spock.lang.Specification

class Threads_Spec extends Specification {

    static class MyThread extends Thread {
        void run() {
            println Thread.currentThread().getName()
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        void run() {
            println Thread.currentThread().getName()
        }
    }

    def "Thread 1 example"() {
        given:
            MyThread thread = new MyThread()
        when:
            thread.start()
        then:
            noExceptionThrown()
    }

    def "Thread 2 example"() {
        given:
            Thread thread = new Thread(new MyRunnable())
        when:
        thread.start()
            then:
            noExceptionThrown()

    }

    def "Thread 3 example"() {
        given:
            Thread thread = new Thread(new MyRunnable(), "My unique name")
        when:
            thread.start()
        then:
            println thread.getName()
            noExceptionThrown()
    }

    def "Thread 4 example"() {
        given:
        when:
        for (int i = 0; i < 10; i++) {
            new Thread(new MyRunnable(), "My unique name " + i).start()
        }
        then:
            noExceptionThrown()
    }
}
