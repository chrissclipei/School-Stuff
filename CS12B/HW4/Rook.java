// Emmanuel Huff 
// ehuff
// Chris Sclipei 
// csclipei
// HW 4
// 3/10/2017
// Filename: Rook.class
//

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

  // return the moves as a 1D array with location of the form col1, row1, col2, row2 and so on
  // it basically captures the direction and location of the moves for rook
  // given the start and end locations square by squares that is recorded in validMoves (excluding start anf end positions)
  public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
    int[] validMoves;

    int addRow = 0;
    int addCol = 0;
    int length = 0;

    // validity check
    if(startRow != destRow && startCol != destCol) {
      return null;
    }

    // move along same column
    if(destCol == startCol) {
      length = Math.abs(destRow - startRow)-1;
      // move left or right
      if(destRow < startRow) {
        addRow = -1;
      }
      else {
        addRow = 1;
      }
    }
    // move along same row
    if(destRow == startRow ) {
      length = Math.abs(destCol - startCol)-1;
      // move below or above
      if(destCol < startCol) {
        addCol = -1;
      }
      else {
        addCol = 1;
      }
    }

    // some more validity checks
    if(length < 0) {
      if(ignore == true) {
        return new int[0];
      }
      return null;
    }
    validMoves = new int[length*2];
    if(length == 0) {
      return validMoves;
    }

    // populate the actual moves from start to end
    validMoves[0] = startCol + addCol;
    validMoves[1] = startRow + addRow;
    for(int i = 2; i < length*2; i += 2) {
      validMoves[i] = validMoves[i-2] + addCol;
      validMoves[i+1] = validMoves[i-1] + addRow;
    }

    return validMoves;
  }

  
  // return all possible moves as a 1D array with location of the form col1, row1, col2, row2 and so on
  // it basically returns every possible square this piece can move to
  public int[] getAllPossibleMoves(boolean ignore) {
    int[] validMoves;
    int validMovesIndex = 0;
    int addRow = 0;
    int addCol = 0;
    int length = 0;
    int startRow = this.getRow();
    int startCol = this.getCol();
   
    validMoves = new int[28];   // For a rook, there are always 14 possible squares it could move to

    // First move from the rooks spot across the board to the right (ie col++)
    for (int i = startCol+1; i<=8; i++) {
        validMoves[validMovesIndex] = i;
        validMoves[validMovesIndex + 1] = startRow;
        validMovesIndex = validMovesIndex + 2;
    }

    // Next move from the rooks start spot across the board to the left (ie col--)
    for (int i = startCol-1; i>0; i--) {
        validMoves[validMovesIndex] = i;
        validMoves[validMovesIndex + 1] = startRow;
        validMovesIndex = validMovesIndex + 2;
    }

    // Next move from the rooks start spot up the board (ie row++)
    for (int i = startRow+1; i<=8; i++) {
        validMoves[validMovesIndex] = startCol;
        validMoves[validMovesIndex + 1] = i;
        validMovesIndex = validMovesIndex + 2;
    }
    // Next move from the rooks start spot down the board (ie row--)
    for (int i = startRow-1; i>0; i--) {
        validMoves[validMovesIndex] = startCol;
        validMoves[validMovesIndex + 1] = i;
        validMovesIndex = validMovesIndex + 2;
    }

    return validMoves;
  }

  
  // Boolean function that determines if self (which is a rook) is attacking another chesspiece at location row, col, given as argument
  // Input: integer row and col
  // Output: True if self is attacking the chesspiece at position (row, col), false otherwise
  public boolean isAttacking(ChessPiece piece)
  {
      if ((this.getRow() == piece.getRow() || this.getCol() == piece.getCol()) && (this.getColor() != piece.getColor())) // if self has same row or column as chesspiece, self is attacking
          return true;
      else
          return false; // self is not attacking chesspiece
  }
}

// End
