public class LoadBufferSlot {

    int address;
    Boolean busy;
    int remainingCycles;
    int issueOrder = -1;

    boolean JustAdded=true;
    public LoadBufferSlot(int address,Boolean busy, int remainingCycles,boolean JustAdded){
        this.address = address;
        this.busy = false;
        this.remainingCycles = remainingCycles;
        this.JustAdded=JustAdded;
    }





}
