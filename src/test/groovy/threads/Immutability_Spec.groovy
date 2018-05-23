package threads

import spock.lang.Specification

class Immutability_Spec extends Specification {

    class ImmutableValue {
        private int value = 0;

        public ImmutableValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public ImmutableValue add(int valueToAdd) {
            return new ImmutableValue(this.value + valueToAdd);
        }
    }

    class Calculator {
        private Integer lock1 = new Integer(1);

        private ImmutableValue value;

        public ImmutableValue getValue() {
            return value;
        }

        public void setValue(ImmutableValue value) {
            this.value = value;
        }

        public void add(int newValue) {
            synchronized (lock1) {
                this.value = this.value.add(newValue);
            }
        }
    }

    def "Immutability"() {
        given:
            Calculator calc = new Calculator();
            ImmutableValue value = new ImmutableValue(1);
            calc.setValue(value)
        when:
            calc.add(10)
        then:
            calc.getValue().getValue() == 11
            !calc.getValue().equals(value)
    }
}
