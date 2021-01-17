//Emmanuel Huff 
//ehuff
//Chris Sclipei 
//csclipei
//HW 3
//2/19/2017
//Queen.class

class Queen extends ChessPiece {

    // Default constructor sets row and col to infeasible (negative) values
    public Queen()
    {
        super();
    }

    // Constructor creates Queen with col c and row r
    public Queen(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // Boolean function that determines if self (which is a queen) is attacking another chesspiece, given as argument
    // Input: integer row and col
    // Output: True if self is attacking another chesspiece at (row, col), false otherwise
    public boolean isAttacking(ChessBoard board, int row, int col)
    {
        int rowIncrement = 0;
        int colIncrement = 0;
        int checkRow = 0;
        int checkCol = 0;
        boolean retVal;

        retVal = true;
        if (this.getRow() == row || this.getCol() == col) { // if self has same row or column as chesspiece, self is attacking
            if (this.getRow() == row){
                if (col > this.getCol()) {
                    colIncrement = -1;  // move down while checking
                }
                else {
                    colIncrement = 1; //move up while checking
                }
                checkCol = col + colIncrement;
                checkRow = this.getRow();
                while (checkCol != this.getCol()) {
                    if (board.findChessPiece(checkRow, checkCol) != null) {
                        retVal = false;  //there is another piece in between
                    }
                    checkRow = checkRow + rowIncrement;
                    checkCol = checkCol + colIncrement;
                }
            }
            if (this.getCol() == col) {
                if (row > this.getRow()) {
                    rowIncrement = -1;  // move down while checking
                }
                else {
                    rowIncrement = 1; //move up while checking
                }
                checkRow = row + rowIncrement;
                checkCol = this.getCol();
                while (checkRow != this.getRow())  {
                    if (board.findChessPiece(checkRow, checkCol) != null) {
                        retVal = false;  //there is another piece in between
                    }
                    checkRow = checkRow + rowIncrement;
                    checkCol = checkCol + colIncrement;
                }
            }

        }
        else if (Math.abs(this.getRow() - row) == Math.abs(this.getCol() - col)){ // if self is on same diagonal as chesspiece, this is attack. we use absolute values to determine diagonal
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
            retVal = false; // self is not attacking chesspiece
        }
        return retVal;
    }
}

// End
