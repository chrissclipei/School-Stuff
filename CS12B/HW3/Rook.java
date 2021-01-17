//Emmanuel Huff 
//ehuff
//Chris Sclipei 
//csclipei
//HW 3
//2/19/2017
//Rook.class

class Rook extends ChessPiece {

    // Default constructor sets loc to infeasible (negative) values
    public Rook()
    {
        super();
    }

    // Constructor creates Rook with row, col and color
    public Rook(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // Boolean function that determines if self (which is a rook) is attacking another chesspiece at location row, col, given as argument
    // Input: integer row and col
    // Output: True if self is attacking the chesspiece at position (row, col), false otherwise
    public boolean isAttacking(ChessBoard board, int row, int col)
    {
        int rowIncrement = 0;
        int colIncrement = 0;
        int checkRow = 0;
        int checkCol = 0;
        boolean retVal;

        retVal = true;
        if (this.getRow() == row || this.getCol() == col){ // if self has same row or column as chesspiece, self is attacking
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
        else {
            retVal = false; // self is not attacking chesspiece
        }
        return retVal;
    }
}

// End
