public class ReservationSlot {

    boolean busy ;
    String op;
    String Vj;
    String Vk;
    String Qj;
    String Qk;
    int remainingCycles;
    int issueOrder = -1;

    boolean JustAdded=true;

    public ReservationSlot(boolean busy, String op, String Vj, String Vk,String Qj, String Qk, int remainingCycles, boolean JustAdded){
        this.busy = false;
        this.op = op;
        this.Vj = Vj;
        this.Vk = Vk;
        this.Qj = Qj;
        this.Qk = Qk;
        this.remainingCycles = remainingCycles;
        this.JustAdded=JustAdded;

    }









}
