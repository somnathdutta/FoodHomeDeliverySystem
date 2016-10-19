package Bean;

public class StarBean {

	private int star;
	private String fieldName;
	
	public StarBean(int star, String fieldName) {
		super();
		this.star = star;
		this.fieldName = fieldName;
	}
	
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	
}
