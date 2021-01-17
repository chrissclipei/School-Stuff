//Emmanuel Huff 
//ehuff
//Chris Sclipei 
//csclipei
//HW 3
//2/19/2017
//Knight.class

class Knight extends ChessPiece {

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

    // Boolean function that determines if self (which is a knight) is attacking another chesspiece, given as argument
    // Input: integer row, col
    // Output: True if self is attacking chesspiece at location (row, col), false otherwise
    public boolean isAttacking(ChessBoard board, int row, int col)
    {
        int attackRow[] = {-1, 1, -1, 1, -2, -2, 2, 2}; // possible attack row positions
        int attackCol[] = {-2, -2, 2, 2, -1, 1, -1, 1}; // possible attack col positions

        for(int i = 0; i < 8; i++) {
          if(this.getRow() + attackRow[i] == row && this.getCol() + attackCol[i] == col) {
            return true;
          }
        }
        return false;
    }
}

// End
