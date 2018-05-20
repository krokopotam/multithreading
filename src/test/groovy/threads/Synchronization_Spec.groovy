package threads

import spock.lang.Specification

import java.lang.reflect.Array

class Synchronization_Spec extends Specification {

    interface TwoSums {
        void add(int val1, int val2);
        int get1()
        int get2()
    }

    class TwoSumsSynchronized implements TwoSums{

        private int sum1 = 0;
        private int sum2 = 0;

        private Integer sum1Lock = new Integer(1);
        private Integer sum2Lock = new Integer(2);

        @Override
        void add(int val1, int val2) {
            synchronized (this.sum1Lock) {
                this.sum1 += val1;
            }
            synchronized (this.sum2Lock) {
                this.sum2 += val2;
            }
            println "TSS: "+sum1+" "+sum2
        }

        @Override
        int get1() {
            return this.sum1;
        }

        @Override
        int get2() {
            return this.sum2
        }
    }

    class TwoSumsNonSynchronized implements TwoSums{
        private int sum1 = 0;
        private int sum2 = 0;

        @Override
        void add(int val1, int val2) {
            this.sum1 += val1;
            this.sum2 += val2;
            println "TSNS "+sum1+" "+sum2
        }

        @Override
        int get1() {
            return this.sum1
        }

        @Override
        int get2() {
            return this.sum2
        }
    }

    class MyRunnableWithSums implements Runnable {

        final TwoSums sums
        MyRunnableWithSums(TwoSums sums) {
            this.sums = sums
        }

        @Override
        void run() {
            sums.add(1, 1)
            println Thread.currentThread().getName() + " s1: " + sums.get1() + " s2: " + sums.get2()
        }
    }

    def "Synchronization example"() {
        given:
            final TwoSumsSynchronized sums = new TwoSumsSynchronized()
            List<Thread> list = new ArrayList<>();
        when:
            for (int i = 0; i < 10; i++) {
                Thread thr = new Thread(new MyRunnableWithSums(sums), "My unique name " + i)
                list.add(thr)
                thr.start()
            }
            for(Thread thread : list) {
                thread.join()
            }
        then:
            sums.sum1 == 10
            sums.sum2 == 10
    }

    def "No Synchronization example"() {
        given:
            final TwoSumsNonSynchronized sums = new TwoSumsNonSynchronized()

        List<Thread> list = new ArrayList<>();

        when:
        for (int i = 0; i < 10; i++) {
            Thread thr = new Thread(new MyRunnableWithSums(sums), "My unique name " + i)
            list.add(thr)
            thr.start()
        }
        for(Thread thread : list) {
            thread.join()
        }
        then:
            sums.sum1 != 10
            sums.sum2 != 10
    }

}
