// Emmanuel Huff 
// ehuff
// Chris Sclipei 
// csclipei
// HW 4
// 3/10/2017
// Filename: Knight.class
//
import java.util.*;

class Knight extends ChessPiece {
    private static final int movesRow[] = {-1, 1, -1, 1, -2, -2, 2, 2}; // possible attack row positions
    private static final int movesCol[] = {-2, -2, 2, 2, -1, 1, -1, 1}; // possible attack col positions

    // Default constructor sets location to infeasible (negative) values
    public Knight()
    {
        super();
    }

    // Constructor creates Knight with row, col and color
    public Knight(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // method that checks if the end position is a valid knight move or not
    public int ifValidReturnLength(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
        boolean isValid = false;

        for(int i = 0; i < movesRow.length; i++) {
            if(startRow + movesRow[i] == destRow && startCol + movesCol[i] == destCol) {
                isValid = true;
            }
        }

        if(!isValid) {
            if(ignore == true) {
                return 0;
            }
            return -1;
        }
        return 0;
    }

    // return the moves as a 1D array with location of the form col1, row1, col2, row2 and so on
    // for knight it does not really matter, all we need to check are if the moves are valid
    // additional checks are performed by the ifValidReturnLength
    public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore){
        int[] validMoves = new int[0];
        int length = ifValidReturnLength(startRow, startCol, destRow, destCol, ignore);
        if(length == -1) {
            return null;
        }
        return validMoves;
    }

    // return all possible moves as a 1D array with location of the form col1, row1, col2, row2 and so on
    // it basically returns every possible square this piece can move to
    public int[] getAllPossibleMoves(boolean ignore) {
        int[] validMoves;
        int[] validMovesResized;
        int validMovesIndex = 0;
        int startRow = this.getRow();
        int startCol = this.getCol();

        validMoves = new int[16];   // A knight has a maximum of 8 possible moves

        if (startCol > 2 && startRow < 8) {
            // First try to move up 1 and to the left 2
            validMoves[validMovesIndex] = startCol - 2;  // the col
            validMoves[validMovesIndex + 1] = startRow + 1;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol > 2 && startRow > 1) {
            // Next try to move down 1 and to the left 2
            validMoves[validMovesIndex] = startCol - 2;  // the col
            validMoves[validMovesIndex + 1] = startRow - 1;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol > 1 && startRow < 7) {
            // Next try to move up 2 and to the left 1
            validMoves[validMovesIndex] = startCol - 1;  // the col
            validMoves[validMovesIndex + 1] = startRow + 2;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol > 1 && startRow > 2) {
            // Next try to move down 2 and to the left 1
            validMoves[validMovesIndex] = startCol - 1;  // the col
            validMoves[validMovesIndex + 1] = startRow - 2;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol < 8 && startRow < 7) {
            // Next try to move up 2 and to the right 1
            validMoves[validMovesIndex] = startCol + 1;  // the col
            validMoves[validMovesIndex + 1] = startRow + 2;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol < 8 && startRow > 2) {
            // Next try to move down 2 and to the right 1
            validMoves[validMovesIndex] = startCol + 1;  // the col
            validMoves[validMovesIndex + 1] = startRow - 2;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol < 7 && startRow < 8) {
            // Next try to move up 1 and to the right 2
            validMoves[validMovesIndex] = startCol + 2;  // the col
            validMoves[validMovesIndex + 1] = startRow + 1;  // the row
            validMovesIndex = validMovesIndex + 2;
        }
        if (startCol < 7 && startRow > 1) {
            // Next try to move down 1 and to the right 2
            validMoves[validMovesIndex] = startCol + 2;  // the col
            validMoves[validMovesIndex + 1] = startRow - 1;  // the row
            validMovesIndex = validMovesIndex + 2;
        }

        // depending on the position of the knight, the final array of possible moves may not have 8 squares
        // although that is the maximum.  Create a new array of the actual size and copy into it.
        validMovesResized = Arrays.copyOf(validMoves, validMovesIndex);
        return validMovesResized;
    }

    // Boolean function that determines if self (which is a knight) is attacking another chesspiece, given as argument
    // Input: integer row, col
    // Output: True if self is attacking chesspiece at location (row, col), false otherwise
    public boolean isAttacking(ChessPiece piece)
    {
        int attackRow[] = {-1, 1, -1, 1, -2, -2, 2, 2}; // possible attack row positions
        int attackCol[] = {-2, -2, 2, 2, -1, 1, -1, 1}; // possible attack col positions

        for(int i = 0; i < 8; i++) {
            if((this.getRow() + attackRow[i] == piece.getRow() && this.getCol() + attackCol[i] == piece.getCol()) && (this.getColor() != piece.getColor())) {
                return true;
            }
        }
        return false;
    }
}

// End
