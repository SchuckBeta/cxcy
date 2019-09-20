//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Net.PC15.Connector;

public enum E_ControllerType {
    FC8900(0),
    FC8800(1),
    MC5800(2);

    private final int value;

    public static E_ControllerType getByValue(int val) {
        for(E_ControllerType e: E_ControllerType.values()) {
            if ((e.getValue() == val)) {
                return e;
            }
        }
        return null;
    }

    private E_ControllerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
