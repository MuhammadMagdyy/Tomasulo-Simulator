
public class Register {

	String value;
	String name; 
//	boolean used;
	boolean type; //true = int
	
	
	public Register(String value, String name ){
		
		this.value = value;
		this.name = name;
		this.type = false;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	
		if (value != null && !value.isEmpty() && value.charAt(0) !='F') {
			this.type = true;
		}
		
		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isType() {
		return type;
	}


	public void setType(boolean type) {
		this.type = type;
	}


	public String toString() {
		return "Name: "+this.name+"| Value: "+this.value+ "| used before?: "+this.type;
	}
	
	
	
	
	
	
	
}
