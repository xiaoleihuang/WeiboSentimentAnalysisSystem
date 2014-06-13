package retrieval_System;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter writer=new BufferedWriter(new FileWriter("text.txt"));
		writer.append("a");
		writer.flush();
		writer.close();
	}
}