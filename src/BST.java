import java.util.LinkedList;

/**
 * Created by kpatchirajan on 9/23/15.
 */
public class BST {

    class Node{
        int minimumAmount;
        String CSV;
        Node left;
        Node right;

        public Node(int minimumAmount, String csv){
            this.minimumAmount = minimumAmount;
            CSV = csv;
            left = null;
            right = null;
        }
    }
    Node root = null;

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
            if (minAmount <= node.minimumAmount)
                node.left = insert(node.left, minAmount,CSV);
            else
                node.right = insert(node.right, minAmount,CSV);
        }
        return node;
    }
    private LinkedList<String> list = new LinkedList<String>();
    public LinkedList<String> inOrderTraversal(){
        list = new LinkedList<String>();
        if(root!=null)
            inOrderTraversalUtil(root);
        return list;
    }

    private void inOrderTraversalUtil(Node root){
        if(root!=null){
            inOrderTraversalUtil(root.left);
            list.add(root.CSV);
            inOrderTraversalUtil(root.right);
        }
    }

}
