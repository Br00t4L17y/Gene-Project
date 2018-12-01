public class TreeObject {
	private Long key;
	private int frequency;
	private String sequence;

	public TreeObject(String sequence){
		this.sequence = sequence;
		this.frequency = 0;
		String binaryVal = "";
		for(int i = 0; i < sequence.length(); i++){
			char charVal = sequence.charAt(i);
			
			if(charVal == 'A'){
				binaryVal += "00";
			}else if(charVal == 'C'){
				binaryVal += "01";
			}else if(charVal == 'G'){
				binaryVal += "10";
			}else if(charVal == 'T'){
				binaryVal += "11";
			}else{
				break;
			}
		}

		key = Long.parseLong(binaryVal);

	}

	public Long getKey(){
		return this.key;
	}

	public void incFrequency(){
		this.frequency++;
	}

	public int getFrequency(){
		return this.frequency;
	}
	
	/**
	 * 
	 * @param object
	 * @return -1 if this.key < parameter object
	 * @return 0 if the two keys are equal
	 * @return 1 if this.key > the parameter object
	 */
	public int compareTo(TreeObject object){
		if (object.getKey() > this.key) {
			return -1;
		}
		else if(object.getKey().equals(this.key)) {
			return 0;
		}
		return 1;
	}

	public String toString(){
		return this.sequence + ": " + this.frequency;
	}
}