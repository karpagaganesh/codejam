import java.util.LinkedList;

public class BinarySearchTree {

    class Node{
        int minimumAmount;  // "Atleast Amount" present in Tax table.
        String CSV;         // Tax table entry in CSV format.
        Node left;          // Left reference, value lesser than the root node.
        Node right;         // Right reference, value greater than the root node.

        public Node(int minimumAmount, String csv){ // Initialized using constructor.
            this.minimumAmount = minimumAmount;
            CSV = csv;
            left = null;
            right = null;
        }
    }

    Node root = null;   // Binary Search Tree => Root Node

    public void insert(int minAmount, String csv)
    {
        root = insert(root, minAmount,csv);
    }

    private Node insert(Node node, int minAmount, String CSV)
    {
        if (node == null)
            node = new Node(minAmount,CSV);
        else
        {
            /* This block will ignore duplicate entries from tables.
            *  If any PDF has range/values in two different pages, then modify the condition as
            *  if (minAmount <= node.minimumAmount) and remove else if statements condition
            */

            if (minAmount < node.minimumAmount)
                node.left = insert(node.left, minAmount,CSV);
            else if (minAmount > node.minimumAmount)
                node.right = insert(node.right, minAmount,CSV);
        }
        return node;
    }

    private LinkedList<String> list = new LinkedList<String>();

    public LinkedList<String> inOrderTraversal(){
        list = new LinkedList<String>();        // List of Tax table entries in sorted (ascending) order.
        if(root!=null)
            inOrderTraversalUtil(root);
        return list;
    }

    /*
    *   Retrieves processed values in sorted order.
    */
    private void inOrderTraversalUtil(Node root){
        if(root!=null){
            inOrderTraversalUtil(root.left);
            list.add(root.CSV);
            inOrderTraversalUtil(root.right);
        }
    }

}
