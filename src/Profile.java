import java.io.File;
import java.util.ArrayList;


public class Profile {
	private String name;
	private String url;
	File localFile;
	
	//those attributes below are all used to calculate percentage
	private int hasCompany=0;
	private int hasTitle=0;
	private int hasExpPeriod=0;
	private int hasMajor=0;
	private int hasDegree=0;
	private int hasEduPeriod=0;
	private int hasEdu=0;
	ArrayList<Education> edu = new ArrayList<Education>();
	ArrayList<Company> companies = new ArrayList<Company>();
	public int getHasEdu() {
		return hasEdu;
	}
	public void setHasEdu() {
		this.hasEdu = 1;
	}
	public int getHasEduPeriod() {
		return hasEduPeriod;
	}
	public void setHasEduPeriod() {
		this.hasEduPeriod = 1;
	}
	public int getHasCompany() {
		return hasCompany;
	}
	public void setHasCompany() {
		this.hasCompany = 1;
	}
	
	public int getHasTitle() {
		return hasTitle;
	}
	public void setHasTitle() {
		this.hasTitle = 1;
	}
	public int getHasExpPeriod() {
		return hasExpPeriod;
	}
	public void setHasExpPeriod() {
		this.hasExpPeriod = 1;
	}
	public int getHasMajor() {
		return hasMajor;
	}
	public void setHasMajor() {
		this.hasMajor = 1;
	}
	public int getHasDegree() {
		return hasDegree;
	}
	public void setHasDegree() {
		this.hasDegree = 1;
	}
	
	public ArrayList<Company> getCompany() {
		return companies;
	}
	public void updateCompany(String company,Company c) {
		
		c.comName = company;
	}
	
	public void updateCompanyTitle(String company,Company comp) {
		
		comp.title = company;
	}
	public void updateCompanyPeriod(String company,Company comp) {
	
	comp.comPeriod = company;
}
	
	
	
	public ArrayList<Education> getEducation() {
		return edu;
	}
	public void updateEducation(String education, Education edu) {
		edu.eduName=education;
	}
	
	public void updateEducationPeriod(String education, Education edu) {
		edu.eduPeriod=education;
	}
	
	public void updateEducationDegree(String education, Education edu) {
		edu.degree=education;
	}
	
	public void updateEducationMajor(String education, Education edu) {
		edu.major=education;
	}
	
	Profile(String name, String url,File localFile){
		this.name=name;
		this.url=url;
		this.localFile=localFile;
	}
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "|"+name+"|"+getCompany()+getEducation();
	}

}
