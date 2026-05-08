import java.util.*;

public class Main {


 static Boolean First = true;
 static Boolean FirstE = true;
 static Scanner scanner;

    // -----------------------------------
    public static void Parser(Tomasulo Tom){
         Scanner sc = scanner;
         System.out.println("Enter load latency: ");
         String loadCycles =  sc.nextLine();
         System.out.println("Enter store latency: ");
         String storeCycles =   sc.nextLine();
         System.out.println("Enter add latency: ");
         String addCycles =  sc.nextLine();
         System.out.println("Enter sub latency: ");
         String subCycles  = sc.nextLine();
         System.out.println("Enter mul latency: ");
         String mulCycles  =   sc.nextLine();
         System.out.println("Enter div latency: ");
         String divCycles  =  sc.nextLine();


         System.out.println("///////////////////Write code///////////////////");



         String insLine = "";
         while(sc.hasNextLine()) {

             insLine = insLine + "\n" + sc.nextLine().trim();

         }

//         System.out.print(insLine);

         String Lines [] = insLine.split("\n");

         for(int i = 0; i < Lines.length; i++) {

             if (Lines[i].isEmpty()) {
                 continue;
             }


//             System.out.println(i);
//             System.out.println(Lines[i]);

             String instruction [] = Lines[i].split(" ");
//             System.out.println(instruction[0]);
//             System.out.println(instruction[1]);
//             System.out.println(instruction[2]);
              if (instruction[0].equals("L.D"))
              {
                 Tom.InstructionQueue.add(new String[]{instruction[0],instruction[1], instruction[2], " ",loadCycles.toString()});
              }
             if (instruction[0].equals("S.D"))
             {
                 Tom.InstructionQueue.add(new String[]{instruction[0],instruction[1], instruction[2], " ",  storeCycles.toString()});

             }
             if (instruction[0].equals("ADD.D"))
             {
                 Tom.InstructionQueue.add(new String[]{instruction[0],instruction[1], instruction[2],instruction[3],  addCycles.toString()});

             }
             if (instruction[0].equals("SUB.D"))
             {
                 Tom.InstructionQueue.add(new String[]{instruction[0],instruction[1], instruction[2],instruction[3],  subCycles.toString()});

             }
             if (instruction[0].equals("MUL.D"))
             {
                 Tom.InstructionQueue.add(new String[]{instruction[0],instruction[1], instruction[2],instruction[3],  mulCycles.toString()});

             }
             if (instruction[0].equals("DIV.D"))
             {
                 Tom.InstructionQueue.add(new String[]{instruction[0],instruction[1], instruction[2],instruction[3],  divCycles.toString()});
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

                if(reg[i].type==true) {
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

        boolean removed=true;



        if(Tom.InstructionQueue.get(0)[0].equals("ADD.D")){




            for(int i=0;i<Tom.addSubReservation.length;i++){

                if (!Tom.addSubReservation[i].busy ){

                    Tom.addSubReservation[i].op="ADD";
                    Tom.addSubReservation[i].busy=true;

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

                    Tom.addSubReservation[i].op="SUB";
                    Tom.addSubReservation[i].busy=true;
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

                    Tom.mulDivReservation[i].op="MUL";
                    Tom.mulDivReservation[i].busy=true;
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

                    Tom.mulDivReservation[i].op="DIV";
                    Tom.mulDivReservation[i].busy=true;
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

                    Tom.loadBuffer[i].busy=true;

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

                    Tom.storeBuffer[i].busy=true;

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


    public static void Excute(Tomasulo Tom){



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


                if(Tom.storeBuffer[i].V!="") {

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

                    if(Tom.mulDivReservation[i].Vj!="" && Tom.mulDivReservation[i].Vk!=""){

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

                    if(Tom.addSubReservation[i].Vj!="" && Tom.addSubReservation[i].Vk!=""){

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


        return 0;
    }

    public static String checkMostDep(Tomasulo Tom){


        int oldest=0;
        String used ="";

        for(int i=0;i<Tom.loadBuffer.length;i++){

            if(Tom.loadBuffer[i].busy) {

                if (Tom.loadBuffer[i].remainingCycles <= -1) {

                    if (oldest >Tom.loadBuffer[i].remainingCycles){

                        oldest=Tom.loadBuffer[i].remainingCycles;
                        used ="L"+i;

                    }
                    else {

                        if(oldest == Tom.loadBuffer[i].remainingCycles){

                            if(search(Tom ,used)<search(Tom,"L"+i )){

                                oldest=Tom.loadBuffer[i].remainingCycles;
                                used="L"+i;
                            }

                        }

                    }

                }
            }
        }





        for(int i=0;i<Tom.storeBuffer.length;i++){

            if(Tom.storeBuffer[i].busy) {

                if (Tom.storeBuffer[i].remainingCycles <= -1) {

                    if (oldest >Tom.storeBuffer[i].remainingCycles){

                        oldest=Tom.storeBuffer[i].remainingCycles;
                        used ="S"+i;

                    }
                    else {

                        if(oldest == Tom.storeBuffer[i].remainingCycles){

                            if(search(Tom ,used)<search(Tom,"S"+i )){

                                oldest=Tom.storeBuffer[i].remainingCycles;
                                used="S"+i;
                            }

                        }

                    }

                }
            }
        }



        for(int i=0;i<Tom.addSubReservation.length;i++){

            if(Tom.addSubReservation[i].busy) {

                if (Tom.addSubReservation[i].remainingCycles <= -1) {

                    if (oldest >Tom.addSubReservation[i].remainingCycles){

                        oldest=Tom.addSubReservation[i].remainingCycles;
                        used ="A"+i;

                    }
                    else {

                        if(oldest == Tom.addSubReservation[i].remainingCycles){

                            if(search(Tom ,used)<search(Tom,"A"+i )){

                                oldest=Tom.addSubReservation[i].remainingCycles;
                                used="A"+i;
                            }

                        }

                    }

                }
            }
        }


        for(int i=0;i<Tom.mulDivReservation.length;i++){

            if(Tom.mulDivReservation[i].busy) {

                if (Tom.mulDivReservation[i].remainingCycles <= -1) {

                    if (oldest >Tom.mulDivReservation[i].remainingCycles){

                        oldest=Tom.mulDivReservation[i].remainingCycles;
                        used ="M"+i;

                    }
                    else {

                        if(oldest == Tom.mulDivReservation[i].remainingCycles){

                            if(search(Tom ,used)<search(Tom,"M"+i )){

                                oldest=Tom.mulDivReservation[i].remainingCycles;
                                used="M"+i;
                            }

                        }

                    }

                }
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
                    Tom.loadBuffer[i].busy=false;
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
                         value = String.valueOf(Float.valueOf(Tom.mulDivReservation[i].Vj) + Float.valueOf(Tom.mulDivReservation[i].Vk));
                    }
                    else{
                         value = String.valueOf(Float.valueOf(Tom.mulDivReservation[i].Vj) - Float.valueOf(Tom.mulDivReservation[i].Vk));
                    }
                    Tom.addSubReservation[i].busy=false;
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
                    Tom.mulDivReservation[i].busy=false;
                    Dis(Tom,"M"+i,value);

                }

            }

        }


        for(int i=0;i<Tom.storeBuffer.length;i++){

            if(Tom.storeBuffer[i].busy){
                String s="S"+i;

                if(s.equals(used)){
                    Tom.dataMemory[Tom.storeBuffer[i].address]=Tom.storeBuffer[i].V;
                    Tom.storeBuffer[i].busy=false;


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

System.out.println(" " +
"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<NEW CYCLE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("Cycle num. :"+Tom.cycle);
        System.out.println("Load Reservation Slots :");
        for(int i=0;i<Tom.loadBuffer.length;i++){

           System.out.println("Load Reservation Slot num. :"+i);
           System.out.println("Busy :"+Tom.loadBuffer[i].busy+" ");
           System.out.println("Address :"+Tom.loadBuffer[i].address+" ");
           System.out.println("RemainingCycles :"+Tom.loadBuffer[i].remainingCycles+" ");
           System.out.println("JustAdded :"+Tom.loadBuffer[i].JustAdded+" ");
           System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        }
        System.out.println("**************************************************************************************************************************************");

        System.out.println("Store Reservation Slots :");
        for(int i=0;i<Tom.storeBuffer.length;i++){

            System.out.println("Store Reservation Slot num. :"+i);
            System.out.println("Busy :"+Tom.storeBuffer[i].busy+" ");
            System.out.println("Address :"+Tom.storeBuffer[i].address+" ");
            System.out.println("RemainingCycles :"+Tom.storeBuffer[i].remainingCycles+" ");
            System.out.println("JustAdded :"+Tom.storeBuffer[i].JustAdded+" ");
            System.out.println("V :"+Tom.storeBuffer[i].V+" ");
            System.out.println("Q :"+Tom.storeBuffer[i].Q+" ");
            System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        }
        System.out.println("**************************************************************************************************************************************");

        System.out.println("Add/Sub Reservation Slots :");
        for(int i=0;i<Tom.addSubReservation.length;i++){

            System.out.println("Add/Sub  Reservation Slot num. :"+i);
            System.out.println("Busy :"+Tom.addSubReservation[i].busy+" ");
            System.out.println("RemainingCycles :"+Tom.addSubReservation[i].remainingCycles+" ");
            System.out.println("JustAdded :"+Tom.addSubReservation[i].JustAdded+" ");
            System.out.println("Vk :"+Tom.addSubReservation[i].Vk+" ");
            System.out.println("Vj :"+Tom.addSubReservation[i].Vj+" ");
            System.out.println("Qk :"+Tom.addSubReservation[i].Qk+" ");
            System.out.println("Qj :"+Tom.addSubReservation[i].Qj+" ");
            System.out.println("OP :"+Tom.addSubReservation[i].op+" ");


            System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        }
        System.out.println("**************************************************************************************************************************************");


        for(int i=0;i<Tom.mulDivReservation.length;i++){

            System.out.println("Mul/Div  Reservation Slot num. :"+i);
            System.out.println("Busy :"+Tom.mulDivReservation[i].busy+" ");
            System.out.println("RemainingCycles :"+Tom.mulDivReservation[i].remainingCycles+" ");
            System.out.println("JustAdded :"+Tom.mulDivReservation[i].JustAdded+" ");
            System.out.println("Vk :"+Tom.mulDivReservation[i].Vk+" ");
            System.out.println("Vj :"+Tom.mulDivReservation[i].Vj+" ");
            System.out.println("Qk :"+Tom.mulDivReservation[i].Qk+" ");
            System.out.println("Qj :"+Tom.mulDivReservation[i].Qj+" ");
            System.out.println("OP :"+Tom.mulDivReservation[i].op+" ");


            System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        }
        System.out.println("**************************************************************************************************************************************");

        System.out.println("Reg File :");
        for(int i=0;i<Tom.regs.length;i++){

            System.out.print(Tom.regs[i]+"  ");
        }
        System.out.println("**************************************************************************************************************************************");

        System.out.println("Data Mem :");
        for(int i=0;i<Tom.dataMemory.length;i++){

            System.out.print(Tom.dataMemory[i]+"  ");
        }
        System.out.println("**************************************************************************************************************************************");
    }

    public static void simulator(Tomasulo Tom){

        while(true) {
            if(!Tom.InstructionQueue.isEmpty() || FirstE  ) {
                Issue(Tom);
                FirstE=false;
            }
            Excute(Tom);
            WriteBack(Tom);
            Tom.cycle++;
            printTom(Tom);
            if (checkEnd(Tom)) {
                System.out.println("Finished");
                break;
            }

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
        Register regs[] = new Register[16];
        System.out.println("Enter addSub Reservation station size: ");
        int addSubSize = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter mulDiv Reservation station size: ");
        int mulDivSize = Integer.parseInt(scanner.nextLine().trim());
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


    }
}
