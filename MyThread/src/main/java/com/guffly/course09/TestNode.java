package com.guffly.course09;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-17 00:12
 **/
public class TestNode {
    Node haed;
    private Node last;

    public TestNode() {
        this.haed = last = new Node(null);
    }

    public boolean add(Object o){
        Node node = new Node(o);
        last = last.next = node;
        return true;
    }

    public static void main(String[] args) {
        TestNode testNode = new TestNode();
        testNode.add(1);
        testNode.add(2);
        testNode.add(3);
    }

    class Node{
        private Object o;
        private Node next;

        public Node(Object o) {
            this.o = o;
            this.next = null;
        }

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getNext() {
            return next;
        }
    }
}
