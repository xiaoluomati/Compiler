package main;

public class Stack<Item> {
    
    private Node<Item> top;
    
    public void makeEmpty() {
        top = null;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public Item pop() {
        if(isEmpty())
            return null;
        Node<Item> temp = top;
        top = top.next();
        return temp.getData();
    }
    
    public void push(Item item) {
    	Node<Item> node = new Node<Item>(item);
        node.setNext(top);
        top = node;
    }
    
    public Item getTop() {
        return top.getData();
    }
    
    @SuppressWarnings("hiding")
	class Node<Item>{
        
        private Node<Item> next;
        
        private Item data;
        
        public Node(Item data) {
            this.data = data;
        }

        public Node<Item> next() {
            return next;
        }

        public void setNext(Node<Item> next) {
            this.next = next;
        }

        public Item getData() {
            return data;
        }

        public void setData(Item data) {
            this.data = data;
        }
        
    }

}
