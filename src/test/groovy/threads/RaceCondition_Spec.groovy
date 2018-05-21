package threads

import spock.lang.Specification

class RaceCondition_Spec extends Specification {

    static class MyThread extends Thread {
        void run() {
            println Thread.currentThread().getName()
        }
    }

    static class MyRunnable implements Runnable {

        NotThreadSafe nts;

        MyRunnable(NotThreadSafe nts) {
            this.nts = nts
        }

        @Override
        void run() {
            for(int i=0;i<100000;i++) {
                this.nts.add("text")
            }
            println Thread.currentThread().getName()
        }
    }

    class NotThreadSafe{
        StringBuilder builder = new StringBuilder();

        public add(String text){
            this.builder.append(text);
        }
    }

    def "Race condition example"() {
        given:
            NotThreadSafe sharedInstance = new NotThreadSafe();
            Thread thread1 = new Thread(new MyRunnable(sharedInstance))
            Thread thread2 = new Thread(new MyRunnable(sharedInstance))

        when:
            thread1.start()
            thread2.start()
        then:
            noExceptionThrown()
    }

}
