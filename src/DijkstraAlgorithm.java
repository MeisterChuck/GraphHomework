import java.util.*;

class Node {
    int val;
    int key;
    Node parent;

    public Node(int val) {
        this.val = val;
        this.key = Integer.MAX_VALUE;
        this.parent = null;
    }
}

class MinHeap {
    int capacity;
    Node arr[];
    int pos[];
    int size;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = capacity;
        this.arr = new Node[capacity];
        this.pos = new int[size];
    }

    void swap(Node[] arr, int i, int j) {
        Node temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        int tempIndex = pos[arr[i].val];
        pos[arr[i].val] = pos[arr[j].val];
        pos[arr[j].val] = tempIndex;
    }

    int parent(int i) {
        return (i-1)/2;
    }

    int left(int i) {
        return 2*i + 1;
    }

    int right(int i) {
        return 2*i + 2;
    }

    Node extractMin() {
        if(size <= 0) {
            System.out.println("Heap underflow");
            return null;
        }
        if(size == 1) {
            size--;
            return arr[0];
        }

        Node root = arr[0];

        arr[0] = arr[size-1];

        // change indices
        pos[arr[size-1].val] = 0;
        pos[root.val] = size-1;

        size--;
        minHeapify(0);

        return root;
    }

    void minHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;

        if(l < size && arr[l].key < arr[smallest].key)
            smallest = l;
        if(r < size && arr[r].key < arr[smallest].key)
            smallest = r;
        if(smallest != i) {
            swap(arr,i,smallest);
            minHeapify(smallest);
        }
    }

    void fixUpwards(int i) {
        while(i != 0 && arr[i].key < arr[parent(i)].key) {
            swap(arr,i,parent(i));
            i = parent(i);
        }
    }

    void decreaseKey(Node v, int newKey) {
        int index = pos[v.val];
        if(arr[index].key < newKey) {
            System.out.println("New key is greater.");
            return;
        }

        arr[index].key = newKey;
        fixUpwards(index);
    }

    boolean isInMinHeap(Node node) {
        if(pos[node.val] < size)
            return true;
        return false;
    }
}

class MyLinkedList extends LinkedList<Node> {
}

class MyArrayList extends LinkedList<Integer> {
}

class Graph {
    int V;
    MyLinkedList[] lists;
    MyArrayList[] weights;

    public Graph(int V) {
        this.V = V;
        this.lists = new MyLinkedList[V];
        this.weights = new MyArrayList[V];

        for(int i=0;i<V;i++) {
            lists[i] = new MyLinkedList();
            weights[i] = new MyArrayList();
        }

        for(int i=0;i<V;i++) {
            Node node = new Node(i);
            lists[i].add(node);
        }
    }

    void addEdge(int u, int v, int weight) {
        lists[u].add(lists[v].getFirst());
        lists[v].add(lists[u].getFirst());
        weights[u].add(weight);
        weights[v].add(weight);
    }

    void printGraph() {
        for(MyLinkedList l : lists) {
            int i = 0;
            for(Node node : l) {
                if(i == 0)
                    System.out.print(node.val + " : ");
                else
                    System.out.print(node.val + "(" + weights[l.getFirst().val].get(i-1) + ") ");
                i++;
            }
            System.out.println();
        }
    }

    void printPath(Node node) {
        if(node.parent  != null) {
            printPath(node.parent);
            System.out.print(node.val + " ");
        }
        else
            System.out.print("0 ");
    }

    void printDijkstraSolution() {
        int src = 0;
        System.out.println("\nVertex\tDistance\tPath");
        for(MyLinkedList l : lists) {
            Node node = l.getFirst();
            if(node.parent != null) {
                System.out.print(src + "->" + node.val + "\t" + node.key + "\t\t\t");
                printPath(node);
                System.out.println();
            }
        }
    }

    void dijkstra() {
        MinHeap mh = new MinHeap(V);

        lists[0].getFirst().key = 0;
        lists[0].getFirst().parent = null;

        for(int i=0;i<V;i++) {
            mh.arr[i] = lists[i].getFirst();
            mh.pos[i] = i;
        }

        while(mh.size > 0) {
            Node u = mh.extractMin();

            int i=0;
            for(Node v : lists[u.val]) {
                if(i == 0) {
                    i++;
                    continue;
                }

                if((mh.isInMinHeap(v)) && (u.key + weights[u.val].get(i-1) < v.key)) {
                    v.key = u.key + weights[u.val].get(i-1);
                    v.parent = u;
                    mh.decreaseKey(v,v.key);
                }
                i++;
            }
        }

        printDijkstraSolution();
    }
}

class Dijkstra{

    public static void main(String[] args) {
        int V = 9;
        Graph g = new Graph(V);

        g.addEdge(0, 1, 4);
        g.addEdge(0, 7, 8);
        g.addEdge(1, 2, 8);
        g.addEdge(1, 7, 11);
        g.addEdge(2, 3, 7);
        g.addEdge(2, 8, 2);
        g.addEdge(2, 5, 4);
        g.addEdge(3, 4, 9);
        g.addEdge(3, 5, 14);
        g.addEdge(4, 5, 10);
        g.addEdge(5, 6, 2);
        g.addEdge(6, 7, 1);
        g.addEdge(6, 8, 6);
        g.addEdge(7, 8, 7);

        System.out.println("Graph:");
        g.printGraph();

        g.dijkstra();
    }
}