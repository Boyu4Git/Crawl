import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	static HashMap<String, Profile> record =new HashMap<String,Profile>();
	static HashSet<String> urlSet = new HashSet<String>();
	static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
	static int currentNum = 0;
	double hasCompany=0;
	double hasTitle=0;
	double hasExpPeriod=0;
	double hasEdu=0;
	double hasDegree=0;
	double hasMajor=0;
	double hasEduPeriod=0;
	public void extractProfile(String url,int atLeast) throws IOException{
		
		
		//HashSet to store url to avoid duplicate url,list is CopyOnWriteArrayList used to do loop when fetching url
		urlSet.add(url);
		list.add(url);
		//to fetch other url from current url
		findAnotherUrl(url);
		//add elments while doing loop,break if the url is more than 30(requirement)
		Iterator<String> iterator = list.iterator();

		while(iterator.hasNext()){

		       String s = iterator.next();
		       if(urlSet.size()>atLeast)
					break;
				 findAnotherUrl(s);
		       

		}
		//Download all the data from urlSet
		downLoadProfile(urlSet);
		
		//traverse the record, key is the url, value is class profile
		 Iterator iter = record.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next(); 
			    String key = (String) entry.getKey(); 
			    Profile val = (Profile) entry.getValue(); 
			    //extract the content we want
			    extractProfile(val);
			    hasCompany= hasCompany+val.getHasCompany();
				   hasDegree= hasDegree+ val.getHasDegree();
				   hasEdu= hasEdu+ val.getHasEdu();
				   hasEduPeriod =hasEduPeriod+ val.getHasEduPeriod();
				   hasExpPeriod =hasExpPeriod+ val.getHasExpPeriod();
				   hasMajor =hasMajor+ val.getHasMajor();
				   hasTitle =hasTitle+ val.getHasTitle();
			    //for calculate percentage
			  
			}
			//output percentage
	
		 
		 
	}
	
	public void outputPecentage(){
		
		
		 System.out.println("-----------");
			System.out.println("percentage of hascompany is "+hasCompany/urlSet.size());
			System.out.println("percentage of hasTitle is "+hasTitle/urlSet.size());
			System.out.println("percentage of hasExpPeriod is "+hasExpPeriod/urlSet.size());
			System.out.println("percentage of hasEdu is "+hasEdu/urlSet.size());
			System.out.println("percentage of hasDegree is "+hasDegree/urlSet.size());
			System.out.println("percentage of hasEduPeriod is "+hasEduPeriod/urlSet.size());
			System.out.println("percentage of hasMajor is "+hasMajor/urlSet.size());
	}
	
	public void outputProfile(){
		Iterator iter = record.entrySet().iterator(); 
		while (iter.hasNext()) { 
			 Map.Entry entry = (Map.Entry) iter.next(); 
			    String key = (String) entry.getKey(); 
			    Profile val = (Profile) entry.getValue(); 
			System.out.println(val);
		}
	}
	
	public void outputSingle(String url){
		System.out.println(record.get(url));
		
	}
	
	public void extractSingle(String url) throws IOException{
		Document doc = Jsoup.connect((String)url).get();
		String content = doc.html();
		String name = doc.select("head").select("title").text();
		//split profile name
		String a[] = name.split("\\|");
		if (record.containsKey(url))
			return;
		//download the content to project directory's profile
		File root = new File("SingleProfile");
		root.mkdir();
		File f = new File("SingleProfile//"+  a[0] + ".txt");
		OutputStreamWriter osw = new OutputStreamWriter(
				new FileOutputStream(f));
		osw.write(content);
		//initialize Profile 
		Profile p = new Profile(a[0], (String)url, f);
		//url is key
		record.put((String)url, p);
		extractProfile(p);
	}
	
	

	private void extractProfile(Profile p) throws IOException {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(p.localFile, "UTF-8");
		Elements companies = doc.select("div[class*=experience");
		for(Element company:companies){
			
			
			//if we find a relative content update the content in profile and then set relative variable to one for calculating the percentage
			Company comp;
			
			if(company.select(".company-profile-public").select("span").hasText()){
				 comp= new Company();
				p.updateCompany(company.select(".company-profile-public").select("span").text(),comp);
				p.setHasCompany();
			}else{break;}
			if(company.select(".title").hasText()){
				p.updateCompanyTitle(company.select(".title").text(),comp);
				p.setHasTitle();
			}
			if(company.select(".period").select("abbr").hasText()){
			p.updateCompanyPeriod(company.select(".period").select("abbr").text(),comp);
			p.setHasExpPeriod();
			}
			p.companies.add(comp);
		}
		
		Elements education = doc.select("div[class$=summary-education").select(".education");
		
		for(Element edu:education){
			Education e;
			if(edu.select("h3[class^=summary").hasText()){
				e=new Education();
			p.updateEducation(edu.select("h3[class^=summary").text(),e);
			p.setHasEdu();
			}else{break;}
			
			if (edu.select(".degree").hasText()) {
				p.updateEducationDegree(edu.select(".degree").text(),e);
				p.setHasDegree();
			}
			
			if (edu.select(".major").hasText()) {
				p.updateEducationMajor(edu.select(".major").text(),e);
				p.setHasMajor();
			}
			if(edu.select(".period").select("abbr").hasText()){
			p.updateEducationPeriod(edu.select(".period").select("abbr").text(),e);
			p.setHasEduPeriod();
			}
			p.edu.add(e);
		}

		
		
		
		
		
		
	}

	private void findAnotherUrl(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		//find also viewed
		Elements el = doc.select(".leo-module").select("div.content").select("strong").select("a[href]");
		for(Element ele:el){
			if(ele.attr("href").contains("pub-pbmap")){
				urlSet.add(ele.attr("href"));
				list.add(ele.attr("href"));
			}
		}
		
		
		
	}

	private void downLoadProfile(HashSet set) throws IOException {
		//create profile directory
		File dir = new File("profile");
		dir.mkdir();
		for (Object url:set) {
			Document doc = Jsoup.connect((String)url).get();
			String content = doc.html();
			String name = doc.select("head").select("title").text();
			//split profile name
			String a[] = name.split("\\|");
			if (!record.containsKey(url)){
			
			//download the content to project directory's profile
			File root = new File("profile//" + a[0] + ".txt");
			
			BufferedWriter osw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(root)));
			osw.write(content);
			//initialize Profile 
			Profile p = new Profile(a[0], (String)url, root);
			//url is key
			record.put((String)url, p);
			}
		}
	}
}
