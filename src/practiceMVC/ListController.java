package practiceMVC;

/**
 * A generic double-linked list controller for managing elements of type E (generic).
 *
 * @param <E> The generic type of elements to be stored in the list
 */
public class ListController<E> {
    /**
     * Reference to the first node in the list.
     */
    private Node<E> firstNode;

    /**
     * Reference to the last node in the list.
     */
    private Node<E> lastNode;

    /**
     * Counter attribute to store the number of total elements in the list.
     */
    private int totalElements = 0;

    /**
     * Initializes an empty ListController.
     */
    public ListController() {
        this.firstNode = null;
        this.lastNode = null;
    }

    /**
     * DEBUGGING PURPOSES. Displays the elements in the list from the first element to the last.
     */
    public void showMyListForward() {
        Node<E> temporary = this.firstNode;
        while (temporary != null) {
            System.out.println(temporary.getMain());
            temporary = temporary.getNextNode();
        }
    }

    /**
     * DEBUGGING PURPOSES. Displays the elements in the list from the last element to the first.
     */
    public void showMyListBackward() {
        Node<E> temporary = this.lastNode;
        while (temporary != null) {
            System.out.println(temporary.getMain());
            temporary = temporary.getPreviousNode();
        }
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param p The element to be added to the list.
     */
    public void add(E p) {
        // Create a new node containing the provided element.
        Node<E> newNode = new Node(p);

        // If the list is empty, set the new node as both the first and last node.
        if (firstNode == null) {
            firstNode = newNode;
            lastNode = newNode;
        } else {
            // Link the new node to the previous last node.
            newNode.setPreviousNode(lastNode);
            // Update the next node of the previous last node to point to the new node.
            lastNode.setNextNode(newNode);
            // Update the last node to be the new node.
            lastNode = newNode;
        }
        // Update the total elements count.
        totalElements++;
    }

    /**
     * Removes the first occurrence of an element from the list.
     *
     * @param p The element to be removed from the list.
     */
    public void remove(E p) {
        // Start from the first node.
        Node<E> current = firstNode;

        // Traverse the list to find the element to remove.
        while (current != null) {
            if (current.getMain().equals(p)) {
                Node<E> previous = current.getPreviousNode();
                Node<E> next = current.getNextNode();
                if (previous != null) {
                    // Update the next node of the previous node to skip the current node.
                    previous.setNextNode(next);
                } else {
                    // If the removed node was the first node, update the first node.
                    firstNode = next;
                }
                if (next != null) {
                    // Update the previous node of the next node to skip the current node.
                    next.setPreviousNode(previous);
                } else {
                    // If the removed node was the last node, update the last node.
                    lastNode = previous;
                }
                // Update the total elements count.
                totalElements--;
                return; // Element removed.
            }
            current = current.getNextNode();
        }
    }

    /**
     * Gets the position (index) of the first occurrence of an element in the list.
     *
     * @param p The element to find the position of.
     * @return The position (index) of the element in the list. Returns -1 if the element is not found.
     */
    public int getPosition(E p) {
        int position = -1;
        int index = 0;
        Node<E> currentNode = firstNode;

        // Traverse the list to find the element's position.
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


    /**
     * Gets the total number of elements in the list.
     *
     * @return The total number of elements in the list.
     */
    public int getTotalElements() {
        return totalElements;
    }

    /**
     * DEBUGGING PURPOSES. Checks if the list is empty.
     *
     * @return `true` if the list is empty, `false` otherwise.
     */
    public boolean isEmpty() {
        // The list is empty if there is no first node.
        return firstNode == null;
    }

    /**
     * Gets the first node in the list.
     *
     * @return The first node in the list.
     */
    public Node<E> getFirstNode() {
        return firstNode;
    }

    /**
     * DEBUGGING PURPOSES. Gets the last node in the list.
     *
     * @return The last node in the list.
     */
    public Node<E> getLastNode() {
        return lastNode;
    }

    /**
     * Inner class representing a node in the double-linked list.
     *
     * @param <E> The generic type of elements stored in the node.
     */
    public class Node<E> {
        /**
         * Reference to the next node in the list.
         */
        private Node<E> nextNode;

        /**
         * Reference to the previous node in the list.
         */
        private Node<E> previousNode;

        /**
         * The element (focused data) stored in the node.
         */
        E main;

        /**
         * Initializes a new list node with the given element.
         *
         * @param p The element to be stored in the node.
         */
        public Node(E p) {
            this.nextNode = null;
            this.previousNode = null;
            this.main = p;
        }

        /**
         * Gets the next node in the list.
         *
         * @return The next node in the list.
         */
        public Node<E> getNextNode() {
            return this.nextNode;
        }

        /**
         * Sets the next node in the list.
         *
         * @param nextNode The next node to be set.
         */
        public void setNextNode(Node<E> nextNode) {
            this.nextNode = nextNode;
        }

        /**
         * Gets the previous node in the list.
         *
         * @return The previous node in the list.
         */
        public Node<E> getPreviousNode() {
            return this.previousNode;
        }

        /**
         * Sets the previous node in the list.
         *
         * @param previousNode The previous node to be set.
         */
        public void setPreviousNode(Node<E> previousNode) {
            this.previousNode = previousNode;
        }

        /**
         * Gets the element stored in the node.
         *
         * @return The element stored in the node.
         */
        public E getMain() {
            return main;
        }

        /**
         * DEBUGGING PURPOSES. Sets the element stored in the node.
         *
         * @param p The element to be set.
         */
        public void setMain(E p) {
            this.main = p;
        }
    }
}
