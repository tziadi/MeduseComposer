package fr.lip6.move.meduse.core;

public class KeyProcessFragment {

	
	private String processComponentKey;
	private String processElementKey;
	
	
	public KeyProcessFragment(String key1, String key2){
		
		this.processComponentKey=key1;
		this.processElementKey=key2;
		
		
	}


	@Override
	public String toString() {
		return "KeyProcessFragment [processComponentKey=" + processComponentKey
				+ ", processElementKey=" + processElementKey + "]";
	}


	

	public String getProcessComponentKey() {
		return processComponentKey;
	}


	public String getProcessElementKey() {
		return processElementKey;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyProcessFragment other = (KeyProcessFragment) obj;
		if (processComponentKey == null) {
			if (other.processComponentKey != null)
				return false;
		} else if (!processComponentKey.equals(other.processComponentKey))
			return false;
		if (processElementKey == null) {
			if (other.processElementKey != null)
				return false;
		} else if (!processElementKey.equals(other.processElementKey))
			return false;
		return true;
	}
	
	
	
}
