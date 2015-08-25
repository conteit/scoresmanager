package it.conteit.scoresmanager.management.storage;

import java.io.File;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.workers.GrestLoader;
import it.conteit.scoresmanager.control.workers.GrestSerializer;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.Partial;
import it.conteit.scoresmanager.data.Penality;

public class TestWorkers{

	public static void main(String[] args) throws Exception{		
		IGrest grest = Grest.create("TestGrest");
		File f = new File("/Users/conteit/Documents/avatar.jpg");
		if(f.exists()){
			grest.setLogo(f);
			for(ITeam t : grest.teams()){
				t.setAvatar(f);
			}
		}
		
		IDay d1 = Day.create("Day1", grest.teamCount());
		IDay d2 = Day.create("Day2", grest.teamCount());
		IDay d3 = Day.create("Day3", grest.teamCount());
		
		grest.addDay(d1);
		grest.addDay(d2);
		grest.addDay(d3);
		
		IScore[] ss = new IScore[]{Partial.create("Day1-Partial1", new Integer[] {10,20}),
				Partial.create("Day1-Partial2", new Integer[] {30,20}),
				Partial.create("Day1-Partial3", new Integer[] {15,50}),
				Penality.create("Day1-Penality1", new Integer[] {-5,0}),
				Partial.create("Day2-Partial1", new Integer[] {10,20}),
				Partial.create("Day2-Partial2", new Integer[] {30,20}),
				Penality.create("Day2-Penality1", new Integer[] {-5,0}),
				Penality.create("Day2-Penality2", new Integer[] {-15,-50}),
				Penality.create("Day3-Penality1", new Integer[] {-10,-20}),
				Penality.create("Day3-Penality2", new Integer[] {-30,-20}),
				Penality.create("Day3-Penality3", new Integer[] {-15,-50}),
				Partial.create("Day3-Partial1", new Integer[] {5,100}),
				Partial.create("Day3-Partial2", new Integer[] {0,10})};
		
		grest.days()[0].addScore(ss[0]);
		grest.days()[0].addScore(ss[1]);
		grest.days()[0].addScore(ss[2]);
		grest.days()[0].addScore(ss[3]);
		
		grest.days()[1].addScore(ss[4]);
		grest.days()[1].addScore(ss[5]);
		grest.days()[1].addScore(ss[6]);
		grest.days()[1].addScore(ss[7]);
		
		grest.days()[2].addScore(ss[8]);
		grest.days()[2].addScore(ss[9]);
		grest.days()[2].addScore(ss[10]);
		grest.days()[2].addScore(ss[11]);
		grest.days()[2].addScore(ss[12]);
		
		ApplicationSystem.initialize(new String[0]);
		File file = new File("/Users/conteit/Desktop/storage.xml");
		
		GrestSerializer ser = GrestSerializer.startSaving(grest, file);
		System.out.println("Saving..");
		
		while(!ser.isCompleted()){
			Thread.sleep(100);
		}
		
		GrestLoader loa = GrestLoader.startLoading(file);
		System.out.println("Loading..");
		
		while(!loa.isCompleted()){
			Thread.sleep(100);
		}
		
		
		System.out.println("Completed");
	}
}
