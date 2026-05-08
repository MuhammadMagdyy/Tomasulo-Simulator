import java.util.*;

public class Main {


 static Boolean First = true;
 static Boolean FirstE = true;
 static Scanner scanner;
 static final int MAX_CYCLES = 10000;

    private static boolean hasValue(String value) {
        return value != null && !value.isEmpty();
    }

    private static void prepareReservationSlot(ReservationSlot slot, String op) {
        slot.op = op;
        slot.busy = true;
        slot.Vj = "";
        slot.Vk = "";
        slot.Qj = "";
        slot.Qk = "";
        slot.JustAdded = true;
    }

    private static void clearReservationSlot(ReservationSlot slot) {
        slot.busy = false;
        slot.op = "";
        slot.Vj = "";
        slot.Vk = "";
        slot.Qj = "";
        slot.Qk = "";
        slot.remainingCycles = 0;
        slot.issueOrder = -1;
        slot.JustAdded = true;
    }

    private static void prepareLoadBuffer(LoadBufferSlot slot) {
        slot.busy = true;
        slot.JustAdded = true;
    }

    private static void clearLoadBuffer(LoadBufferSlot slot) {
        slot.busy = false;
        slot.address = 0;
        slot.remainingCycles = 0;
        slot.issueOrder = -1;
        slot.JustAdded = true;
    }

    private static void prepareStoreBuffer(StoreBufferSlot slot) {
        slot.busy = true;
        slot.V = "";
        slot.Q = "";
        slot.JustAdded = true;
    }

    private static void clearStoreBuffer(StoreBufferSlot slot) {
        slot.busy = false;
        slot.address = 0;
        slot.V = "";
        slot.Q = "";
        slot.remainingCycles = 0;
        slot.issueOrder = -1;
        slot.JustAdded = true;
    }

    private static int readPositiveInt(Scanner sc, String label) {
        String raw = sc.nextLine().trim();
        try {
            int value = Integer.parseInt(raw);
            if (value > 0) {
                return value;
            }
        } catch (NumberFormatException ignored) {
        }
        throw new IllegalArgumentException(label + " must be a positive integer.");
    }

    private static String readNonNegativeLatency(Scanner sc, String label) {
        String raw = sc.nextLine().trim();
        try {
            int value = Integer.parseInt(raw);
            if (value >= 0) {
                return String.valueOf(value);
            }
        } catch (NumberFormatException ignored) {
        }
        throw new IllegalArgumentException(label + " latency must be a non-negative integer.");
    }

    private static boolean isRegister(String value) {
        if (value == null || !value.startsWith("F")) {
            return false;
        }
        try {
            int number = Integer.parseInt(value.substring(1));
            return number >= 1 && number <= 16;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static boolean isMemoryAddress(String value) {
        try {
            int address = Integer.parseInt(value);
            return address >= 0 && address <= 100;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static void addInstruction(Tomasulo Tom, String[] instruction, String latency, int lineNumber) {
        String op = instruction[0];

        if (op.equals("L.D") || op.equals("S.D")) {
            if (instruction.length != 3 || !isRegister(instruction[1]) || !isMemoryAddress(instruction[2])) {
                System.out.println("Skipping invalid instruction on line " + lineNumber + ": expected " + op + " F1..F16 0..100");
                return;
            }
            Tom.InstructionQueue.add(new String[]{op, instruction[1], instruction[2], " ", latency});
            return;
        }

        if (op.equals("ADD.D") || op.equals("SUB.D") || op.equals("MUL.D") || op.equals("DIV.D")) {
            if (instruction.length != 4 || !isRegister(instruction[1]) || !isRegister(instruction[2]) || !isRegister(instruction[3])) {
                System.out.println("Skipping invalid instruction on line " + lineNumber + ": expected " + op + " F1..F16 F1..F16 F1..F16");
                return;
            }
            Tom.InstructionQueue.add(new String[]{op, instruction[1], instruction[2], instruction[3], latency});
            return;
        }

        System.out.println("Skipping invalid instruction on line " + lineNumber + ": unsupported operation " + op);
    }

    // -----------------------------------
    public static void Parser(Tomasulo Tom){
	         Scanner sc = scanner;
	         System.out.println("Enter load latency: ");
	         String loadCycles =  readNonNegativeLatency(sc, "Load");
	         System.out.println("Enter store latency: ");
	         String storeCycles =   readNonNegativeLatency(sc, "Store");
	         System.out.println("Enter add latency: ");
	         String addCycles =  readNonNegativeLatency(sc, "Add");
	         System.out.println("Enter sub latency: ");
	         String subCycles  = readNonNegativeLatency(sc, "Sub");
	         System.out.println("Enter mul latency: ");
	         String mulCycles  =   readNonNegativeLatency(sc, "Mul");
	         System.out.println("Enter div latency: ");
	         String divCycles  =  readNonNegativeLatency(sc, "Div");


         System.out.println("///////////////////Write code///////////////////");



         String insLine = "";
         while(sc.hasNextLine()) {

             insLine = insLine + "\n" + sc.nextLine().trim();

         }

//         System.out.print(insLine);

         String Lines [] = insLine.split("\n");

             int instructionLineNumber = 0;
	         for(int i = 0; i < Lines.length; i++) {

	             if (Lines[i].isEmpty()) {
	                 continue;
	             }
                 instructionLineNumber++;


//             System.out.println(i);
//             System.out.println(Lines[i]);

             String instruction [] = Lines[i].split("\\s+");
//             System.out.println(instruction[0]);
//             System.out.println(instruction[1]);
//             System.out.println(instruction[2]);
              if (instruction[0].equals("L.D")) {
                  addInstruction(Tom, instruction, loadCycles, instructionLineNumber);
              } else if (instruction[0].equals("S.D")) {
                  addInstruction(Tom, instruction, storeCycles, instructionLineNumber);
              } else if (instruction[0].equals("ADD.D")) {
                  addInstruction(Tom, instruction, addCycles, instructionLineNumber);
              } else if (instruction[0].equals("SUB.D")) {
                  addInstruction(Tom, instruction, subCycles, instructionLineNumber);
              } else if (instruction[0].equals("MUL.D")) {
                  addInstruction(Tom, instruction, mulCycles, instructionLineNumber);
              } else if (instruction[0].equals("DIV.D")) {
                  addInstruction(Tom, instruction, divCycles, instructionLineNumber);
              } else {
                  addInstruction(Tom, instruction, "", instructionLineNumber);
              }

         }



}


    public static String find(String address, Register[] reg) {

        for (int i = 0; i<reg.length; i++) {

            if (reg[i].name.equals(address)) {
                return reg[i].value;
            }

        }

        return "not found";


    }

    public static String checkValueType(String address, Register[] reg) {


        for (int i = 0; i<reg.length; i++) {

            if (reg[i].name.equals(address)) {

                if(reg[i].type) {
                    return ""+reg[i].value;
                }
                return ""+reg[i].value;

            }

        }

        return "not found";
    }


    public static void Issue(Tomasulo Tom)
    {
	        if(First){
	        Parser(Tom);
	        First=false;}

            if (Tom.InstructionQueue.isEmpty()) {
                return;
            }

        boolean removed=true;



        if(Tom.InstructionQueue.get(0)[0].equals("ADD.D")){




            for(int i=0;i<Tom.addSubReservation.length;i++){

                if (!Tom.addSubReservation[i].busy ){

                    prepareReservationSlot(Tom.addSubReservation[i], "ADD");
                    Tom.addSubReservation[i].issueOrder = Tom.nextIssueOrder++;

/////////////////////////////////////////////////////// Vj & Qj ///////////////////////////////////////////////////////

                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[2].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.addSubReservation[i].Vj = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.addSubReservation[i].Qj = Tom.regs[j].value;
                            }
                        }

                    }


/////////////////////////////////////////////////////// Vk & Qk ///////////////////////////////////////////////////////
                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[3].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.addSubReservation[i].Vk = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.addSubReservation[i].Qk = Tom.regs[j].value;
                            }
                        }

                    }


/////////////////////////////////////////////////////////Update Reg File ///////////////////////////////////////////////

                    for(int j=0;j<Tom.regs.length;j++){

                        if(Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name)){

                            Tom.regs[j].type=false;
                            Tom.regs[j].value="A"+i;

                        }

                    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    Tom.addSubReservation[i].remainingCycles = Integer.parseInt(Tom.InstructionQueue.get(0)[4]);
                    removed =true;
                    break;
                }


                else{

                    removed=false;

                }

            }

        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        if(Tom.InstructionQueue.get(0)[0].equals("SUB.D")){


            for(int i=0;i<Tom.addSubReservation.length;i++){

                if (!Tom.addSubReservation[i].busy ){

                    prepareReservationSlot(Tom.addSubReservation[i], "SUB");
                    Tom.addSubReservation[i].issueOrder = Tom.nextIssueOrder++;
                    /////////////////////////////////////////////////////// Vj & Qj ///////////////////////////////////////////////////////

                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[2].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.addSubReservation[i].Vj = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.addSubReservation[i].Qj = Tom.regs[j].value;
                            }
                        }

                    }


/////////////////////////////////////////////////////// Vk & Qk ///////////////////////////////////////////////////////
                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[3].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.addSubReservation[i].Vk = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.addSubReservation[i].Qk = Tom.regs[j].value;
                            }
                        }

                    }
/////////////////////////////////////////////////////////Update Reg File ///////////////////////////////////////////////

                    for(int j=0;j<Tom.regs.length;j++){

                        if(Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name)){

                            Tom.regs[j].type=false;
                            Tom.regs[j].value="A"+i;

                        }

                    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    Tom.addSubReservation[i].remainingCycles = Integer.parseInt(Tom.InstructionQueue.get(0)[4]);
                    removed =true;
                    break;
                }

                else{
                    removed =false;
                }

            }

        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(Tom.InstructionQueue.get(0)[0].equals("MUL.D")){


            for(int i=0;i<Tom.mulDivReservation.length;i++){

                if (!Tom.mulDivReservation[i].busy ){

                    prepareReservationSlot(Tom.mulDivReservation[i], "MUL");
                    Tom.mulDivReservation[i].issueOrder = Tom.nextIssueOrder++;
/////////////////////////////////////////////////////// Vj & Qj ///////////////////////////////////////////////////////

                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[2].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.mulDivReservation[i].Vj = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.mulDivReservation[i].Qj = Tom.regs[j].value;
                            }
                        }

                    }

/////////////////////////////////////////////////////// Vk & Qk ///////////////////////////////////////////////////////
                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[3].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.mulDivReservation[i].Vk = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.mulDivReservation[i].Qk = Tom.regs[j].value;
                            }
                        }

                    }
/////////////////////////////////////////////////////////Update Reg File ///////////////////////////////////////////////

                    for(int j=0;j<Tom.regs.length;j++){

                        if(Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name)){

                            Tom.regs[j].type=false;
                            Tom.regs[j].value="M"+i;

                        }

                    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    Tom.mulDivReservation[i].remainingCycles = Integer.parseInt(Tom.InstructionQueue.get(0)[4]);
                    removed =true;
                    break;
                }

                else{
                    removed =false;
                }

            }

        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(Tom.InstructionQueue.get(0)[0].equals("DIV.D")){


            for(int i=0;i<Tom.mulDivReservation.length;i++){

                if (!Tom.mulDivReservation[i].busy ){

                    prepareReservationSlot(Tom.mulDivReservation[i], "DIV");
                    Tom.mulDivReservation[i].issueOrder = Tom.nextIssueOrder++;
/////////////////////////////////////////////////////// Vj & Qj ///////////////////////////////////////////////////////

                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[2].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.mulDivReservation[i].Vj = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.mulDivReservation[i].Qj = Tom.regs[j].value;
                            }
                        }

                    }

/////////////////////////////////////////////////////// Vk & Qk ///////////////////////////////////////////////////////
                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[3].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.mulDivReservation[i].Vk = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.mulDivReservation[i].Qk = Tom.regs[j].value;
                            }
                        }

                    }

/////////////////////////////////////////////////////////Update Reg File ///////////////////////////////////////////////

                    for(int j=0;j<Tom.regs.length;j++){

                        if(Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name)){

                            Tom.regs[j].type=false;
                            Tom.regs[j].value="M"+i;

                        }

                    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    Tom.mulDivReservation[i].remainingCycles = Integer.parseInt(Tom.InstructionQueue.get(0)[4]);
                    removed =true;
                    break;
                }

                else
                {
                    removed =false;
                }

            }

        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(Tom.InstructionQueue.get(0)[0].equals("L.D")){


            for(int i=0;i<Tom.loadBuffer.length;i++){
               // System.out.print(Tom.loadBuffer[i].busy );
                if (!Tom.loadBuffer[i].busy ){

                    prepareLoadBuffer(Tom.loadBuffer[i]);
                    Tom.loadBuffer[i].issueOrder = Tom.nextIssueOrder++;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    Tom.loadBuffer[i].address=Integer.parseInt(Tom.InstructionQueue.get(0)[2]);
                    Tom.loadBuffer[i].remainingCycles = Integer.parseInt(Tom.InstructionQueue.get(0)[4]);



/////////////////////////////////////////////////////////Update Reg File ///////////////////////////////////////////////

                    for(int j=0;j<Tom.regs.length;j++){

                        if(Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name)){

                            Tom.regs[j].type=false;
                            Tom.regs[j].value="L"+i;

                        }

                    }

                    removed =true;

                    break;
                }

                else
                {
                    removed =false;
                }

            }

        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        if(Tom.InstructionQueue.get(0)[0].equals("S.D")){


            for(int i=0;i<Tom.storeBuffer.length;i++){

                if (!Tom.storeBuffer[i].busy ){

                    prepareStoreBuffer(Tom.storeBuffer[i]);
                    Tom.storeBuffer[i].issueOrder = Tom.nextIssueOrder++;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    Tom.storeBuffer[i].address=Integer.parseInt(Tom.InstructionQueue.get(0)[2]);
                    Tom.storeBuffer[i].remainingCycles = Integer.parseInt(Tom.InstructionQueue.get(0)[4]);



                    for (int j = 0; j<Tom.regs.length; j++)
                    {
                        if (Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name))
                        {
                            if (Tom.regs[j].type)
                            {
                                Tom.storeBuffer[i].V = Tom.regs[j].value;
                            }
                            else
                            {
                                Tom.storeBuffer[i].Q = Tom.regs[j].value;
                            }
                        }

                    }

/////////////////////////////////////////////////////////Update Reg File ///////////////////////////////////////////////

//                    for(int j=0;j<Tom.regs.length;j++){
//
//                        if(Tom.InstructionQueue.get(0)[1].equals(Tom.regs[j].name)){
//
//                            Tom.regs[j].type=false;
//                            Tom.regs[j].value="S"+i;
//
//                        }
//
//                    }




                    removed =true;

                    break;

                }

                else
                {
                    removed =false;
                }

            }

        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


 if (removed ) Tom.InstructionQueue.remove(0);




    }


    public static void Execute(Tomasulo Tom){



        for(int i=0;i<Tom.loadBuffer.length;i++){

            if(Tom.loadBuffer[i].busy){

               if(!Tom.loadBuffer[i].JustAdded) {
                   Tom.loadBuffer[i].remainingCycles--;
               }
               else{
                   Tom.loadBuffer[i].JustAdded=false;
               }
            }

        }

        for(int i=0;i<Tom.storeBuffer.length;i++){

            if(Tom.storeBuffer[i].busy){


                if(!Tom.storeBuffer[i].JustAdded){


                if(hasValue(Tom.storeBuffer[i].V)) {

                    Tom.storeBuffer[i].remainingCycles--;
                }
            }
                else{

                    Tom.storeBuffer[i].JustAdded=false;

                }
            }

        }


        for(int i=0;i<Tom.mulDivReservation.length;i++){

            if(Tom.mulDivReservation[i].busy){

                if(!Tom.mulDivReservation[i].JustAdded){

                    if(hasValue(Tom.mulDivReservation[i].Vj) && hasValue(Tom.mulDivReservation[i].Vk)){

                        Tom.mulDivReservation[i].remainingCycles--;
                    }

                }

                else{

                    Tom.mulDivReservation[i].JustAdded=false;
                }


            }


        }


        for(int i=0;i<Tom.addSubReservation.length;i++){

            if(Tom.addSubReservation[i].busy){

                if(!Tom.addSubReservation[i].JustAdded){

                    if(hasValue(Tom.addSubReservation[i].Vj) && hasValue(Tom.addSubReservation[i].Vk)){

                        Tom.addSubReservation[i].remainingCycles--;
                    }

                }

                else{

                    Tom.addSubReservation[i].JustAdded=false;
                }


            }


        }



    }


    public static int search (Tomasulo Tom , String dep){

        if (dep == null || dep.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        char type = dep.charAt(0);
        int index;
        try {
            index = Integer.parseInt(dep.substring(1));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }

        if (type == 'L' && index >= 0 && index < Tom.loadBuffer.length) {
            return Tom.loadBuffer[index].issueOrder;
        }
        if (type == 'S' && index >= 0 && index < Tom.storeBuffer.length) {
            return Tom.storeBuffer[index].issueOrder;
        }
        if (type == 'A' && index >= 0 && index < Tom.addSubReservation.length) {
            return Tom.addSubReservation[index].issueOrder;
        }
        if (type == 'M' && index >= 0 && index < Tom.mulDivReservation.length) {
            return Tom.mulDivReservation[index].issueOrder;
        }
        return Integer.MAX_VALUE;
    }

    private static String olderReadyCandidate(Tomasulo Tom, String current, String candidate) {
        if (candidate.isEmpty()) {
            return current;
        }
        if (current.isEmpty()) {
            return candidate;
        }
        return search(Tom, candidate) < search(Tom, current) ? candidate : current;
    }

    public static String checkMostDep(Tomasulo Tom){

        String used ="";

        for(int i=0;i<Tom.loadBuffer.length;i++){
            if(Tom.loadBuffer[i].busy && Tom.loadBuffer[i].remainingCycles <= -1) {
                used = olderReadyCandidate(Tom, used, "L"+i);
            }
        }

        for(int i=0;i<Tom.storeBuffer.length;i++){
            if(Tom.storeBuffer[i].busy && Tom.storeBuffer[i].remainingCycles <= -1) {
                used = olderReadyCandidate(Tom, used, "S"+i);
            }
        }

        for(int i=0;i<Tom.addSubReservation.length;i++){
            if(Tom.addSubReservation[i].busy && Tom.addSubReservation[i].remainingCycles <= -1) {
                used = olderReadyCandidate(Tom, used, "A"+i);
            }
        }

        for(int i=0;i<Tom.mulDivReservation.length;i++){
            if(Tom.mulDivReservation[i].busy && Tom.mulDivReservation[i].remainingCycles <= -1) {
                used = olderReadyCandidate(Tom, used, "M"+i);
            }
        }

        return used;

    }

    public static void WriteBack(Tomasulo Tom){

       String used= checkMostDep(Tom);


        for(int i=0;i<Tom.loadBuffer.length;i++){

            if(Tom.loadBuffer[i].busy){
                String s="L"+i;

                if(s.equals(used)){

                    String value = Tom.dataMemory[Tom.loadBuffer[i].address];
                    clearLoadBuffer(Tom.loadBuffer[i]);
                    Dis(Tom,"L"+i,value);

                }

            }

        }


        for(int i=0;i<Tom.addSubReservation.length;i++){

            if(Tom.addSubReservation[i].busy){
                String s="A"+i;

                if(s.equals(used)){
                    String value;
                    if(Tom.addSubReservation[i].op.equals("ADD")) {
                         value = String.valueOf(Float.valueOf(Tom.addSubReservation[i].Vj) + Float.valueOf(Tom.addSubReservation[i].Vk));
                    }
                    else{
                         value = String.valueOf(Float.valueOf(Tom.addSubReservation[i].Vj) - Float.valueOf(Tom.addSubReservation[i].Vk));
                    }
                    clearReservationSlot(Tom.addSubReservation[i]);
                    Dis(Tom,"A"+i,value);

                }

            }

        }

        for(int i=0;i<Tom.mulDivReservation.length;i++){

            if(Tom.mulDivReservation[i].busy){
                String s="M"+i;

                if(s.equals(used)){
                    String value;
                    if(Tom.mulDivReservation[i].op.equals("MUL")) {
                         value = String.valueOf(Float.valueOf(Tom.mulDivReservation[i].Vj) * Float.valueOf(Tom.mulDivReservation[i].Vk));
                    }
                    else{
                        value = String.valueOf(Float.valueOf(Tom.mulDivReservation[i].Vj) / Float.valueOf(Tom.mulDivReservation[i].Vk));

                    }
                    clearReservationSlot(Tom.mulDivReservation[i]);
                    Dis(Tom,"M"+i,value);

                }

            }

        }


        for(int i=0;i<Tom.storeBuffer.length;i++){

            if(Tom.storeBuffer[i].busy){
                String s="S"+i;

                if(s.equals(used)){
                    Tom.dataMemory[Tom.storeBuffer[i].address]=Tom.storeBuffer[i].V;
                    clearStoreBuffer(Tom.storeBuffer[i]);


                }

            }

        }



    }

    public static boolean checkEnd(Tomasulo Tom){

        boolean done=true;
        for(int i=0;i<Tom.loadBuffer.length;i++){

            if(Tom.loadBuffer[i].busy){

                done=false;
            }
        }
        for(int i=0;i<Tom.storeBuffer.length;i++){

            if(Tom.storeBuffer[i].busy){

                done=false;
            }
        }

        for(int i=0;i<Tom.mulDivReservation.length;i++){

            if(Tom.mulDivReservation[i].busy){

                done=false;
            }
        }
        for(int i=0;i<Tom.addSubReservation.length;i++){

            if(Tom.addSubReservation[i].busy){

                done=false;
            }
        }

        if(!Tom.InstructionQueue.isEmpty()){

            done =false;
        }

        return done;

    }
    public static void printTom(Tomasulo Tom){

        System.out.println();
        System.out.println("=== Cycle " + Tom.cycle + " ===");

        System.out.println("Load Buffers");
        System.out.printf("%-4s %-5s %-7s %-5s %-5s%n", "Tag", "Busy", "Addr", "Left", "Ord");
        for(int i=0;i<Tom.loadBuffer.length;i++){
            System.out.printf("%-4s %-5s %-7d %-5d %-5d%n", "L"+i, Tom.loadBuffer[i].busy, Tom.loadBuffer[i].address,
                    Tom.loadBuffer[i].remainingCycles, Tom.loadBuffer[i].issueOrder);
        }

        System.out.println("Store Buffers");
        System.out.printf("%-4s %-5s %-7s %-8s %-8s %-5s %-5s%n", "Tag", "Busy", "Addr", "V", "Q", "Left", "Ord");
        for(int i=0;i<Tom.storeBuffer.length;i++){
            System.out.printf("%-4s %-5s %-7d %-8s %-8s %-5d %-5d%n", "S"+i, Tom.storeBuffer[i].busy, Tom.storeBuffer[i].address,
                    Tom.storeBuffer[i].V, Tom.storeBuffer[i].Q, Tom.storeBuffer[i].remainingCycles, Tom.storeBuffer[i].issueOrder);
        }

        System.out.println("Add/Sub Reservation Stations");
        System.out.printf("%-4s %-5s %-5s %-8s %-8s %-8s %-8s %-5s %-5s%n", "Tag", "Busy", "Op", "Vj", "Vk", "Qj", "Qk", "Left", "Ord");
        for(int i=0;i<Tom.addSubReservation.length;i++){
            System.out.printf("%-4s %-5s %-5s %-8s %-8s %-8s %-8s %-5d %-5d%n", "A"+i, Tom.addSubReservation[i].busy,
                    Tom.addSubReservation[i].op, Tom.addSubReservation[i].Vj, Tom.addSubReservation[i].Vk,
                    Tom.addSubReservation[i].Qj, Tom.addSubReservation[i].Qk, Tom.addSubReservation[i].remainingCycles,
                    Tom.addSubReservation[i].issueOrder);
        }

        System.out.println("Mul/Div Reservation Stations");
        System.out.printf("%-4s %-5s %-5s %-8s %-8s %-8s %-8s %-5s %-5s%n", "Tag", "Busy", "Op", "Vj", "Vk", "Qj", "Qk", "Left", "Ord");
        for(int i=0;i<Tom.mulDivReservation.length;i++){
            System.out.printf("%-4s %-5s %-5s %-8s %-8s %-8s %-8s %-5d %-5d%n", "M"+i, Tom.mulDivReservation[i].busy,
                    Tom.mulDivReservation[i].op, Tom.mulDivReservation[i].Vj, Tom.mulDivReservation[i].Vk,
                    Tom.mulDivReservation[i].Qj, Tom.mulDivReservation[i].Qk, Tom.mulDivReservation[i].remainingCycles,
                    Tom.mulDivReservation[i].issueOrder);
        }

        System.out.println("Registers");
        for(int i=0;i<Tom.regs.length;i++){
            System.out.print(Tom.regs[i].name + "=" + Tom.regs[i].value + (i == Tom.regs.length - 1 ? "" : "  "));
        }
        System.out.println();

        System.out.println("Data Memory");
        for(int i=0;i<Tom.dataMemory.length;i++){
            if (Tom.dataMemory[i] != null) {
                System.out.print("Mem[" + i + "]=" + Tom.dataMemory[i] + "  ");
            }
        }
        System.out.println();
    }

    public static void simulator(Tomasulo Tom){

        while(Tom.cycle < MAX_CYCLES) {
            if(!Tom.InstructionQueue.isEmpty() || FirstE  ) {
                Issue(Tom);
                FirstE=false;
            }
            Execute(Tom);
            WriteBack(Tom);
            Tom.cycle++;
            printTom(Tom);
            if (checkEnd(Tom)) {
                System.out.println("Finished");
                break;
            }

        }

        if (!checkEnd(Tom)) {
            System.out.println("Stopped after " + MAX_CYCLES + " cycles without finishing.");
        }


    }

    public static void Dis(Tomasulo Tom , String name , String value ){

        for(int i=0;i<Tom.addSubReservation.length;i++){

            if(Tom.addSubReservation[i].busy){

                if(Tom.addSubReservation[i].Qj.equals(name)){

                    Tom.addSubReservation[i].Vj=value;
                    Tom.addSubReservation[i].Qj="";

                }

                if(Tom.addSubReservation[i].Qk.equals(name)){

                    Tom.addSubReservation[i].Vk=value;
                    Tom.addSubReservation[i].Qk="";

                }

            }

        }


        for(int i=0;i<Tom.mulDivReservation.length;i++){

            if(Tom.mulDivReservation[i].busy){

                if(Tom.mulDivReservation[i].Qj.equals(name)){

                    Tom.mulDivReservation[i].Vj=value;
                    Tom.mulDivReservation[i].Qj="";

                }

                if(Tom.mulDivReservation[i].Qk.equals(name)){

                    Tom.mulDivReservation[i].Vk=value;
                    Tom.mulDivReservation[i].Qk="";

                }

            }

        }

        for(int i=0;i<Tom.storeBuffer.length;i++){

            if(Tom.storeBuffer[i].busy){

                if(Tom.storeBuffer[i].Q.equals(name)){

                    Tom.storeBuffer[i].V=value;
                    Tom.storeBuffer[i].Q="";

                }


            }

        }


        for(int i=0;i<Tom.regs.length;i++){

            if(Tom.regs[i].value.equals(name)){


                    Tom.regs[i].value=value;
                    Tom.regs[i].type=true;



            }

        }




    }

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        try {
        Register regs[] = new Register[16];
        System.out.println("Enter addSub Reservation station size: ");
        int addSubSize = readPositiveInt(scanner, "Add/sub reservation station size");
        System.out.println("Enter mulDiv Reservation station size: ");
        int mulDivSize = readPositiveInt(scanner, "Mul/div reservation station size");
        int cycle = 0;
        ReservationSlot addSubReservation[] = new ReservationSlot[addSubSize];
        ReservationSlot mulDivReservation[] = new ReservationSlot[mulDivSize];
        LoadBufferSlot loadBuffer[] = new LoadBufferSlot[3];
        StoreBufferSlot storeBuffer[] = new StoreBufferSlot[3];
        ArrayList<String[]> InstructionQueue = new ArrayList<String[]>();
        String dataMemory[] = new String[101];
        dataMemory[0]="10";
        dataMemory[1]="11";
        dataMemory[2]="5";
        dataMemory[3]="6";

//        LoadBufferSlot ld = new LoadBufferSlot(0,false,0,true);
//        StoreBufferSlot sd = new StoreBufferSlot (0,false,0,"","",true);
//        ReservationSlot rs = new ReservationSlot (false,"","","","","",0,true);



        for(int i=0;i<addSubSize;i++){
            addSubReservation[i] = new ReservationSlot (false,"","","","","",0,true);

        }
        for(int i=0;i<mulDivSize;i++){

            mulDivReservation[i] =new ReservationSlot (false,"","","","","",0,true);
        }
        for(int i=0;i<3;i++){

            loadBuffer[i] = new LoadBufferSlot(0,false,0,true);
        }
        for(int i=0;i<3;i++){

            storeBuffer[i] = new StoreBufferSlot (0,false,0,"","",true);
        }

        Tomasulo Tom = new Tomasulo(regs, addSubSize, mulDivSize, addSubReservation, mulDivReservation,
                loadBuffer,storeBuffer,InstructionQueue,dataMemory,cycle);

//        for (int i = 0 ; i<Tom.InstructionQueue.size(); i++){
//            System.out.print(Tom.InstructionQueue.get(i)[0] + " ");
//            System.out.print(Tom.InstructionQueue.get(i)[1] + " ");
//            System.out.print(Tom.InstructionQueue.get(i)[2] + " ");
//            System.out.print(Tom.InstructionQueue.get(i)[3] + " ");
//            System.out.println(Tom.InstructionQueue.get(i)[4] + " ");
//        }

        simulator(Tom);

        } catch (IllegalArgumentException e) {
            System.out.println("Input error: " + e.getMessage());
        }

    }
}
