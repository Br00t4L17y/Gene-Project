import java.util.*;

public class BTreeTest
{
    int t=3;

    Node root;
    

    class Node
    {
        public Node(Node parent)
        {
            key = new Vector<String>(); 
            kids = new Vector<Node>();;
            this.parent=parent;
        }

        boolean isFull()
        {
            return key.size()==2*t-1;
        }

        boolean isLeaf()
        {
            return kids.size()==0;
        }

        int findChildIndex(String key)
        {
					int x;
            for(x=0;x<this.key.size();x++)
            {
                if(key.compareTo(this.key.get(x)) <= 0) return x;
            }
            return x;
        }

        Node getChild(int index)
        {
            return kids.get(index);
        }


        Vector<String> key;
        Vector<Node> kids;
        Node parent;
    }

    public BTreeTest()
    {
        root = new Node(null);
		}
		
		public BTreeTest(int t)
		{
			root = new Node(null);
			this.t=t;
		}

    private void insert(String key, Node curr)
    {
        if(curr.isFull()) // need to split
        {
           Node newNode = new Node(curr.parent);

           while(curr.key.size() > t-1)
           {
               if(!curr.isLeaf()) newNode.kids.add(0,curr.kids.remove(curr.kids.size()-1));
               newNode.key.add(0,curr.key.remove(curr.key.size()-1));
           }
           if(!curr.isLeaf()) newNode.kids.add(0,curr.kids.remove(curr.kids.size()-1));

           int i;

					 String medKey= curr.key.remove(curr.key.size()-1);
					 if (curr!=root)
					 {
						curr.parent.key.add(i=curr.parent.findChildIndex(medKey),medKey);
						curr.parent.kids.add(i+1,newNode);
					 }
        }
        if(curr.isLeaf()) // add key to node
        {
					curr.key.add(curr.findChildIndex(key), key);
           // need to write some code here
        }
        else insert(key,curr.getChild(curr.findChildIndex(key))); 

    }

    public void insert(String key)
    {
        insert(key,root);
    }

		private String toString(Node curr)
		{
			if(curr == null) return "";
			String result = "";
			int x;
			for (x = 0; x < curr.key.size(); x++) 
			{
				if (!curr.isLeaf()) result += toString(curr.kids.get(x)) + curr.key.get(x) + ", ";
				else result += curr.kids.get(x) + ", ";
			}
			if (!curr.isLeaf()) result += toString(curr.kids.get(x));
			return result;
		}

		public String toString()
		{
			return toString(this.root);
		}

    public static void main(String[] args)
    {
			String[] arr = {"a", "b", "c"};
			BTreeTest bt = new BTreeTest(3);

			for (String str : arr) {
				bt.insert(str);
			}

			System.out.print(bt.toString());
    }
}
