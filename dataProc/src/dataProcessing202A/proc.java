package dataProcessing202A;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
public class proc {

	static ArrayList<Integer[]> interData = new ArrayList<Integer[]>();
	static ArrayList<String> resultData = new ArrayList<String>();
	static int lastFilledIndex = -1;
	static boolean [] stopped = {true,true,true,true,true,true,true,true,true,true,true};

	public static void main(String[] args) {

		String csvFile = "mdzzfilled.csv";
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<String[]> listData = new ArrayList<String[]>();


		//read csv
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] data = line.split(cvsSplitBy);
				listData.add(data);



			}


		} catch (IOException e) {
			e.printStackTrace();
		}
		//generate form
		/*
		for (int j = 1; j <= 7; j++){
			for (int i = 0 ; i <= 1440; i+=5)
			{
				Integer[] minuteLine = new Integer[11];
				minuteLine[0] = j;
				minuteLine[1] = i;
				minuteLine[2] = 0;
				minuteLine[3] = 0;
				minuteLine[4] = 0;
				minuteLine[5] = 0;
				minuteLine[6] = 0;
				minuteLine[7] = 0;
				minuteLine[8] = 0;
				minuteLine[9] = 0;
				minuteLine[10] = 0;

				interData.add(minuteLine);
			}
		}
		 */

		//processing

		for (int i = 1; i < listData.size(); i++){
			String[] currRow = listData.get(i);
			int day = 0;
			String date[] = listData.get(i)[0].split(" ");
			String dayArr[] = date[0].split("/");
			int dateReal = Integer.parseInt(dayArr[1]);
			day = Integer.parseInt(dayArr[1])%7 +1;
			System.out.println(day);

			int minute = 0;

			String min[] = date[1].split(":");
			minute = Integer.parseInt(min[0])*60+ Integer.parseInt(min[1]);

			for (int col = 1; col < currRow.length; col++){
				if (currRow[col].equals("-1"))
					if (i == 1)
						currRow[col] = "0";
					else
						currRow[col] = listData.get(i-1)[col].toString();
				//System.out.print(currRow[col] + " ");

			}
			//System.out.println();
			generateDay(day, minute,dateReal,currRow);

		}
		try{
			PrintWriter pw = new PrintWriter("dataout.csv");
			for (int i = 0; i < interData.size(); i++){
				for (int j = 0; j < 11; j++){
					System.out.print(interData.get(i)[j]+" ");
					if (j<10)
						pw.print(interData.get(i)[j]+",");
					else
						pw.print(interData.get(i)[j]);

				}
				System.out.println();
				pw.println();
			}
		}catch(FileNotFoundException e){}
	}



	public static void generateDay (int day, int minute,int date, String[] line){
		int listIndex = (date-17) * 1440/5 + minute/5;

		for ( ; lastFilledIndex<listIndex; lastFilledIndex++)
		{
			
			Integer[] curr = new Integer[11];
			if (lastFilledIndex == -1)
			{
				curr[0] = 4;
				curr[1] = 0;
				curr[2] = 0;
				curr[3] = 0;
				curr[4] = 0;
				curr[5] = 63;
				curr[6] = 72;
				curr[7] = 70;
				curr[8] = 0;
				curr[9] = 0;
				curr[10] = 0;

			}
			else{
				if (interData.get(lastFilledIndex)[1] ==1435)
					if (interData.get(lastFilledIndex)[0] == 7)
						curr[0] = 1;
					else
						curr[0] = interData.get(lastFilledIndex)[0]+1;
				else
					curr[0] = interData.get(lastFilledIndex)[0];
				curr[1] = ((lastFilledIndex+1)%288)*5;
				for (int j = 2; j < 11; j++){
					if (j == 5||j==6||j==7)
						curr[j] = interData.get(lastFilledIndex)[j];
					else
						if(stopped[j])
							curr[j] = 0;
						else
							curr[j] = 1;
						
				}

			}
			interData.add(curr);
		}

		Integer[] curr = new Integer[11];
		curr[0] = day;
		curr[1] = minute - minute%5;
		Integer[] temp = interData.get(lastFilledIndex);
		int lfi = lastFilledIndex;
		if (interData.get(lastFilledIndex)[1].equals(curr[1])){
			for (int i = 2; i < 11; i++){
				if(i == 2)
					if(Integer.parseInt(line[i-1]) == 0){
						interData.get(lastFilledIndex)[i] = 0;
						stopped[i] = true;
					}
					else{
					
					stopped[i] = false;
				}
					
				
				if (Integer.parseInt(line[i-1]) == 1){
					interData.get(lastFilledIndex)[i] = 1;
					stopped[i] = false;
				}
				else if (Integer.parseInt(line[i-1]) > 1)
					interData.get(lastFilledIndex)[i] = Integer.parseInt(line[i-1]);
				else{stopped[i] = true;}
					
			}
		}
		else{

			for (int i = 2; i < 11; i++){
				if (Integer.parseInt(line[i-1]) == 1){
					curr[i] = 1;
					stopped[i] = false;
				}
				else if (Integer.parseInt(line[i-1]) > 1){
					curr[i] = Integer.parseInt(line[i-1]);
				}
				else{
					curr[i] = 0;
					stopped[i] = true;
				}
			}
			interData.add(curr);
			lastFilledIndex++;
		}
		


	}

}


