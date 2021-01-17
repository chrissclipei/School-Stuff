//Chris Sclipei
//csclipei
//Emmanuel Huff
//ehuff 
//1/31/17
//CMPS12B
//This is our superclass which contains the methods for all of our chess
//pieces as well as taking care of the isAttacking function

public class Chesspiece {
    String color;
    int posRow;
    int posColumn;
    String pieceType;

    public Chesspiece (String piecetype, String color, int posRow, int posColumn) {
        this.color = color;
        this.posRow = posRow;
        this.posColumn = posColumn;
        this.pieceType = piecetype;
    }

    public boolean CheckOneAttack(Chessboard board, int row, int col) {
        return false;
    }

    public Chesspiece CheckAllAttacks(Chessboard board) {
        return null;
    }
}

class King extends Chesspiece {
    int i, j;
    public King (String piecetype, String color, int posRow, int posColumn) {
        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn
        // For a king, move left 1 space
        Chesspiece possibleAttacker = board.find(this.posRow, this.posColumn-1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move up 1 space
        possibleAttacker = board.find(this.posRow+1, this.posColumn);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move right 1 space
        possibleAttacker = board.find(this.posRow, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move down 1 space
        possibleAttacker = board.find(this.posRow-1, this.posColumn);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move down 1 left 1
        possibleAttacker = board.find(this.posRow-1, this.posColumn-1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move down 1 right 1
        possibleAttacker = board.find(this.posRow-1, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move up 1 left 1
        possibleAttacker = board.find(this.posRow+1, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a king, move up 1 right 1
        possibleAttacker = board.find(this.posRow+1, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        return null;
    }

}

class Queen extends Chesspiece {
    int i, j;
    public Queen (String piecetype, String color, int posRow, int posColumn) {

        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn
        // For a queen, first check all the way to the left
        for (int col = this.posColumn - 1; col >= 0; col--) {
            Chesspiece possibleAttacker = board.find(this.posRow, col);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }        
        // For a queen, next check all the way up
        for (int row = this.posRow + 1; row <= board.boardSize-1; row++) {
            Chesspiece possibleAttacker = board.find(row, this.posColumn);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        // For a queen, next check all the way to the right
        for (int col = this.posColumn + 1; col <= board.boardSize; col++) {
            Chesspiece possibleAttacker = board.find(this.posRow, col);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        // For a queen, next check all the way down
        for (int row = this.posRow - 1; row >= 0; row--) {
            Chesspiece possibleAttacker = board.find(row, this.posColumn);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        //Checks the lower left diagonal
        for (i = this.posRow-1, j = this.posColumn-1; i>=0 && j>=0; i--, j--) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        //Checks the lower right diagonal
        for (i = this.posRow-1, j = this.posColumn+1; i<=board.boardSize-1 && j>=0; i--, j++) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        //Checks the upper right diagonal
        for (i = this.posRow+1, j = this.posColumn+1; i<=board.boardSize-1 && j<=board.boardSize-1; i++, j++) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        //Checks the upper left diagonal
        for (i = this.posRow+1, j = this.posColumn-1; i>=0 && j<=board.boardSize-1; i++, j--) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }

        return null;
    }

}

class Bishop extends Chesspiece {
    int i, j;
    public Bishop (String piecetype, String color, int posRow, int posColumn) {

        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn
        //Checks the lower left diagonal
        for (i = this.posRow-1, j = this.posColumn-1; i>=0 && j>=0; i--, j--) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }       
        //Checks the lower right diagonal
        for (i = this.posRow-1, j = this.posColumn+1; i<=board.boardSize-1 && j>=0; i--, j++) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        //Checks the upper right diagonal
        for (i = this.posRow+1, j = this.posColumn+1; i<=board.boardSize+1 && j<=board.boardSize-1; i++, j++) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        //Checks the upper left diagonal
        for (i = this.posRow+1, j = this.posColumn-1; i<=board.boardSize-1 && j>=0; i++, j--) {
            Chesspiece possibleAttacker = board.find(i, j);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }

        return null;
    }
}

class Knight extends Chesspiece {
    int i,j; 
    public Knight (String piecetype, String color, int posRow, int posColumn) {

        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn

        // move up 2 then right 1

        Chesspiece possibleAttacker = board.find(this.posRow+2, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move up 2 then left 1 
        possibleAttacker = board.find(this.posRow+2, this.posColumn-1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move down 2 right 1 
        possibleAttacker = board.find(this.posRow-2, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move down 2 left 1 
        possibleAttacker = board.find(this.posRow-2, this.posColumn-1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move up 1 then right 2
        possibleAttacker = board.find(this.posRow+1, this.posColumn+2);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move up 1 then left 2 
        possibleAttacker = board.find(this.posRow+1, this.posColumn-2);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move down 1 right 2 
        possibleAttacker = board.find(this.posRow-1, this.posColumn+2);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // move down 1 left 2  
        possibleAttacker = board.find(this.posRow-1, this.posColumn-2);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        return null;
    }
}

class Rook extends Chesspiece {
    public Rook (String piecetype, String color, int posRow, int posColumn) {

        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn
        // For a rook, first check all the way to the left
        for (int col = this.posColumn - 1; col >= 0; col--) {
            Chesspiece possibleAttacker = board.find(this.posRow, col);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }        
        // For a rook, next check all the way up
        for (int row = this.posRow + 1; row <= board.boardSize-1; row++) {
            Chesspiece possibleAttacker = board.find(row, this.posColumn);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        // For a rook, next check all the way to the right
        for (int col = this.posColumn + 1; col <= board.boardSize-1; col++) {
            Chesspiece possibleAttacker = board.find(this.posRow, col);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }
        // For a rook, next check all the way down
        for (int row = this.posRow - 1; row >= 0; row--) {
            Chesspiece possibleAttacker = board.find(row, this.posColumn);
            if (possibleAttacker != null) {
                // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
                return possibleAttacker;  
            }
        }

        return null;
    }

}

class whitePawn extends Chesspiece {
    int i, j;
    public whitePawn (String piecetype, String color, int posRow, int posColumn) {

        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn
        // For a pawn, move up 1 right 1

        Chesspiece possibleAttacker = board.find(this.posRow+1, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a pawn, move up 1 left 1
        possibleAttacker = board.find(this.posRow+1, this.posColumn-1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        return null;
    }
}

class blackPawn extends Chesspiece {
    int i, j;
    public blackPawn (String piecetype, String color, int posRow, int posColumn) {

        super( piecetype, color, posRow, posColumn);
    }

    @Override
    public Chesspiece CheckAllAttacks(Chessboard board) {
        // First find any piece that this piece attacks.  This piece is at posRow, posColumn
        // For a pawn, move down one right one
        Chesspiece possibleAttacker = board.find(this.posRow-1, this.posColumn+1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        // For a pawn, move down one left one
        possibleAttacker = board.find(this.posRow-1, this.posColumn-1);
        if (possibleAttacker != null) {
            // Here we found a piece that this piece attacks.  Now check if that piece returns the attack
            return possibleAttacker;  
        }

        return null;
    }
}
