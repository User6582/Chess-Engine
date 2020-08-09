package com.company;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.floorDiv;


public class Piece {
    static final byte[][] wpMoves = {{8},{7,9}};
    static final byte[][] bpMoves = {{-8},{-7,-9}};
    static final byte[] nMoves = {-17,-15,-6,10,17,15,6,-10};
    static final byte[] bMoves = {-9,-7,7,9};
    static final byte[] rMoves = {-1,1,8,-8};
    static final byte[] qkMoves = {-1,1,8,-8,-9,-7,7,9};

    public static ArrayList<Byte> get_piece_moves(byte[] board, byte piece, byte pos){
        switch(abs(piece)){
            case 1: return moves_p(board, piece, pos);
            case 2: return moves_n(board, piece, pos);
            case 3: case 4: case 5: return moves_straight(board, piece, pos);  //Bishop, queen, rook move in same way
            case 6: return moves_k(board, piece, pos);
        }
        return null;
    }

    private static ArrayList<Byte> moves_p(byte[] board, byte piece, byte pos) {
        ArrayList<Byte> possibleMoves = new ArrayList<>();
        boolean colour = piece > 0;
        byte[][] moves;
        if (colour) {
            moves = wpMoves;
        } else {
            moves = bpMoves;
        }
        byte num = 1;
        if ((Math.floorDiv(pos, 8) == 1 && colour) || (Math.floorDiv(pos, 8) == 6 && !(colour))) { // check if it can move 2
            num = 2;
        }
        byte sqr;
        byte lastPos;
        for (byte move : moves[0]) {
            byte nPos = pos;
            for (byte i = 0; i < num; i++) {
                lastPos = nPos;
                nPos += move;
                if (nPos > -1 && nPos < 64 && lastPos%8-nPos%8==0 && abs(floorDiv(lastPos, 8)- floorDiv(nPos,8))<3){
                    sqr = board[nPos];
                    if (sqr == 0) {
                        possibleMoves.add(nPos);
                    }else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        for (byte move : moves[1]) {
            lastPos = pos;
            byte nPos = pos;
            nPos += move;
            if (nPos > -1 && nPos < 64 && abs(lastPos%8-nPos%8)<2 && abs(floorDiv(lastPos, 8)- floorDiv(nPos,8))<2){
                sqr = board[nPos];
                if (sqr != 0 && colour != (sqr > 0)) {
                    possibleMoves.add(nPos);
                } else if (board[66] == nPos && colour!=board[board[66]+8*-1*piece]>0) { //cleanup
                    possibleMoves.add(nPos);
                }
            }
        }
        return possibleMoves;
    }


    private static ArrayList<Byte> moves_k(byte[] board, byte piece, byte pos){ //add castling
        boolean colour = piece>0;
        ArrayList<Byte> possibleMoves = new ArrayList<>();
        byte sqr;
        byte lastPos;
        for(byte move: qkMoves){
            lastPos = pos;
            byte nPos = pos;
            nPos+=move;
            if (nPos > -1 && nPos < 64 && abs(lastPos%8-nPos%8) <2 && abs(floorDiv(lastPos, 8)- floorDiv(nPos,8))<2){
                sqr = board[nPos];
                if (sqr == 0 || colour != (sqr>0)){
                    possibleMoves.add(nPos);
                }
            }
        }
        byte num = 4;
        if(colour){
            num = 2;
        }
        for(byte i = (byte) (num-2); i<num; i++){
            if (Board.can_castle(board[65], i)){
                if (i%2==0 && board[pos+1]==0 && board[pos+2]==0){
                    possibleMoves.add((byte)(pos+2));
                }
                else if(board[pos+-1]==0 && board[pos-2]==0 && board[pos-3]==0){
                    possibleMoves.add((byte)(pos-2));
                }
            }
        }
        return possibleMoves;
    }


    private static ArrayList<Byte> moves_n(byte[] board, byte piece, byte pos){ //add castling
        boolean colour = piece>0;
        ArrayList<Byte> possibleMoves = new ArrayList<>();
        byte lastPos;
        byte sqr;
        for(byte move: nMoves){
            lastPos = pos;
            byte nPos = pos;
            nPos += move;
            if (nPos > -1 && nPos < 64 && abs(lastPos%8-nPos%8) < 3 && abs(floorDiv(lastPos, 8)- floorDiv(nPos,8))<3){
                sqr = board[nPos];
                if (sqr == 0 || colour != (sqr>0)){
                    possibleMoves.add(nPos);
                }
            }
        }
        return possibleMoves;
    }


    private static ArrayList<Byte> moves_straight(byte[] board, byte piece, byte pos){
        boolean colour = piece>0;
        byte[] moves = new byte[0];
        switch (abs(piece)){
            case 3: moves = bMoves; break;
            case 4: moves = rMoves; break;
            case 5: moves = qkMoves; break;
        }
        ArrayList<Byte> possibleMoves = new ArrayList<>();
        byte sqr;
        byte lastPos;
        for(byte move: moves){
            byte nPos = pos;
            for(byte i = 0; i<8; i++){
                lastPos = nPos;
                nPos+=move;
                if (nPos > -1 && nPos < 64 && abs(lastPos%8-nPos%8) < 2 && abs(floorDiv(lastPos, 8)- floorDiv(nPos,8))<2){ // checks it hasnt gone off board
                    sqr = board[nPos];
                    if (sqr == 0){
                        possibleMoves.add(nPos);
                    }
                    else if(colour != (sqr>0)){
                        possibleMoves.add(nPos);
                        break;
                    }
                    else{
                        break;
                    }
                }
                else{break;}
            }
        }
        return possibleMoves;
    }

}
