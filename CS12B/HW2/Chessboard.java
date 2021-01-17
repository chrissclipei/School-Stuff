//CHris Sclipei
//csclipei
//Emmanuel Huff
//ehuff
//1/31/17
//CMPS12B
//This is our main class which runs the main program by taking user input
//and presenting it to the output file as well as the screen

import javax.xml.soap.Node;
import java.io.*;
import java.util.Scanner;

class Chessboard {
    pieceList pieces;
    int boardSize;

    // A Chess piece

    class Node {
        Chesspiece piece;
        Node nextLink;

        Node(Chesspiece piece) {
            this.piece = piece;
            this.nextLink = null;
        }

    }

    class pieceList {
        Node head;

        //LinkList constructor
        public pieceList() {
            head = null;
        }

        //Returns true if list is empty
        public boolean isEmpty() {
            return head == null;
        }

        //Inserts a new Link at the head of the list
        void insert(Chesspiece piece) {
            Node link = new Node(piece);
            link.nextLink = head;
            head = link;
        }
    }

    Chessboard(int boardSize) {
        this.boardSize = boardSize;
        pieces = new pieceList();
    }

    public boolean checkExistsKings(Node next) {
        int whiteKings;
        int blackKings;

        whiteKings = 0;
        blackKings = 0;
        while (next != null) {
            if (next.piece.pieceType.equals("k")) {
                whiteKings++;
            }
            if (next.piece.pieceType.equals("K")) {
                blackKings++;
            }       
            next = next.nextLink;
        }

        // There must be exactly 1 black and 1 white king.  Anything else is invalid
        if ((whiteKings == 1) && (blackKings ==1)) {
            return true;
        }
        else {
            return false;
        }

    }

    public boolean checkExistsPosition(Node next, int row, int col ) {
        // Check if the same row, col exist anywhere further down the linked list
        boolean foundOne;

        if (next.nextLink == null) {
            return false;
        }
        else if ((next.nextLink.piece.posRow == row) && (next.nextLink.piece.posColumn == col)) {
            return true;
        }
        else {
            //Continue checking through the rest of the linked list
            foundOne = checkExistsPosition(next.nextLink, row, col);
        }

        if (foundOne == true) {
            return true;
        }
        else {
            // Recursively check the rest of the list for any duplicates
            foundOne =  checkExistsPosition(next.nextLink, next.nextLink.piece.posRow, next.nextLink.piece.posColumn);
        }

        return foundOne;
    }

    public boolean checkValidity( ) {
        boolean retVal = false;
        Node thisNode = pieces.head;

        // An empty list has no duplciates but also has no kings
        if (pieces.head == null) {
            return false;
        }

        // starting with the head of the list, check if there are any pieces on the same position further down the list
        if (checkExistsPosition(pieces.head, thisNode.piece.posRow, thisNode.piece.posColumn) == true) {
            return false;
        }

        // Now we now there are no duplicates; check for a king of each color
        if (checkExistsKings(pieces.head)) {
            return true;
        }
        else
        {
            return false;
        }

    }

    public Chesspiece find(int row, int column) {
        Chesspiece returnPiece;
        if (pieces.isEmpty()) {
            return null;
        }
        returnPiece = null;
        for (Node apiecelink = pieces.head; ((apiecelink != null) && (returnPiece == null)); apiecelink = apiecelink.nextLink){
            if ((apiecelink.piece.posRow == row) && (apiecelink.piece.posColumn == column)) {
                returnPiece = apiecelink.piece;
            }
        }

        return returnPiece;
    }

    public static void main(String[] args) throws IOException{
        Chessboard board;
        String color;
        Chesspiece newpiece;
        int i;
        // open input.txt files
        Scanner in = new Scanner(new File("input.txt"));
        PrintWriter writer = new PrintWriter("analysis.txt", "UTF-8");
        // read lines from in, write lines to out
        while( in.hasNextLine() ){
            String line = in.nextLine();
            String[] values = line.split(" ");
            int size = Integer.parseInt(values[0]);
            board = new Chessboard(size);
            i = 1;
            while (i < values.length) {

                if (values[i].equals("k")) {
                    newpiece = new King (values[i], "White", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("K")) {
                    newpiece = new King (values[i], "Black", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("q")) {
                    newpiece = new Queen (values[i], "White", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("Q")) {
                    newpiece = new Queen (values[i], "Black", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("b")) {
                    newpiece = new Bishop (values[i], "White", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("B")) {
                    newpiece = new Bishop (values[i], "Black", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("n")) {
                    newpiece = new Knight (values[i], "White", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("N")) {
                    newpiece = new Knight (values[i], "Black", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("r")) {
                    newpiece = new Rook (values[i], "White", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("R")) {
                    newpiece = new Rook (values[i], "Black", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else if (values[i].equals("p")) {
                    newpiece = new whitePawn (values[i], "White", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                else {
                    newpiece = new blackPawn (values[i], "Black", Integer.parseInt(values[i+1]), Integer.parseInt(values[i+2]));
                }
                board.pieces.insert(newpiece);  // this "places" the piece on the board by inserting it into the linked list
                i = i + 3;   // move to next piece
            }

            // Now we have read in all the pieces.  Next line should contain a square to check
            line = in.nextLine();
            values = line.split(" ");

            if (board.checkValidity()) {
                //find what is at values[0], values[1]
                newpiece = board.find(Integer.parseInt(values[0]),Integer.parseInt(values[1]));
                if (newpiece == null) {
                    System.out.print("-");   //no piece at that position
                    writer.print("-");
                }
                else {
                    System.out.print(newpiece.pieceType);
                    writer.print(newpiece.pieceType);
                }
                // Now find attacking pieces
                Chesspiece attackerPiece;
                Chesspiece attackedPiece;
                Node apiecelink;
                attackerPiece = null;
                attackedPiece = null;
                // Go through the linked list until we find a piece that both attacks and is attacked.
                for (apiecelink = board.pieces.head; ((apiecelink != null) && (attackedPiece == null)); apiecelink = apiecelink.nextLink){
                    attackerPiece = apiecelink.piece; // save this off before For loop changes it.
                    attackedPiece = apiecelink.piece.CheckAllAttacks(board);
                }
                if (attackedPiece == null) {
                    System.out.println(" -");   //no piece attacks another
                    writer.println(" -");   //no piece attacks another
                }
                else {
                    System.out.print(" " + attackerPiece.pieceType + " " + attackerPiece.posRow + " " + attackerPiece.posColumn);
                    writer.print(" " + attackerPiece.pieceType + " " + attackerPiece.posRow + " " + attackerPiece.posColumn);
                    System.out.println(" " + attackedPiece.pieceType + " " + attackedPiece.posRow + " " + attackedPiece.posColumn);
                    writer.println(" " + attackedPiece.pieceType + " " + attackedPiece.posRow + " " + attackedPiece.posColumn);
                }
            }
            else {
                System.out.println("Invalid");
                writer.println("Invalid");
            }

        }
        // close files
        in.close();
        writer.close();

    }
} 
