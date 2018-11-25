public class TreeObject {
	Long key;

	public TreeObject(String sequence){
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

	public boolean compareTo(TreeObject object){
		return this.key == object.getKey();
	}
}