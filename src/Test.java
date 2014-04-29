
import java.io.IOException;


public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Crawler c= new Crawler();
		
		c.extractProfile("http://www.linkedin.com/pub/jiasen-lu/45/a81/212",60);
		c.extractSingle("http://www.linkedin.com/pub/jiasen-lu/45/a81/212");
		c.outputProfile();
		c.outputSingle("http://www.linkedin.com/pub/jiasen-lu/45/a81/212");
		c.outputPecentage();
		c.outputPecentage();
	}

}
