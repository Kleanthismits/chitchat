package chitchat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileEdit {
	BufferedWriter writer;
	
	
	
	
	public FileEdit(String filePath, String username) {
		try {
			
			File file = new File(username+"\\"+filePath);
			if (!file.exists()) {
			    try{
			        file.mkdirs();
			    } 
			    catch(SecurityException se){
			    }        
			}
			this.writer = new BufferedWriter(new FileWriter(file+"\\"+filePath+".txt",true));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public BufferedWriter getW() {
		return writer;
	}
	public void setW(BufferedWriter w) {
		this.writer = w;
	}

}
