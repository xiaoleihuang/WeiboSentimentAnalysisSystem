package model.mallet.topicmodel;

import java.io.IOException;

import model.mallet.InstancesReader;
import cc.mallet.topics.PAM4L;
import cc.mallet.util.Randoms;

public class PAM4LCompute {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PAM4L p=new PAM4L(500, 10);
		p.estimate(InstancesReader.getInstances("./resource/LDATrain.csv"), 500, 50, 50, 50, "./test.model", new Randoms());
		p.printTopWords(20, true);
	}
}