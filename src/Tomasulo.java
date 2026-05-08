import java.util.*;

public class Tomasulo {
	
	Register regs[] = new Register[16];
	int addSubSize;
	int mulDivSize;
	int cycle = 0;
	int nextIssueOrder = 0;

	ReservationSlot addSubReservation[];
	ReservationSlot mulDivReservation[] ;
	LoadBufferSlot loadBuffer[];
	StoreBufferSlot storeBuffer[];
	 ArrayList<String[]> InstructionQueue = new ArrayList<String[]>();

	String dataMemory[] = new String[101];



	
	

	Tomasulo(Register[] regs, int addSubSize, int mulDivSize, ReservationSlot[] addSubReservation, ReservationSlot[] mulDivReservation,
             LoadBufferSlot[] loadBuffer, StoreBufferSlot[] storeBuffer, ArrayList<String[]> InstructionQueue, String[] dataMemory, int cycle) {

		this.regs = regs;

		// Populate Register File
		for (int i = 1; i<17; i++) {


			Register R = new Register("","F"+i);


			this.regs[i-1] = R;

		}

		this.addSubReservation = addSubReservation;
		this.mulDivReservation = mulDivReservation;
		this.loadBuffer = loadBuffer;
		this.storeBuffer = storeBuffer;
		this.InstructionQueue = InstructionQueue;
		this.dataMemory = dataMemory;
		this.cycle = cycle;

	}


	
	public static void populate(Tomasulo tom) {
		
		
		for (int i = 1; i<17; i++) {
			
			
			Register R = new Register(null,"F"+i);
		
			
			tom.regs[i-1] = R;
			

			
		}
		
//		return tom.regs;
	}
	
	public static void printPopulate(Register[] reg) {
		
		for (int i = 0; i<reg.length;i++) {
			System.out.println(reg[i].toString());
			
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
				return "Value is a number: "+reg[i].value;
			}
			return "Address of Register: "+reg[i].value;
			
		}
		
	}
	
	return "not found";
			
	
	
	}

	
	
	
	public static void main(String args[]) {
		
		Register reg[] = new Register[16];
//		
		
//		Tomasulo tom = new Tomasulo(reg);
//		populate(tom);
//
//
//
//		tom.regs[3].setValue("15");
//
//		System.out.println(find("",tom.regs));
//		System.out.println(checkValueType("F4",tom.regs));
//
		
		
		
		
		
		
		
	}
	

	
	
	
	
	
	
}
