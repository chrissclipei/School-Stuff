//Emmanuel Huff 
//ehuff
//Chris Sclipei 
//csclipei
//HW 3
//2/19/2017
//Node.class
//Sets up the chess board and pieces

import java.io.*;

class ChessBoard extends ChessPiece {

    private static Node head; // linkedlist to store chesspieces
    public static int board_size; // board_size
    public static BufferedWriter writer; // write to write to file

    // constructor
    public ChessBoard() {
        head = new Node();
    }

    // Method to perform insertion at the front of the list
    // Input: Node to be inserted
    // Output: void
    public Node insert(Node piece) {
        Node temp = head.getNext();
        head.setNext(piece);
        piece.setNext(temp);
        return head;
    }

    public void remove(Node pieceToDelete){
        Node piece = head.getNext();
        Node prevPiece = head;
        boolean foundIt = false;

        while((piece != null) && (!foundIt))  {
            if(piece.getRow() == pieceToDelete.getRow() && piece.getCol() == pieceToDelete.getCol()) {
                foundIt = true;
            }
            else {
                prevPiece = piece;
                piece = piece.getNext();
            }
        }

        if (foundIt) {
            prevPiece.setNext(piece.getNext());  // this is the delete
        }

    }

    // Method to find Node in a given location
    // Input: integer row and column to look for
    // Output: Node found
    public Node findChessPiece(int row, int col) {
        Node piece = head.getNext();
        while(piece != null) {
            if(piece.getRow() == row && piece.getCol() == col) {
                return piece;
            }
            piece = piece.getNext();
        }
        return null;
    }

    // Method to count the number of chesspieces for a given type
    // This method will helps us check the validity case
    // Input: character color
    // Output: returns the count
    public int countPiecesOfType(char pieceType) {
        Node piece = head.getNext();
        int pieceCtr = 0;
        // loop through to check if the same piece type is found
        while(piece != null) {
            if(Utilities.returnChessPieceType(piece) == pieceType) {
                pieceCtr++;
            }
            piece = piece.getNext();
        }
        return pieceCtr;
    }

    public Node findPieceOfType(char pieceType) {
        boolean found = false;

        Node piece = head.getNext();
        int pieceCtr = 0;
        // loop through to check if the same piece type is found
        while((piece != null) && (!found)) {
            if(Utilities.returnChessPieceType(piece) == pieceType) {
                found = true;
            }
            else {
                piece = piece.getNext();
            }
        }
        return piece;
    }
    // Method to count the number of chesspieces on a single location
    // This method will helps us check the validity case
    // Input: integer row and column
    // Output: returns the count
    public int countPiecesInLocation(int row, int col) {
        Node piece = head.getNext();
        int pieceCtr = 0;
        // loop through to check if any two pieces overlap
        while(piece != null) {
            if(piece.getRow() == row && piece.getCol() == col) {
                pieceCtr++;
            }
            piece = piece.getNext();
        }
        return pieceCtr;
    }

    // Method to check if two pieces occupy the same place
    // This method utilizes the countPiecesInLocation method to see
    // if there are more than two pieces in a single location
    // Input: none
    // Output: returns true if two pieces occupy same position
    public boolean twoPiecesOccupySamePosition() {
        Node piece = head.getNext();
        // loop through and see if any of the pieces overlap
        while(piece != null) {
            if(countPiecesInLocation(piece.getRow(), piece.getCol()) > 1) {
                return true;
            }
            piece = piece.getNext();
        }
        return false;
    }

    // Method to check validity
    // basically looks if there are not two chesspieces in the same location and
    // one each colored king is present
    // Input: none
    // Output: returns if it is valid or not
    public boolean checkValidity() {
        if(!twoPiecesOccupySamePosition() && countPiecesOfType('k') == 1 && countPiecesOfType('K') == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // Method to check if any piece exists in the query location
    // Input: integer array query
    // Output: returns the piece (in character) if found otherwise just returns '-'
    public char findChessPiece(int[] query) {
        int col = query[0];
        int row = query[1];
        Node foundPiece = findChessPiece(row, col);
        if ( foundPiece != null) {
            return Utilities.returnChessPieceType(foundPiece);
        }
        return '-';
    }

    // Method to check if two nodes given are different or the same ones
    // It serves as a helper when trying to find the attack
    // Input: two nodes
    // Output: returns if they are same or different pieces
    public boolean isDifferent(Node one, Node other) {
        if(one.getRow() == other.getRow() && one.getCol() == other.getCol() && one.getColor() == other.getColor()) {
            return false;
        }
        return true;
    }

    // Method to see if any of the pieces attack
    // as soon as you encounter the first attack, just print it and return
    // Input: none
    // Output: returns nothing
    public void isOneAttackingOther() {
        // get the first valid chesspiece (remember not the head)
        Node piece = head.getNext();
        // loop through each of the remaining chesspieces and check for attack
        while(piece != null) {
            Node other = head.getNext();
            while(other != null) {
                if(isDifferent(piece, other) && piece.getChessPiece().isAttacking(this, other.getRow(), other.getCol())) {
                    writeToAnalysisFile(Utilities.returnChessPieceType(piece) + " " + piece.getCol() + " " + piece.getRow() + " " + Utilities.returnChessPieceType(other) + " " + other.getCol() + " " + other.getRow() + "\n");
                    return;
                }
                other = other.getNext();
            }
            piece = piece.getNext();
        }
        writeToAnalysisFile("-\n");
    }

    // Method to see if any of the pieces attack a particular piece (ie a king)
    // Input: the row and col you want to check if it is attacked.  
    // Output: the attacking piece
    public Node findAttacker(int rowAttacked, int colAttacked) {
        Node retVal = null;
        Node pieceAttacked = findChessPiece(rowAttacked, colAttacked);

        Node other = head.getNext();
        while((other != null) && (retVal == null)) {
            if (pieceAttacked == null) {
                if(other.getChessPiece().isAttacking(this, rowAttacked, colAttacked)) {
                    retVal = other;
                }
            }
            else if((isDifferent(pieceAttacked, other)) && (other.getChessPiece().isAttacking(this, rowAttacked, colAttacked)) && (pieceAttacked.getChessPiece().getColor() != other.getChessPiece().getColor())) {
                retVal = other;
            }

            other = other.getNext();
        }
        return retVal;
    }

    // Method to write to the analysis.txt file
    // Input: String to write
    // Output: void, just write
    public void writeToAnalysisFile(String stringToWrite) {
        try {
            writer.write(stringToWrite);
            System.out.print(stringToWrite);
        }
        catch (Exception e) {
            Utilities.errExit("Exception occurred while trying to write to file: writeToAnalysisFile"); // throw a generic exception if failure to read occurs
        }
    }

    // Method to iterate through the list and update a 2D array for printing the board
    // onto the console
    // Input: integer board number read from input.txt
    // Output: void, jusr print the solution
    public static void convertFromListToMatrixAndPrint(int board_no) {
        // Initialize isFilled board
        char[][] isFilled = new char[board_size+1][board_size+1];
        for(int i = 0; i < board_size; i++) {
            for(int j = 0; j < board_size; j++) {
                isFilled[i][j] = '-';
            }
        }
        // iterate through the list and update isFilled matrix
        Node piece = head.getNext();
        while(piece != null) {
            isFilled[piece.getRow()][piece.getCol()] = Utilities.returnChessPieceType(piece);
            piece = piece.getNext();
        }

        //System.out.println("Board No: " + (board_no/2));
        //Utilities.printSolution(isFilled, board_size);
    }

    public boolean canKingMoveOutOfCheck(Node king) {
        Node piece1;
        boolean retVal = false;
        int attackRow[] = {-1, -1, 0, 1, 0, 1, 1, -1}; // possible attack row positions
        int attackCol[] = {0, -1, -1, -1, 1, 1, 0, 1}; // possible attack col positions

        //Check each possible place to move to see if it gets out of check
        for(int i = 0; ((i < 8) && (!retVal)); i++) {
            if (king.getRow() + attackRow[i] > 0 && king.getRow() + attackRow[i] <= board_size && king.getCol() + attackCol[i] > 0 && king.getCol() + attackCol[i] <= board_size) {
                piece1 = findChessPiece(king.getRow() + attackRow[i], king.getCol() + attackCol[i]);
                if ((piece1 == null) || (piece1.getChessPiece().getColor() != king.getChessPiece().getColor())) {
                    // spot is a valid place to move.  Now would it also be in check?
                    if (piece1 != null) {
                        remove(piece1);  // simulate taking the piece that is there
                    }
                    remove(king);  // need to do this so king itself doesn't block new attack checks
                    if (findAttacker(king.getRow() + attackRow[i], king.getCol() + attackCol[i]) == null) {
                        retVal = true;
                    }
                    if (piece1 != null) {
                        insert(piece1);  // put back the piece we simulated deleting
                    }
                    insert(king);  //put the king back since we didn't really move it yet.
                }
            }
        }
        return retVal;
    }

    public String checkForChecks() {
        String retVal = "All kings safe";
        Node piece1 = findPieceOfType('k');
        if (findAttacker(piece1.getRow(),piece1.getCol()) != null) {
            // Now find a move for king to get out of check. If none, then it's a checkmate
            if (canKingMoveOutOfCheck(piece1)) {
                retVal = "White in check"; 
            }
            else {
                retVal = "White checkmated"; 
            }
        }
        else {
            piece1 = findPieceOfType('K');
            if (findAttacker(piece1.getRow(),piece1.getCol()) != null) {
                if (canKingMoveOutOfCheck(piece1)) {
                    retVal = "Black in check"; 
                }
                else {
                    retVal = "Black checkmated"; 
                }
            }
            else {
                retVal = "All kings safe";
            }
        }
        return retVal;
    }
    // Method to read from input.txt
    // for each chessboard and query, perform all the required operations
    // an then proceed further
    // Input: none
    // Output: void, jusr read and perform requested operations
    public static void readFromInputFile() {

        int lineCtr = 0;
        int[] query = new int[2];
        int argNumber;
        Node piece1;
        Node piece2;
        ChessBoard c = null;
        boolean isValidMove = true;
        String checkString = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(" "); // Reader assumes that the input format is as given in the instruction
                // If the line is 2i, then I know that it is a configuration of a ChessBoard
                // so create a new ChessBoard here, parse board size and insert
                // given chesspieces into the linked list
                if(lineCtr % 2 == 0) {
                    c = new ChessBoard();
                    board_size = Integer.parseInt(args[0]);
                    for(int i = 1; i < args.length; i += 3) {
                        head = c.insert(new Node(args[i].charAt(0), Integer.parseInt(args[i+1]), Integer.parseInt(args[i+2])));
                    }

                    // Here we will check for checks and checkMates.  Note this is before the moves, not after the moves
                    checkString = c.checkForChecks();

                }
                else {
                    // as soon as you read the query perform the requested
                    // operations
                    //query[0] = Integer.parseInt(args[0]);
                    //query[1] = Integer.parseInt(args[1]);
                    //performOperations(c, query, lineCtr-1);
                    argNumber = 0;   // next item to look at 
                    isValidMove = true;
                    while (argNumber < args.length  && isValidMove) {
                        piece1 = c.findChessPiece(Integer.parseInt(args[argNumber]), Integer.parseInt(args[argNumber + 1]));  // first piece 
                        piece2 = c.findChessPiece(Integer.parseInt(args[argNumber+2]), Integer.parseInt(args[argNumber + 3]));  // second piece (may be null)
                        if (piece1 == null) {
                            isValidMove = false;
                        }
                        else if (piece1.getChessPiece() instanceof Pawn){
                            //special case for pawn
                            if (piece1.getChessPiece().isAttacking(c, Integer.parseInt(args[argNumber+2]), Integer.parseInt(args[argNumber + 3]))) {
                                // This is case where pawn may be attacking another piece at an angle
                                if (piece2 != null && piece1.getChessPiece().getColor() != piece2.getChessPiece().getColor()) {
                                    isValidMove = true;
                                }
                                else {
                                    isValidMove = false;
                                }
                            }
                            else if (piece1.getChessPiece().canMove(c, Integer.parseInt(args[argNumber+2]), Integer.parseInt(args[argNumber + 3]))) {
                                // this is the case where the pawn is not attacking at an angle but might move forward one
                                if (piece2 == null ) {
                                    isValidMove = true;
                                }
                                else {
                                    isValidMove = false;
                                }
                            }
                            else {
                                isValidMove = false;
                            }
                        }
                        else if (piece1.getChessPiece().isAttacking(c, Integer.parseInt(args[argNumber+2]), Integer.parseInt(args[argNumber + 3]))) {
                            if (piece2 != null && piece1.getChessPiece().getColor() != piece2.getChessPiece().getColor()) {
                                isValidMove = true;
                            }
                            else if (piece2 == null) {
                                isValidMove = true;
                            }
                            else {
                                isValidMove = false;
                            }
                        }

                        else {
                            isValidMove = false;
                        }

                        if (isValidMove) {
                            //if piece2 exists, delete it becauseit is being captured by piece1
                            if (piece2 != null) {
                                c.remove(piece2);
                            }
                            //Now move piece1 to the new location
                            piece1.setRow(Integer.parseInt(args[argNumber+2]));
                            piece1.setCol(Integer.parseInt(args[argNumber+3]));
                            writer.write("Valid ");
                            System.out.print ("Valid ");
                        }
                        else {
                            writer.write("Invalid ");
                            System.out.print ("Invalid ");

                        }
                        argNumber = argNumber + 4;  //Move to set of moves
                    }
                    // Now move to the next line of the output file.
                    writer.write("\n"); 
                    System.out.print ("\n");

                    // We had to check for checks before the moves but out the string afterwards here
                    writer.write(checkString); 
                    System.out.print (checkString);

                    // Now move to the next line of the output file.
                    writer.write("\n"); 
                    System.out.print ("\n");
                }
                lineCtr++; // move to the next line
            }
            reader.close();
        }
        catch (NumberFormatException e) {
            Utilities.errExit("All arguments must be integers"); // throw error incase parsing integer fails
        }
        catch (IndexOutOfBoundsException e) {
            Utilities.errExit("Array index is out of bounds"); // throw error when inserting elements into arrays fail
        }
        catch (Exception e) {
            Utilities.errExit("Exception occurred trying to read file"); // throw a generic exception if failure to read occurs
        }
    }

    // Method to perform all the requested operations
    // namely, check validity, perform the search query
    // check for attack
    // Input: ChessBoard and the query
    // Output: returns the count(c.findChessPiece(query) + " ");(c.findChessPiece(query) + " ");
    public static void performOperations(ChessBoard c, int[] query, int board_no) {
        try {
            // Check for validity here
            if(c.checkValidity() == false) {
                writer.write("Invalid\n");
                System.out.print("Invalid\n");
                return;
            }
            // Find the chesspiece given in query location
            writer.write(c.findChessPiece(query) + " ");
            System.out.print(c.findChessPiece(query) + " ");
            // See if anyone attacks anyone else
            c.isOneAttackingOther();
            convertFromListToMatrixAndPrint(board_no);
        }
        catch(Exception e) {
            Utilities.errExit("Error while performing operations");
        }

    }

    // main method
    public static void main(String[] args) {
        try{
            writer = new BufferedWriter(new FileWriter("analysis.txt")); // open the file to write
            readFromInputFile(); // read from input file and perform operations
            writer.close(); // close the writer
        }
        catch(Exception e) {
            Utilities.errExit("Error while creating BufferedWriter");
        }

    }
}

// End
