// Emmanuel Huff 
// ehuff
// Chris Sclipei 
// csclipei
// HW 4
// 3/10/2017
// Filename: ChessBoard.java
// Contains class ChessBoard that creates a linkedlist of chessboard where each node holds a chesspiece to be placed
//
import java.io.*;

class ChessBoard {

    private static Node head; // linkedlist to store chesspieces
    private static int board_size; // board_size
    private static int board_no; // current board number we are processing
    public static BufferedWriter writer; // write to write to file
    public static final int possibleRowMoves[] = {-1, -1, 0, 1, 0, 1, 1, -1}; // these are the possible row moves for a king
    public static final int possibleColMoves[] = {0, -1, -1, -1, 1, 1, 0, 1}; // these are the possible col moves for a king
    public static int movesToCheck;

    // constructor
    public ChessBoard() {
        head = new Node();
    }

    // Method to perform capture.
    // First, it finds a node that contains piece to capture and captures it
    // Input: Node containing piece to capture
    // Output: returns the modified list
    public Node capture(Node list, Node pieceToCapture) {
        return ListOperations.deleteNode(list, pieceToCapture);
    }

    // Method to check if given coordinates are out of the board or not
    // Input: row and column
    // Output: returns true if the coordinates lie outside the board
    public boolean isOutOfBoard(int row, int col) {
        if(row <= 0 || col <= 0 || row > board_size || col > board_size) {
            return true;
        }
        return false;
    }

    // Method to place the chesspiece in a final destination
    // if there is a piece of other color in the destination, it performs capture as well
    // Input: Node containing piece to move, node containing piece in destination, destination location
    // Output: returns if placement is successful or not
    public boolean placePiece(Node list, Node piece, Node dest, int row, int col, boolean shouldPrint) {
        if(dest != null) {
            if(piece.getColor() != dest.getColor()) {
                list = capture(list, dest);
            }
            else {
                if(shouldPrint) {
                    System.out.println("Invalid Move from " + piece.getCol() + " " + piece.getRow() +
                        " to " + row + " " + col + ": Destination piece, " + Utilities.returnChessPieceType(dest)
                        + " is the same color as that of " + Utilities.returnChessPieceType(piece));
                }
                return false;
            }
        }
        piece.setRow(row);
        piece.setCol(col);
        return true;
    }

    // Method to check if the move is blocked
    // if there is a piece in the path to destination, it returns false
    // Input: all the valid moves of a piece in the direction of destination
    // Output: returns if a move is blocked or not
    public boolean checkBlock(Node list, int[] validMoves, int startRow, int startCol, int destRow, int destCol, boolean shouldPrint) {
        for(int j = 0; j < validMoves.length; j += 2) {
            Node piece = ListOperations.findChessPiece(list, validMoves[j+1], validMoves[j]);
            if(piece != null) {
                if(shouldPrint) {
                    System.out.println("Invalid Move from " + startCol + " " + startRow + " to " + destCol +
                        " " + destRow + ": Blocked by " + Utilities.returnChessPieceType(piece) + " at position "
                        + validMoves[j] + " " + validMoves[j+1]);
                }
                return false;
            }
        }
        return true;
    }

    // Method to move pawns if the positions given are valid
    // If it is a straight move, then all you have to check is if it is one step move and that there
    // are no other pieces in destination. If it is not a straight move, check if it is
    // any of the immediate left or right and that there is a piece of different color.
    // Input: pieceToMove, piece in the destination, starting column and new column to move to
    // Output: returns if a move is successful or not
    public boolean movePawns(Node list, Node pieceToMove, Node pieceInDestination,
    int newRowToMoveTo, int newColToMoveTo, boolean shouldPrint) {
        // check if it a straight move
        if(pieceToMove.getCol() == newColToMoveTo) {
            // check if the move is just by one row
            if((pieceToMove.getRow() + 1) == newRowToMoveTo || (pieceToMove.getRow() - 1) == newRowToMoveTo) {
                // check if it is not blocked
                if(pieceInDestination == null) {
                    pieceToMove.setCol(newColToMoveTo);
                    pieceToMove.setRow(newRowToMoveTo);
                    return true;
                }
                else {
                    // If someone is blocking the pawn
                    if(shouldPrint) {
                        System.out.println("Invalid Move: " + Utilities.returnChessPieceType(pieceToMove) + " at "
                            + pieceToMove.getCol() + " " + pieceToMove.getRow() + " is blocked by " +
                            Utilities.returnChessPieceType(pieceInDestination) + " at " + newColToMoveTo + " " + newRowToMoveTo);
                    }
                    return false;
                }
            }
        }
        else {
            // if the piece in destination is not null, check if it lies in diagonals and place piece
            // only if you can capture. Otherwise just print that it is not possible and return false
            if(pieceInDestination != null) {
                return placePiece(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
            }
            else {
                if(shouldPrint) {
                    System.out.println("Invalid Move: " + Utilities.returnChessPieceType(pieceToMove) +
                        " cannot move from " + pieceToMove.getCol() + " " + pieceToMove.getRow() + " to " + newColToMoveTo +
                        " " + newRowToMoveTo);
                }
                return false;
            }
        }
        return true;
    }

    // Method to perform moves based on the query read from input.txt
    // It has 3 cases, one for knight, one for pawn and another one for the rest of the chess pieces
    // Input: query. Basically, the move positions
    // Output: returns the vector with bits that are valid (or moves possible) set to true
    public boolean[] makeMoves(Node list, int[] query, boolean shouldPrint) {

        // moveslog will keep track of valid moves until you hit invalid
        // so we are allocating the maximum no. of valids in any given query
        boolean[] movesLog = new boolean[query.length/4];
        // for each move in the query perform the following
        for(int i = 0; i < query.length; i += 4) {

            // keep the start and dest locations and current move you are performing
            int startCol = query[i];
            int startRow = query[i+1];
            int newColToMoveTo = query[i+2];
            int newRowToMoveTo = query[i+3];
            int currentQuery = (i/4);

            // see who is in the start position
            Node pieceToMove = ListOperations.findChessPiece(list, startRow, startCol);
            // if no piece exists in the given starting position, then it is invalid
            // so just stop further processing
            if(pieceToMove == null) {
                if(shouldPrint) {
                    System.out.println("Invalid Move: No piece present at " + startCol + " " + startRow);
                }
                movesLog[currentQuery] = false;
                break;
            }
            // see who is in destination
            Node pieceInDestination = ListOperations.findChessPiece(list, newRowToMoveTo, newColToMoveTo);
            char pieceType = Utilities.returnChessPieceType(pieceToMove);

            // get the moves for the start piece
            int[] validMoves = pieceToMove.getChessPiece().getMoves(startRow, startCol, newRowToMoveTo, newColToMoveTo, false);
            // check if the given move is valid or not
            if(validMoves == null) {
                if(shouldPrint) {
                    System.out.println("Invalid Move: " + Utilities.returnChessPieceType(pieceToMove) + " cannot move from "
                        + startCol + " " + startRow + " to " + newColToMoveTo + " " + newRowToMoveTo);
                }
                movesLog[currentQuery] = false;
                break;
            }

            // initialize it to false
            movesLog[currentQuery] = false;
            // For Knight
            if(pieceType == 'n' || pieceType == 'N') {
                movesLog[currentQuery] = placePiece(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
            }
            // For Pawn
            else if(pieceType == 'p' || pieceType == 'P') {
                movesLog[currentQuery] = movePawns(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
            }
            // For the other pieces. Check the blocking and then place piece if not blocked
            else {
                Node piece = list.getNext();
                movesLog[currentQuery] = checkBlock(list, validMoves, startRow, startCol, newRowToMoveTo, newColToMoveTo, shouldPrint);
                if(movesLog[currentQuery]) {
                    movesLog[currentQuery] = placePiece(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
                }
            }
            if(movesLog[currentQuery] == false) {
                break;
            }
            if(shouldPrint) {
                System.out.println("Board after performing the move: " + startCol + " " + startRow + " to " +
                    newColToMoveTo + " " + newRowToMoveTo);
                //Utilities.convertFromListToMatrixAndPrint(list, board_no, board_size);
            }
        }
        return movesLog;
    }

    // Method to return the kinf node based on the color
    // Input: color of the king
    // Output: returns the corresponding king node
    public Node getKingNode(Node list, boolean kingColor) {
        Node kingNode  = null;
        if(kingColor == true) {
            kingNode = ListOperations.findChessPiece(list, 'k');
        }
        else {
            kingNode = ListOperations.findChessPiece(list, 'K');
        }
        return kingNode;
    }

    // Method to check if a king is in check
    // Input: color of the king
    // Output: returns if there is a check or not
    public boolean determineCheck(Node list, boolean kingColor) {
        // get the first valid chesspiece (remember not the head)
        Node piece = list.getNext();
        Node king  = getKingNode(list, kingColor);
        int row = king.getRow();
        int col = king.getCol();

        // loop through each of the remaining chesspieces and check for attack
        while(piece != null) {
            // if opposite knight gives a check. Then none of them can block. Because knight can jump
            if(ListOperations.isDifferent(piece, king) && piece.getChessPiece().isAttacking(king.getChessPiece())) {
                if(Utilities.returnChessPieceType(piece) == 'n' || Utilities.returnChessPieceType(piece) == 'N') {
                    return true;
                }
                else {
                    // for others, we need to see if someone is blocking for an opposite piece to give a check
                    int[] validMoves = piece.getChessPiece().getMoves(piece.getRow(), piece.getCol(), row, col, true);
                    //If this one is blocked, keep looking for others that are also attacking.
                    if (checkBlock(list, validMoves, piece.getRow(), piece.getCol(), row, col, false)) {
                        return true;
                    }
                }
            }
            piece = piece.getNext();
        }
        return false;
    }

    // Method to check if a king is in weak checkmate
    // Input: color of the king
    // Output: returns if there is a weak checkmate or not
    public boolean determineWeakCheckmate(Node head, boolean kingColor) {
        Node list = ListOperations.listCopy(head);
        boolean checkmate = false;
        // 8 surrounding moves. So a total of 8 positions to check. Let
        // us start by assuming that the king can move to all 8 surrounding positions
        int movePossibility[] = {1, 1, 1, 1, 1, 1, 1, 1};
        int attackCtr = 0;
        int spaceCtr = 0;

        // get the king node, store the locations for accessibility
        Node king = getKingNode(list, kingColor);
        char type = Utilities.returnChessPieceType(king);
        int row = king.getRow();
        int col = king.getCol();

        // proceed to verify the checkmate only if the given king is under check
        if(!determineCheck(list, kingColor)) {
            return false;
        }

        // for all possible moves around that king
        for(int i =0; i < possibleRowMoves.length; i++) {
            // avoid possible moves out of the board
            if(isOutOfBoard(row+possibleRowMoves[i], col+possibleColMoves[i])) {
                movePossibility[i] = 0;
                continue;
            }
            // move the king to his adjacent position
            int[] query = {col, row, col+possibleColMoves[i], row+possibleRowMoves[i]};
            Node newList = ListOperations.listCopy(list);
            boolean[] newPossibility = makeMoves(newList, query, false);

            // perform the move (or not is there is a same colored piece in the adjacent location),
            // then determine if he is still under check
            // if so, then that move/location does not prevent the checkmate
            if(determineCheck(newList, kingColor)) {
                movePossibility[i] = 0;
            }
        }
        // if all the possible places for that king are occupied, then he is under checkmate
        for(int i = 0; i < movePossibility.length; i++) {
            if(movePossibility[i] == 0) {
                attackCtr++;
            }
        }
        if(attackCtr == movePossibility.length) {
            checkmate = true;
        }
        return checkmate;
    }

    // Method to check if a king is in real checkmate
    // Input: color of the king
    // Output: returns if there is a real checkmate or not
    public boolean determineRealCheckmate(Node head, boolean kingColor) {
        // create a copy of the original list
        Node list = ListOperations.listCopy(head);
        // fetch the king node
        Node kingNode = getKingNode(list, kingColor);
        // keep the location of king for accessability
        int row = kingNode.getRow();
        int col = kingNode.getCol();

        // Check if any piece of the same color can be moved to stop the check.  Don't need
        // to call determineWeakCheckmate because in this loop we are going to check all
        // pieces of the same color including the king.
        if(!determineCheck(list, kingColor)) {
            return false;  // We're not in check; can't be checkmate.
        }
        Node piece = list.getNext();
        // loop through all the chess pieces
        while (piece != null) {
            if (piece.getColor() == kingColor) {
                int[] validMoves =  piece.getChessPiece().getAllPossibleMoves(false);
                int[] query = new int[4];

                query[0] = piece.getCol();
                query[1] = piece.getRow();
                for (int i=0; i<validMoves.length; i=i+2) {
                    query[2] = validMoves[i];
                    query[3] = validMoves[i+1];

                    Node tempList =  ListOperations.listCopy(list);
                    boolean[] moveResult= makeMoves(tempList, query, false);
                    if (moveResult[0]) {
                        // Getting here means it was a valid move
                        boolean isOurKingUnderAttack = determineCheck(tempList, kingColor);
                        if (!isOurKingUnderAttack) {//Not allowed to be in check after our move
                            //System.out.println("Checkmate can be blocked");
                            //Utilities.convertFromListToMatrixAndPrint(tempList, board_no, board_size);
                            return false;
                        }
                    }
                }

            }
            piece = piece.getNext();

        }

        return true;

    }
    // Method to write to the analysis.txt file
    // Input: String to write
    // Output: void, just write
    public void writeToAnalysisFile(String stringToWrite) {
        try {
            writer.write(stringToWrite);
        }
        catch (Exception e) {
            Utilities.errExit("Exception occurred while trying to write to file:"
                + "writeToAnalysisFile"); // throw a generic exception if failure to read occurs
        }
    }

    // Method to read from input.txt
    // for each chessboard and query, perform all the required operations
    // an then proceed further
    // Input: none
    // Output: void, jusr read and perform requested operations
    public static void readFromInputFile() {
        int lineCtr = 0;
        int[] query;
        ChessBoard c = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(" "); // Reader assumes that the input format is as given in the instruction
                // If the line is 2i, then I know that it is a configuration of a ChessBoard
                // so create a new ChessBoard here, parse board size and insert
                // given chesspieces into the linked list
                movesToCheck = Integer.parseInt(args[0].replace(":",""));
                c = new ChessBoard();
                board_size = 8;
                for(int i = 1; i < args.length; i += 3) {
                    head = ListOperations.insert(head, new Node(args[i].charAt(0), Integer.parseInt(args[i+2]), Integer.parseInt(args[i+1])));
                }

                board_no = lineCtr;
                performOperations(c);

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

    public int takeATurn(Node list, boolean whoseTurn, int recursionDepth){
        Node piece = list.getNext();
        int saveLevelCheckmateOccurred = -1;
        int levelCheckmateOccurred = -1;
        int[] query = new int[4];
        int[] savedQuery = new int[4];
        Node savedPiece = null;
        boolean haveSafeMove = false;
        boolean realCheckmate = false;
        boolean isOurKingUnderAttack = false;

        while(piece!=null && !realCheckmate){
            if (piece.getColor() == whoseTurn){
                int[] possibleMoves = piece.getChessPiece().getAllPossibleMoves(true);
                query[0] = piece.getCol();
                query[1] = piece.getRow();
                for(int i = 0; i < possibleMoves.length && !realCheckmate ; i+=2){
                    query[2] = possibleMoves[i];
                    query[3] = possibleMoves[i+1];
                    Node tempList = ListOperations.listCopy(list);
                    boolean[] movesLog = makeMoves(tempList, query, false);
                    if(movesLog[0]) {
                        isOurKingUnderAttack = determineCheck(tempList,whoseTurn);
                        if (!isOurKingUnderAttack) {
                            if((movesToCheck % 2) ==  (recursionDepth % 2)){
                                realCheckmate= determineRealCheckmate(tempList, !whoseTurn);
                            }
                            if(realCheckmate){
                                levelCheckmateOccurred = recursionDepth;
                            }
                            if(realCheckmate == false && recursionDepth < movesToCheck){
                                levelCheckmateOccurred = takeATurn(tempList, !whoseTurn, recursionDepth + 1);
                                if(levelCheckmateOccurred > 0){
                                    if((levelCheckmateOccurred % 2) == (recursionDepth % 2)){
                                        realCheckmate = true;
                                    }
                                    else{ //save these off in case we don't find a safe move
                                        saveLevelCheckmateOccurred = levelCheckmateOccurred;
                                        savedQuery[0] = query[0];
                                        savedQuery[1] = query[1];
                                        savedQuery[2] = query[2];
                                        savedQuery[3] = query[3];
                                        savedPiece = piece;
                                        levelCheckmateOccurred = -1;
                                    }
                                }
                                else {
                                    haveSafeMove = true;
                                }
                            }
                            else if(realCheckmate == false){
                                haveSafeMove = true;
                            }
                        }
                    }
                }
            }
            if(!realCheckmate){
                piece = piece.getNext();
            }
        }
        if (!haveSafeMove && saveLevelCheckmateOccurred != -1){
            levelCheckmateOccurred = saveLevelCheckmateOccurred;
            query[0] = savedQuery[0];
            query[1] = savedQuery[1];
            query[2] = savedQuery[2];
            query[3] = savedQuery[3];
            piece = savedPiece;
        }
        if (haveSafeMove && (levelCheckmateOccurred > 0) && (levelCheckmateOccurred % 2) != (recursionDepth % 2)) {
            //We had a safe move but tried more moves which resulted in us being checkmated: ignore checkmate and use the safeMove
            levelCheckmateOccurred = -1; 
        }
        if(recursionDepth == 1){
            if(levelCheckmateOccurred != -1 && levelCheckmateOccurred%2 == 1){
                char type = Utilities.returnChessPieceType(piece);
                writeToAnalysisFile(type + " " + query[0] + " " + query[1] + " " + query[2] + " " + query[3]);
                System.out.println(type + " " + query[0] + " " + query[1] + " " + query[2] + " " + query[3]);
            }
            else if(levelCheckmateOccurred != -1 && levelCheckmateOccurred%2 == 0){
                writeToAnalysisFile("Black can win");
                System.out.println("Black can win");
            }
            else{
                System.out.println("No Solution");
                writeToAnalysisFile("No Solution");
            }
        }
        return levelCheckmateOccurred;
    }
    // Method to perform all the requested operations
    // namely, check validity, identify check,
    // identify strong and weak checkmates and perform moves
    // Input: ChessBoard and the query
    // Output: returns the count
    public static void performOperations(ChessBoard c) {
        try {
            // Check the validity of the board here. Just do it for completion
            if(ListOperations.checkValidity(head) == false) {
                c.writeToAnalysisFile("No Solution\n");
                System.out.println("No Solution");
                return;
            }
            // check the king checks and weak and strong checkmates
            boolean isWhiteKingUnderAttack = c.determineCheck(head, true);
            boolean isBlackKingUnderAttack = c.determineCheck(head, false);
            //boolean realCheckmateWhite = c.determineRealCheckmate(head, true);
            //boolean realCheckmateBlack = c.determineRealCheckmate(head, false);

            // print the initial board
            //System.out.println("Initial Board");
            //Utilities.convertFromListToMatrixAndPrint(head, board_no, board_size);
            if (isWhiteKingUnderAttack || isBlackKingUnderAttack){
                c.writeToAnalysisFile("No Solution\n");
                System.out.println("No Solution");
                return;
            }
            int levelCheckmateOccurred = c.takeATurn(head, true, 1);

            // perform moves given in the query
            c.writeToAnalysisFile("\n");
            // print the checks and checkmates

            /*
            if(realCheckmateWhite && weakCheckmateWhite && isWhiteKingUnderAttack)
            c.writeToAnalysisFile("White checkmated ");
            else if(realCheckmateBlack && weakCheckmateBlack && isBlackKingUnderAttack)
            c.writeToAnalysisFile("Black checkmated ");
            else {
            if(!isBlackKingUnderAttack && !isWhiteKingUnderAttack)
            c.writeToAnalysisFile("All kings safe ");
            else {
            if(isWhiteKingUnderAttack)
            c.writeToAnalysisFile("White in check ");
            if(isBlackKingUnderAttack)
            c.writeToAnalysisFile("Black in check ");
            }
            }

            c.writeToAnalysisFile("\n");
            // print the final board after performing all the moves
            //System.out.println("Board after performing all the valid moves");
            //Utilities.convertFromListToMatrixAndPrint(head, board_no, board_size);
            System.out.println();
            for(int k = 0; k < 50; k++) {
            System.out.print('-');
            }
            System.out.println();

             */
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
