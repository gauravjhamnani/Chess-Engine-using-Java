
public class Search {
	
	public static int Max_depth = 4;
	public static int White = 1;
	public static int Black = 0;
	
	public static String get_bestmove(long WK,
			long WQ, long WR,long WB,long WN,long WP,long BK,long BQ,
			long BR,long BB,long BN,long BP, boolean WhiteMove){
		int depth = 0;
		int player;
		if(WhiteMove){
			player = White;
		}else{
			player = Black;
		}
		String bestmove = minmax_alphabeta(-10000,10000, WK,WQ,WR,WB,WN,WP,BK,
				BQ,BR,BB,BN,BP,player,depth,"");
		return bestmove;
	}
	public static String minmax_alphabeta(int alpha, int beta, long WK,
			long WQ, long WR,long WB,long WN,long WP,long BK,long BQ,
			long BR,long BB,long BN,long BP, int player, int depth, String moves){
		long WKt = 0l, WQt = 0l, WRt = 0l, WBt = 0l, WNt = 0l, WPt = 0l;
		long BKt = 0l, BQt = 0l, BRt = 0l, BBt = 0l, BNt = 0l, BPt = 0l;
		int bestscore;
		String bestmove = "0000";
		String move = "0000";
		String alphamove = "0000";
		String betamove = "0000";
		if(depth == Max_depth){
			bestscore = Evaluation.rating(WK,WQ,WR,WB,WN,WP,BK,
					BQ,BR,BB,BN,BP);
			bestmove = moves+bestscore;
			return bestmove;
		}
		
		if(player == White){
			moves = Move.MovesAvailableW(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);
		}else{
			moves = Move.MovesAvailableB(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);
		}
		
		for(int i = 0; i < moves.length(); i+=4){
			move = moves.substring(i, i+4);
			WPt = Move.makemove(WP, move, 'P');
			WNt = Move.makemove(WN, move, 'N');
			WBt = Move.makemove(WB, move, 'B');
			WRt = Move.makemove(WR, move, 'R');
			WQt = Move.makemove(WQ, move, 'Q');
			WKt = Move.makemove(WK, move, 'K');
			BPt = Move.makemove(BP, move, 'p');
			BNt = Move.makemove(BN, move, 'n');
			BBt = Move.makemove(BB, move, 'b');
			BRt = Move.makemove(BR, move, 'r');
			BQt = Move.makemove(BQ, move, 'q');
			BKt = Move.makemove(BK, move, 'k');
			
			bestmove = minmax_alphabeta(alpha, beta, WKt,WQt,WRt,WBt,WNt,WPt,BKt,
					BQt,BRt,BBt,BNt,BPt,player^1,depth+1,move);
			bestscore = Integer.valueOf(bestmove.substring(4));
			if(player == Black){
				if(bestscore < beta){
					beta = bestscore;
					betamove = move;
				}
			}else{
				if(bestscore > alpha){
					alpha = bestscore;
					alphamove = move;
				}
			}
			
			if(alpha >= beta){
				if(player == Black){
					return betamove+beta;
				}else{
					return alphamove+alpha;
				}
			}
		}
		if(player == Black){
			return betamove+beta;
		}else{
			return alphamove+alpha;
		}
	}
	
	public static int ischeck(long WK,
			long WQ, long WR,long WB,long WN,long WP,long BK,long BQ,
			long BR,long BB,long BN,long BP, int player){
		int bestscore = 0;
		//if(player == White){
			long Blackatt_map = Move.attackbitmapB(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);
			if((WK & Blackatt_map) != 0){
				bestscore = 5000;
			}
		//}else{
			long Whiteatt_map = Move.attackbitmapW(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);
			if((BK & Whiteatt_map) != 0){
				bestscore = -5000;
			}
		//}
		
		return bestscore;
		
	}

}
