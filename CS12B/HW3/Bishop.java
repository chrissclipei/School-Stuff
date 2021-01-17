//Emmanuel Huff 
//ehuff
//Chris Sclipei 
//csclipei
//HW 3
//2/19/2017
//Bishop.class

class Bishop extends ChessPiece {

    // Default constructor sets loc to infeasible (negative) values
    public Bishop()
    {
        super();
    }

    // Constructor creates Bishop at location (col, row) and color
    public Bishop(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // Boolean function that determines if self (which is a bishop) is attacking another chesspiece at row and col, given as argument
    // Input: int row and col
    // Output: True if self is attacking the chesspiece at position row and col, false otherwise
    public boolean isAttacking(ChessBoard board, int row, int col)
    {
        int rowIncrement;
        int colIncrement;
        int checkRow;
        int checkCol;
        boolean retVal;
        
        retVal = true;
        if (Math.abs(this.getRow() - row) == Math.abs(this.getCol() - col)) { // if self is on same diagonal as chesspiece, this is attack. we use absolute values to determine diagonal
            //We can move or attack row,col.  Now check for blocking on the path to row,col
            if (row > this.getRow()) {
                rowIncrement = -1;  // move down while checking
            }
            else {
                rowIncrement = 1; //move up while checking
            }
            if (col > this.getCol()) {
                colIncrement = -1;  // move down while checking
            }
            else {
                colIncrement = 1; //move up while checking
            }
            checkRow = row + rowIncrement;
            checkCol = col + colIncrement;
            while ((checkRow != this.getRow()) && (checkCol != this.getCol())) {
                if (board.findChessPiece(checkRow, checkCol) != null) {
                    retVal = false;  //there is another piece in between
                }
                checkRow = checkRow + rowIncrement;
                checkCol = checkCol + colIncrement;
            }
        }
        else {
            retVal = false; // self is not attacking chesspiece at position l
        }
        return retVal;
    }
}

// End
