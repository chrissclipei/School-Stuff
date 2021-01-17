 

// Emmanuel Huff 
// ehuff
// Chris Sclipei 
// csclipei
// HW 4
// 3/10/2017
// Filename: Node.java
// Contains the class Node that represents a node in a linked list
//
public class Node
{
    // instance variables - replace the example below with your ow
    private String myWord;
    private int count;
    private Node next;

    /**
     * Constructor for objects of class Node
     */
    public Node()
    {
        this.myWord = null;
        this.next = null;
    }

    public Node(String word){
        this.myWord = word;
        this.count = 1;
    }
    
    public Node getNext(){
        return this.next;
    }
    
    public void setNext(Node next){
        this.next = next;
    }
    
    public String getWord(){
        return this.myWord;
    }
    
    public int getCount(){
        return this.count;
    }
    
    public void incrementCount(){
        this.count += 1;
    }
    
}