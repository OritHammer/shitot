package entity;

import java.io.Serializable;

public class TeachingProfessions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tp_id;
	private String name;
	
	public TeachingProfessions() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getTp_id() {
		return tp_id;
	}
	public void setTp_id(String tp_id) {
		this.tp_id = tp_id;
	}
}
