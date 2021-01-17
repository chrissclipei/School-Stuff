ChessPuzzle.jar: ChessBoard.class Node.class ListOperations.class Utilities.class ChessPiece.class King.class Queen.class Rook.class Bishop.class Knight.class Pawn.class
	echo Main-class: ChessBoard > Manifest
	jar cvfm ChessPuzzle.jar Manifest ChessBoard.class Node.class ListOperations.class Utilities.class ChessPiece.class King.class Queen.class Rook.class Bishop.class Knight.class Pawn.class
	rm Manifest

ChessBoard.class Node.class ListOperations.class Utilities.class ChessPiece.class King.class Queen.class Rook.class Bishop.class Knight.class Pawn.class: ChessBoard.java Node.java ListOperations.java Utilities.java ChessPiece.java King.java Queen.java Rook.java Bishop.java Knight.java Pawn.java
	javac -Xlint *.java

clean:
	rm *.class
	rm *.jar
