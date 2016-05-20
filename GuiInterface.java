import java.util.*;
public class GuiInterface {
	
	static long WK = 0l, WQ = 0l, WR = 0l, WB = 0l, WN = 0l, WP = 0l;
	static long BK = 0l, BQ = 0l, BR = 0l, BB = 0l, BN = 0l, BP = 0l;
	static boolean whitemove = true;
	
	public static void communicateToUCI(){
		Scanner input = new Scanner(System.in);
		while(true){
			String inputcmd = input.nextLine();
			if(inputcmd.equals("uci"))
			{
				outputUCI();
			}
			if(inputcmd.startsWith("setoptions"))
			{
				//setOptions();  //Not supported now
			}
			if(inputcmd.equals("isready"))
			{
				System.out.println("readyok");
			}
			if(inputcmd.equals("ucinewgame"))
			{
				//resettheboard();  // not supported now
			}
			if(inputcmd.startsWith("position"))
			{
				setposition(inputcmd);
			}
			if(inputcmd.startsWith("go"))
			{
				outputbestmove();
			}
			if(inputcmd.equals("quit"))
			{
				input.close();
				System.exit(0);
			}
			if(inputcmd.equals("print"))
			{
				GenerateBoard.drawboard(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
			}
		}
	}
	
	static void outputUCI()
	{
		System.out.println("id name VarunChess");
		System.out.println("id author Varun");
		System.out.println("uciok");
	}
	
	static void setposition(String inputcmd)
	{
		whitemove = true;
		if(inputcmd.contains("startpos"))
		{
			GenerateBoard.beginboard();
		}
		if(inputcmd.contains("moves"))
		{
			inputcmd = inputcmd.substring(inputcmd.indexOf("moves") + 6);
			while(inputcmd.length() > 3)
			{
				String move = "";
				if(inputcmd.length() == 4){
					move = inputcmd;
				}else{
					if(inputcmd.charAt(4) == ' '){
						move = inputcmd.substring(0, 4); 
					}else{
						move = inputcmd.substring(0, 5);
					}
				}
				UCIinputToMove(move,whitemove);
				if(inputcmd.contains(" "))
				{
				    inputcmd = inputcmd.substring(inputcmd.indexOf(' ') + 1);
				} else {
					inputcmd = inputcmd.substring(4);
				}
			}
		}
		
	}
	
	public static void outputbestmove()
	{
		String bestmove;
		long startTime = System.currentTimeMillis();
		bestmove = Search.get_bestmove(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP,whitemove);
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
		System.out.println("bestmove "+moveOutputToUCI(bestmove));
	}
	
	static String moveOutputToUCI(String move){
		String ucimove = "";
		String promote_to = "";
		int begin = 0, end = 0;
		if(Character.isDigit(move.charAt(3)))
		{
			begin = Character.getNumericValue(move.charAt(0)) * 8 + Character.getNumericValue(move.charAt(1));
			end = Character.getNumericValue(move.charAt(2)) * 8 + Character.getNumericValue(move.charAt(3));
		}else if (move.charAt(3) == 'P'){
			if(Character.isUpperCase(move.charAt(2))){
				begin = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(0) - '0'] & Move.RANK_Masks_flip[1]);
				end = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(1) - '0'] & Move.RANK_Masks_flip[0]);
			}else{
				begin = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(0) - '0'] & Move.RANK_Masks_flip[6]);
				end = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(1) - '0'] & Move.RANK_Masks_flip[7]);
			}
			promote_to = ""+Character.toLowerCase(move.charAt(2));
		}
		ucimove += (char)('a' + begin%8);
		ucimove += (char)('8' - begin/8);
		ucimove += (char)('a' + end%8);
		ucimove += (char)('8' - end/8);
		ucimove += promote_to;
		return ucimove;
	}
	
	static void UCIinputToMove(String inputcmd, boolean whitetomove)
	{
		String move = "";
		int from;
		int to;
		from = (inputcmd.charAt(0) - 'a') + (8 * ('8' - inputcmd.charAt(1)));
		to = (inputcmd.charAt(2) - 'a') + (8 * ('8' - inputcmd.charAt(3)));
		if(inputcmd.length() == 5){
			move += from%8;
			move += to%8;
			if(whitetomove){
				move += Character.toUpperCase(inputcmd.charAt(4));
			}else{
				move += inputcmd.charAt(4);
			}
			move += "P";
		}else{
			move = ""+(from/8)+(from%8)+(to/8)+(to%8);
		}
		WP = Move.makemove(WP, move, 'P');
		WN = Move.makemove(WN, move, 'N');
		WB = Move.makemove(WB, move, 'B');
		WR = Move.makemove(WR, move, 'R');
		WQ = Move.makemove(WQ, move, 'Q');
		WK = Move.makemove(WK, move, 'K');
		BP = Move.makemove(BP, move, 'p');
		BN = Move.makemove(BN, move, 'n');
		BB = Move.makemove(BB, move, 'b');
		BR = Move.makemove(BR, move, 'r');
		BQ = Move.makemove(BQ, move, 'q');
		BK = Move.makemove(BK, move, 'k');
		whitemove = !whitemove;
	}

	public static void main(String[] args) {
		GenerateBoard.beginboard();
		communicateToUCI();
	}

}
