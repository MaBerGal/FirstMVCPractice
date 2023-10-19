package practiceMVC;

import java.util.Calendar;
public class EmployeeController<E> {
    // Fields for the full list of employees
    private Node<E> firstNode;
    private Node<E> lastNode;


    // Counter attribute for the total elements
    private int totalElements = 0;

    public EmployeeController() {
        this.firstNode = null;
        this.lastNode = null;
    }

    public void showMyListForward() {
        Node<E> temporary = this.firstNode;
        while (temporary != null) {
            System.out.println(temporary.getMain());
            temporary = temporary.getNextNode();
        }
    }

    public void showMyListBackward() {
        Node<E> temporary = this.lastNode;
        while (temporary != null) {
            System.out.println(temporary.getMain());
            temporary = temporary.getPreviousNode();
        }
    }

    public void add(E p) {
        Node<E> newNode = new Node(p);
        if (firstNode == null) {
            firstNode = newNode;
            lastNode = newNode;
        } else {
            newNode.setPreviousNode(lastNode);
            lastNode.setNextNode(newNode);
            lastNode = newNode;
        }
        // Update the total employees count
        totalElements++;
    }

    public void remove(E p) {
        Node<E> current = firstNode;
        while (current != null) {
            if (current.getMain().equals(p)) {
                Node<E> previous = current.getPreviousNode();
                Node<E> next = current.getNextNode();
                if (previous != null) {
                    previous.setNextNode(next);
                } else {
                    firstNode = next;
                }
                if (next != null) {
                    next.setPreviousNode(previous);
                } else {
                    lastNode = previous;
                }
                // Update the total employees count
                totalElements--;
                return; // Element removed
            }
            current = current.getNextNode();
        }
    }

    public int getPosition(E p) {
        int position = -1;
        int index = 0;
        Node<E> currentNode = firstNode;

        while (currentNode != null) {
            if (currentNode.getMain().equals(p)) {
                position = index;
                break;
            }
            currentNode = currentNode.getNextNode();
            index++;
        }

        return position;
    }


    public int getTotalElements() {
        return totalElements;
    }

    public boolean isEmpty() {
        return firstNode == null;
    }

    public Node<E> getFirstNode() {
        return firstNode;
    }

    public Node<E> getLastNode() {
        return lastNode;
    }


    // Inner class
    public class Node<E> {
        private Node<E> nextNode;
        private Node<E> previousNode;
        E main;

        public Node(E p) {
            this.nextNode = null;
            this.previousNode = null;
            this.main = p;
        }

        public Node<E> getNextNode() {
            return this.nextNode;
        }

        public void setNextNode(Node<E> nextNode) {
            this.nextNode = nextNode;
        }

        public Node<E> getPreviousNode() {
            return this.previousNode;
        }

        public void setPreviousNode(Node<E> previousNode) {
            this.previousNode = previousNode;
        }

        public E getMain() {
            return main;
        }

        public void setMain(E p) {
            this.main = p;
        }
    }
}
