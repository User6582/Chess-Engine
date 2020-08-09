package com.company;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Math.floorDiv;


public class Board {
    static final HashMap<Byte, Character> numToPiece =  new HashMap<Byte, Character>(){{put((byte)0,'0');put((byte)1,'P');
    put((byte)2,'N');put((byte)3,'B');put((byte)4,'R');put((byte)5,'Q');put((byte)6,'K');
    put((byte)-1, 'p');put((byte)-2,'n');put((byte)-3,'b');put((byte)-4,'r');put((byte)-5,'q'); put((byte)-6,'k');}};
    static final HashMap<Character, Byte> pieceToNum = new HashMap<Character, Byte>();
    static final String[] cRights = {"K", "Q", "k", "q"};
    static final HashMap<Byte, Integer> points = new HashMap<Byte, Integer>(){{put((byte)1,100);
        put((byte)2,320);put((byte)3,330);put((byte)4,500);put((byte)5,900);put((byte)6,0);
        put((byte)-1, -100);put((byte)-2,-320);put((byte)-3,-330);put((byte)-4,-500);put((byte)-5,-900); put((byte)-6,0);}};

    static final HashMap<Byte, byte[]> pieceSquare = new HashMap<Byte, byte[]>(){{
        put((byte)-1, new byte[] {0,   0,   0,   0,   0,   0,   0,   0, -50, -50, -50, -50, -50,
                -50, -50, -50, -10, -10, -20, -30, -30, -20, -10, -10,  -5,  -5,
                -10, -25, -25, -10,  -5,  -5,   0,   0,   0, -20, -20,   0,   0,
                0,  -5,   5,  10,   0,   0,  10,   5,  -5,  -5, -10, -10,  20,
                20, -10, -10,  -5,   0,   0,   0,   0,   0,   0,   0,   0});
        put((byte)-2,new byte[] {50,  40,  30,  30,  30,  30,  40,  50,  40,  20,   0,   0,   0,
                0,  20,  40,  30,   0, -10, -15, -15, -10,   0,  30,  30,  -5,
                -15, -20, -20, -15,  -5,  30,  30,   0, -15, -20, -20, -15,   0,
                30,  30,  -5, -10, -15, -15, -10,  -5,  30,  40,  20,   0,  -5,
                -5,   0,  20,  40,  50,  40,  30,  30,  30,  30,  40,  50});
        put((byte)-3,new byte[] {20,  10,  10,  10,  10,  10,  10,  20,  10,   0,   0,   0,   0,
                0,   0,  10,  10,   0,  -5, -10, -10,  -5,   0,  10,  10,  -5,
                -5, -10, -10,  -5,  -5,  10,  10,   0, -10, -10, -10, -10,   0,
                10,  10, -10, -10, -10, -10, -10, -10,  10,  10,  -5,   0,   0,
                0,   0,  -5,  10,  20,  10,  10,  10,  10,  10,  10,  20});
        put((byte)-4,new byte[] {0,   0,   0,   0,   0,   0,   0,   0,  -5, -10, -10, -10, -10,
                -10, -10,  -5,   5,   0,   0,   0,   0,   0,   0,   5,   5,   0,
                0,   0,   0,   0,   0,   5,   5,   0,   0,   0,   0,   0,   0,
                5,   5,   0,   0,   0,   0,   0,   0,   5,   5,   0,   0,   0,
                0,   0,   0,   5,   0,   0,   0,  -5,  -5,   0,   0,   0});
        put((byte)-5,new byte[] {20, 10, 10,  5,  5, 10, 10, 20, 10,  0,  0,  0,  0,  0,  0, 10, 10,
                0, -5, -5, -5, -5,  0, 10,  5,  0, -5, -5, -5, -5,  0,  5,  0,  0,
                -5, -5, -5, -5,  0,  5, 10, -5, -5, -5, -5, -5,  0, 10, 10,  0, -5,
                0,  0,  0,  0, 10, 20, 10, 10,  5,  5, 10, 10, 20});
        put((byte)-6, new byte[]{30,  40,  40,  50,  50,  40,  40,  30,  30,  40,  40,  50,  50,
                40,  40,  30,  30,  40,  40,  50,  50,  40,  40,  30,  30,  40,
                40,  50,  50,  40,  40,  30,  20,  30,  30,  40,  40,  30,  30,
                20,  10,  20,  20,  20,  20,  20,  20,  10, -20, -20,   0,   0,
                0,   0, -20, -20, -20, -30, -10,   0,   0, -10, -30, -20});
        put((byte)1, new byte[] {0,   0,   0,   0,   0,   0,   0,   0,   5,  10,  10, -20, -20,
                10,  10,   5,   5,  -5, -10,   0,   0, -10,  -5,   5,   0,   0,
                0,  20,  20,   0,   0,   0,   5,   5,  10,  25,  25,  10,   5,
                5,  10,  10,  20,  30,  30,  20,  10,  10,  50,  50,  50,  50,
                50,  50,  50,  50,   0,   0,   0,   0,   0,   0,   0,   0});
        put((byte)2, new byte[] {-50, -40, -30, -30, -30, -30, -40, -50, -40, -20,   0,   5,   5,
                0, -20, -40, -30,   5,  10,  15,  15,  10,   5, -30, -30,   0,
                15,  20,  20,  15,   0, -30, -30,   5,  15,  20,  20,  15,   5,
                -30, -30,   0,  10,  15,  15,  10,   0, -30, -40, -20,   0,   0,
                0,   0, -20, -40, -50, -40, -30, -30, -30, -30, -40, -50});
        put((byte)3,new byte[]{-20, -10, -10, -10, -10, -10, -10, -20, -10,   5,   0,   0,   0,
                0,   5, -10, -10,  10,  10,  10,  10,  10,  10, -10, -10,   0,
                10,  10,  10,  10,   0, -10, -10,   5,   5,  10,  10,   5,   5,
                -10, -10,   0,   5,  10,  10,   5,   0, -10, -10,   0,   0,   0,
                0,   0,   0, -10, -20, -10, -10, -10, -10, -10, -10, -20});
        put((byte)4,new byte[]{0,  0,  0,  5,  5,  0,  0,  0, -5,  0,  0,  0,  0,  0,  0, -5, -5,
                0,  0,  0,  0,  0,  0, -5, -5,  0,  0,  0,  0,  0,  0, -5, -5,  0,
                0,  0,  0,  0,  0, -5, -5,  0,  0,  0,  0,  0,  0, -5,  5, 10, 10,
                10, 10, 10, 10,  5,  0,  0,  0,  0,  0,  0,  0,  0});
        put((byte)5,new byte[]{20, -10, -10,  -5,  -5, -10, -10, -20, -10,   0,   5,   0,   0,
                0,   0, -10, -10,   5,   5,   5,   5,   5,   0, -10,   0,   0,
                5,   5,   5,   5,   0,  -5,  -5,   0,   5,   5,   5,   5,   0,
                -5, -10,   0,   5,   5,   5,   5,   0, -10, -10,   0,   0,   0,
                0,   0,   0, -10, -20, -10, -10,  -5,  -5, -10, -10, -20});
        put((byte)6,new byte[]{20,  30,  10,   0,   0,  10,  30,  20,  20,  20,   0,   0,   0,
                0,  20,  20, -10, -20, -20, -20, -20, -20, -20, -10, -20, -30,
                -30, -40, -40, -30, -30, -20, -30, -40, -40, -50, -50, -40, -40,
                -30, -30, -40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50,
                -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30});
    }};



    public Board() {
        for (byte num : numToPiece.keySet()) {
            pieceToNum.put(numToPiece.get(num), num);
        }
    }


    public static boolean make_legal_move(byte[] board, byte piece, byte posA, byte posB, boolean move){
        byte colour = board[64];
        boolean result = false;
        byte[] castleChange = new byte[] {-1,-1};
        byte oldPiece;
        byte oldEnPassant = board[66];
        oldPiece = board[posB];
        board[66] = 0;
        board[posA] = 0;
        board[posB] = piece;
        if(abs(piece)==1 && (floorDiv(posB, 8) == 7 || floorDiv(posB, 8) == 0)){ // Upgrades
            board[posB] = (byte)(5*piece);
        }
        else if(abs(piece)==6){
            if (piece > 0 && (can_castle(board[65],(byte)0) || can_castle(board[65],(byte)1))) {
                    castleChange[0] = 0;
                    castleChange[1] = 1;
            }
            else if(can_castle(board[65],(byte)2) || can_castle(board[65],(byte)3)) {
                    castleChange[0] = 2;
                    castleChange[1] = 3;
            }
            if (abs(posA - posB) == 2) { //check for castle
                if (in_check(board)){
                    result = false;
                }
                else {
                    if (posA - posB == -2 && can_castle(board[65], castleChange[0])) {//Kingside castle
                        board[posA + 3] = 0;
                        board[posA + 1] = (byte) (4 * board[64]); //Maybe change to if statements for colour
                    } else if (posA - posB == 2 && can_castle(board[65], (byte) (castleChange[1]))) {//Queenside castle
                        board[posA - 4] = 0;
                        board[posA - 1] = (byte) (4 * board[64]);
                    }
                }
            }
            for(byte castle1: castleChange){
                if(castle1!=-1){
                    change_castle(board, castle1, (byte)0);
                }
            }
        }
        else if(abs(piece)==4){//make more efficient
            switch (posA){
                case 7: castleChange[0] = 0; break;
                case 0: castleChange[0] = 1; break;
                case 63: castleChange[0] = 2; break;
                case 56: castleChange[0] = 3; break;
            }
            if (castleChange[0] != -1) {
                if (can_castle(board[65], castleChange[0])) {
                    change_castle(board, castleChange[0], (byte) 0);
                }
                else {
                    castleChange[0] = -1;
                }
            }
        }
        else if(abs(piece)==1 && abs(posA-posB)==16){
            board[66] = (byte) (posA + (8*colour));
        }
        else if(abs(piece)==1 && oldPiece==0 && abs(posA-posB)%8!=0){
            board[posB-(8*piece)] = 0;
        }
        if(!in_check(board)) {
            result = true;
            if (!move){
                undo_move(board, piece, posA, posB, oldPiece, oldEnPassant, castleChange);
            }
            else { board[64] *= -1; }
        }
        else{
            undo_move(board, piece, posA, posB, oldPiece, oldEnPassant, castleChange);
        }
        return result;
    }

    public static boolean make_legal_move(byte[] board, byte piece, byte posA, byte posB){
        return make_legal_move(board, piece,posA,posB, true);
    }

    public static boolean make_any_move(byte[] board, byte piece, byte posA, byte posB){
        if(piece != 0 && Piece.get_piece_moves(board, piece, posA).contains(posB)){
            return make_legal_move(board, piece, posA, posB);
        }
        else{
            return false;
        }
    }


    public static void undo_move(byte[] board, byte piece, byte posA, byte posB, byte oldPiece, byte oldEnpassant, byte[] castle){
        byte colour = board[64];
        byte castleChange = -1;
        board[66] = oldEnpassant;
        board[posA] = piece;
        board[posB] = oldPiece;
        if(abs(piece)==6){
            if (abs(posA - posB) == 2) {
                //check for castle
                if (posA - posB == -2) {//Kingside castle
                    board[posA + 3] = (byte) (4 * board[64]);
                    board[posA + 1] = 0; //Maybe change to if statements for colour
                } else if (posA - posB == 2) {//Queenside castle
                    board[posA - 4] = (byte) (4 * board[64]);
                    board[posA - 1] = 0;
                }
            }
        }
        else if(abs(piece)==1 && board[posB]==0 && abs(posA-posB)%8!=0){
            board[posB-8*-1*piece] = oldPiece;
            board[posB] = 0;
        }
        for(byte castle1 : castle){
            if (castle1 != -1){
                change_castle(board, castle1, (byte)1);
            }
        }
    }


    public static boolean in_check(byte[] board){
        board[64] *= -1;
        ArrayList<Byte> possibleMoves = get_possible_moves(board);
        board[64] *= -1;
        byte king = (byte)(6*board[64]);
        byte kingPos = 0;
        for (byte i =0;i<64; i++){
            if(board[i]==king){
                kingPos = i;
                break;
            }
        }
        return possibleMoves.contains(kingPos);
    }


    public static byte game_over_yn(byte[] board){
        if (no_legal_moves(board)){
            if (in_check(board)){
                return 1;//Checkmate
            }
            else{
                return 2;//Stalemate
            }
        }
        return 0;//Game not over
    }

    public static boolean no_legal_moves(byte[] board){
        byte piece;
        boolean colour = board[64]>0;
        ArrayList<Byte> pMoves;
        boolean legal;
        for(byte i = 0; i<64; i++){
            piece = board[i];
            if (piece != 0 && (colour==piece>0)){
                pMoves = Piece.get_piece_moves(board, piece, i);
                for(byte move: pMoves){
                    legal = make_legal_move(board, piece, i, move, false);
                    if(legal){
                        return false;
                    }
                }
            }
        }
        return true;
    }



    public static ArrayList<Byte> get_possible_moves(byte[] board){
        ArrayList<Byte> moves = new ArrayList<>();
        byte piece;
        boolean colour = board[64]==1;
        for(byte i = 0; i<64; i++){
            piece = board[i];
            if (piece != 0 && (colour==piece>0)){
                moves.addAll(Piece.get_piece_moves(board, piece, i));
            }
        }
        return moves;
    }

    public static ArrayList<byte[]> get_possible_full_moves(byte[] board){
        ArrayList<byte[]> moves = new ArrayList<>();
        byte piece;
        boolean colour = board[64]==1;
        ArrayList<Byte> pMoves;
        for(byte i = 0; i<64; i++){
            piece = board[i];
            if (piece != 0 && (colour==piece>0)){
                pMoves = Piece.get_piece_moves(board, piece, i);
                for(byte move: pMoves){
                    moves.add(new byte[] {piece, i, move});
                }
            }
        }
        return moves;
    }


    public static int eval(byte[] board){
        int score = 0;
        byte sqr;
        for(byte i = 0;i<64;i++){
            sqr = board[i];
            if(sqr!=0){
                score += points.get(sqr);
                score += pieceSquare.get(sqr)[i];
            }
        }
        return score;
    }

    public static byte[] make_start(){
        byte[] board = new byte[68];
        //Arrays.fill(board, (byte)0);
        for(int i = 0; i<8; i++) {
            byte piece;
            switch (i) {
                case 7: case 0: piece = 4; break;
                case 6: case 1: piece = 2; break;
                case 5: case 2: piece = 3; break;
                case 3: piece = 5; break;
                case 4: piece = 6; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }
            board[i] = piece;
            board[i+56] = (byte) (piece * -1);
            board[i+8] = 1;
            board[i+48] = -1;
        }
        board[64] = 1;// 64: Move, 1-white, -1-black
        board[65] = 15; //Bitwise operations 64 - Castling
        //66 En passant
        //67 halfmove clock
        return board;
    }

    public static byte[] board_from_FEN(String FEN){
        byte[] board = new byte[68];
        String boardFEN = FEN.split(" ")[0];
        int ind = 0;
        for(int i = 0; ind<64; i++) {
            char c = boardFEN.charAt(i);
            if (Character.isDigit(c)) {
                ind += (Character.getNumericValue(c)) ;
            } else if (c != '/') {
                board[56 - ((floorDiv(ind, 8) * 8) - ind % 8)] = pieceToNum.get(c);
                ind++;
            }
        }
        String colour = FEN.split(" ")[1];
        switch (colour) {
            case "w": board[64] = 1; break;
            case "b": board[64] = -1; break;
        }
        String castling = FEN.split(" ")[2];
        byte pos = 0;
        for (String cRight: cRights){
            if (castling.contains(cRight)){
                 change_castle(board, pos, (byte)1);
            }
            pos ++;
        }
        String enPassant = FEN.split(" ")[3];
        if (enPassant.length() == 2){
            board[66] = SAN_to_pos(FEN.split(" ")[3]);
        }
        board[67] = Byte.parseByte(FEN.split(" ")[4]);
        return board;
    }


    public static String board_to_FEN(byte[] board, int moves){ //terrible way of doing it but not used often
        String FEN = "";
        for(int i=0;i<64;i++) {
            if (i % 8 == 0 && i != 0) {
                FEN += "/";
            }
            if (board[i] == 0 && FEN.length() != 0 && Character.isDigit(FEN.charAt(FEN.length() - 1))) {
                FEN = FEN.substring(0, FEN.length() - 1) + (Character.getNumericValue(FEN.charAt(FEN.length() - 1)) + 1); // may need to convert second part to strin
            } else if (board[i] == 0) {
                FEN += "1";
            }
            else{
                FEN += numToPiece.get(board[i]);
            }
        }
        String[] FENArray = FEN.split("/");
        String[] FENArrayRev = new String[8];
        for(int i = 7;i>=0;i--){
            FENArrayRev[i] = FENArray[7-i];
        }
        FEN = String.join("/", FENArrayRev);
        char turn;
        switch (board[64]){
            case -1: turn = 'b'; break;
            case 1: turn = 'w'; break;
            default:
                throw new IllegalStateException("Unexpected value: " + board[64]);
        }
        String castlingStr = "";
        for(byte i = 0;i<4;i++) {
            if (can_castle(board[65], i)) {
                castlingStr += cRights[i];
            }
        }
        if (castlingStr == ""){
            castlingStr = "-";
        }
        String enPassant;
        if (board[66] == 0){
            enPassant = "-";
        }
        else {
            enPassant = pos_to_SAN(board[66]);
        }
        FEN = String.format("%s %c %s %s %o %o", FEN, turn, castlingStr, enPassant, board[67], moves);
        return FEN;
    }


    public static boolean can_castle(byte castle, byte pos){
        return (((castle >> pos) & 1) == 1);
    }

    public static void change_castle(byte[] board, byte pos, byte change){
        if (change==0){
            board[65] &= ~(1 << pos);
        }
        else{
            board[65] |= 1 << pos;
        }
    }


    public static byte SAN_to_pos(String SAN){
        char file = SAN.charAt(0);
        int rankNum = (int)Character.getNumericValue(SAN.charAt(1))-1;
        int fileNum = file - 'a';
        byte pos = (byte)(rankNum*8 + fileNum);
        return pos;
    }

    public static byte[] SAN_to_move(byte[] board, String SAN){
        byte start = Board.SAN_to_pos(SAN.substring(0, 2));
        byte end = Board.SAN_to_pos(SAN.substring(2, 4));
        return new byte[] {board[start], start, end};
    }

    public static String move_to_SAN(byte[] move){
        return Board.pos_to_SAN(move[1]) + Board.pos_to_SAN(move[2]);
    }


    public static String pos_to_SAN(byte pos){
        int fileNum = pos%8;
        int rankNum = floorDiv(pos, 8)+1;
        char file = (char)fileNum;
        file += 'a';
        String rank = String.valueOf(rankNum);
        String SAN = file + rank;
        return SAN;
    }


    public static byte[] copy_board(byte[] board){
        byte[] nBoard = new byte[68];
        for(byte i = 0; i<68; i++){
            nBoard[i] = board[i];
        }
        return nBoard;
    }






}
