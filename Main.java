package com.company;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        File file = new File(System.nanoTime()+".txt");
        byte [] board = new byte[0];
        new Board();
        //System.out.println(perft(board, 6));
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String uciIn = null;
            try {
                uciIn = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(uciIn);
            if("uci".equals(uciIn)){
                //setup
                System.out.println("id name"+ "Jack's engine");
                System.out.println("id author Jack");
                System.out.println("uciok");
            }
            else if(uciIn.startsWith("setoption")){
                System.out.println("SetOptions");
                //set options
            }
            else if("ucinewgame".equals(uciIn)){
                System.out.println("newGame");
                //makes new game clears tables and stuff
            }
            else if("isready".equals(uciIn)){
                //print is ready
                System.out.println("readyok");
            }
            else if(uciIn.startsWith("position")){
                if(uciIn.contains("startpos")){
                    board = Board.board_from_FEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                }
                else if(uciIn.contains("fen")){
                    String FEN = uciIn.substring(uciIn.indexOf("fen")+4);
                    board = Board.board_from_FEN(FEN);
                }
                else{
                    System.out.println("ERROR incorrect command");
                    System.exit(1);
                }
                if(uciIn.contains("moves")){
                    String moveStr = uciIn.substring(uciIn.indexOf("moves")+6);
                    String[] moves = moveStr.split(" ");
                    for(String move: moves){
                        byte[] moveB = Board.SAN_to_move(board, move);
                        boolean moved = Board.make_any_move(board, moveB[0], moveB[1], moveB[2]);
                        if (!moved) {
                            System.out.println("ERROR incorrect move");
                            System.exit(1);
                        }
                    }
                }
                System.out.println("Position Set");
            }
            else if(uciIn.startsWith("go")){
                System.out.println("SEARCHING...");
                int[][] negaResult = negamax(board, (byte) 5, -2147483647, 2147483647);
                int[] moves = negaResult[1];
                System.out.println("bestmove "+ Board.move_to_SAN(new byte[]{(byte)moves[0],(byte)moves[1],(byte)moves[2]}));
                try{
                    FileWriter writer = new FileWriter(file.getName());
                    writer.write(Board.board_to_FEN(board, 1));
                    for(int i = 0;i<moves.length;i+=3){
                        writer.write(Board.move_to_SAN(new byte[]{(byte)moves[i],(byte)moves[i+1],(byte)moves[i+2]}));
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            /*long sTime = System.nanoTime();
            int[][] negaResult = negamax(board, (byte) 5, -2147483647, 2147483647);
            long eTime = System.nanoTime();
            int[] moves = negaResult[1];
            try{
                FileWriter writer = new FileWriter(file.getName());
                writer.write(Board.board_to_FEN(board, 1));
                for(int i = 0;i<moves.length;i+=3){
                    writer.write(Board.pos_to_SAN((byte) moves[i+1]) + Board.pos_to_SAN((byte) moves[i+2]));
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            Board.make_legal_move(board, (byte)moves[0], (byte)moves[1], (byte)moves[2]);
            System.out.println("time: " + ((eTime - sTime) / 1000000));
            System.out.println(Board.board_to_FEN(board, 1));
            System.out.println("eval: " + negaResult[0][0]);
            System.out.println(Board.pos_to_SAN((byte) moves[1]) + Board.pos_to_SAN((byte) moves[2]));
            player_move(board);*/
        }
    }

    /*public static void player_move(byte[] board){
        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                String uMove = in.next();
                byte start = Board.SAN_to_pos(uMove.substring(0, 2));
                byte end = Board.SAN_to_pos(uMove.substring(2, 4));
                boolean moved = Board.make_any_move(board, board[start], start, end);
                if (moved) {
                    return;
                }
                else{
                    System.out.println("INCORRECT MOVE");
                }
            }
            catch (Exception e){
                System.out.println("INCORRECT MOVE");
            }

        }
    }*/

    public static int[][] negamax(byte[] board, byte depth, int alpha, int beta){
        if(depth==0){
            byte gameOver = Board.game_over_yn(board);
            if (gameOver == 0){
                return new int[][] {{Board.eval(board)*board[64]},{}};
            }
            else if(gameOver == 1){
                return new int[][] {{-100000},{}};
            }
            else{
                return new int[][] {{0},{}};
            }
        }
        ArrayList<byte[]> possibleMoves = Board.get_possible_full_moves(board);
        int maxEval = -2147483647;
        int[] bestoldMove = new int[0];
        byte[] bestMove = new byte[0];
        for(byte[] move: possibleMoves){
            byte[] nBoard = Board.copy_board(board);
            boolean moveB = Board.make_legal_move(nBoard, move[0], move[1], move[2]);
            if(moveB){
                int[][] result = negamax(nBoard, (byte) (depth-1), -beta, -alpha);
                result[0][0] *= -1;
                if (result[0][0] > maxEval){
                    bestoldMove = result[1];
                    bestMove = move;
                    maxEval = result[0][0];
                    alpha = Math.max(alpha, maxEval);
                }
                if(beta<=alpha){
                    int[] oldMoves = new int[depth*3];
                    for(byte i=0; i<bestoldMove.length; i++){
                        oldMoves[i]=bestoldMove[i];
                    }
                    for(byte i=0; i<3; i++){
                        oldMoves[i+bestoldMove.length] = bestMove[i];
                    }
                    return new int[][] {{maxEval}, oldMoves};
                }
            }
        }
        if(maxEval!=-2147483647){
            int[] oldMoves = new int[depth*3];
            for(byte i=0; i<3; i++){
                oldMoves[i] = bestMove[i];
            }
            for(byte i=0; i<bestoldMove.length; i++){
                oldMoves[i+3]=bestoldMove[i];
            }
            return new int[][] {{maxEval}, oldMoves};
        }
        else{
            if(Board.in_check(board)){
                return new int[][] {{-100000},{}};
            }
            else{
                return new int[][] {{0},{}};
            }
        }


    }


    public static int perft(byte[] board, int depth){
        if (depth==0){
            return 1;
        }
        int nodes = 0;
        ArrayList<byte[]> moves = Board.get_possible_full_moves(board);
        for(byte[] move: moves){
            //System.out.println(Board.numToPiece.get(move[0])+Board.pos_to_SAN(move[2]));
            byte[] nBoard = Board.copy_board(board);
            boolean result = Board.make_legal_move(nBoard,move[0], move[1], move[2]);
            if (result){
                nodes += perft(nBoard, depth-1);
            }
        }
        return nodes;
    }


}
