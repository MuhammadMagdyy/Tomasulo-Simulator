public class StoreBufferSlot {

    int address;
    Boolean busy ;
    String V;
    String Q;
    int remainingCycles;
    int issueOrder = -1;

    boolean JustAdded=true;

    public StoreBufferSlot(int address,Boolean busy, int remainingCycles, String V, String Q, boolean JustAdded){
        this.address = address;
        this.busy = false;
        this.remainingCycles = remainingCycles;
        this.V = V;
        this.Q = Q;
        this.JustAdded=JustAdded;
    }





}
