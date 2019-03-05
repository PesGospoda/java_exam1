package log;

import java.util.*;

public class FixedSizeQueue<E> implements Queue<E> {

    private int max_Size;
    private ArrayDeque<E> queue;

    public FixedSizeQueue(int maxSize) {
        max_Size = maxSize;
        queue = new ArrayDeque<E>(max_Size);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean add(E e) {
        if (queue.size() == max_Size)
            queue.poll();
        return queue.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return queue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return queue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public void clear() {
        queue.clear();

    }

    @Override
    public boolean offer(E e) {
        return queue.offer(e);
    }

    @Override
    public E remove() {
        return queue.remove();
    }

    @Override
    public E poll() {
        return queue.poll();
    }

    @Override
    public E element() {
        return queue.element();
    }

    @Override
    public E peek() {
        return queue.peek();
    }

    public ArrayList<E> subList(int start, int end) {//ToDo здесь могут быть некорректные значения нужно обрабатывать
        ArrayList<E> outList = new ArrayList<E>();
        ArrayDeque<E> tempQueue = queue.clone();
        for (int i = 0; i < start; i++) {
            tempQueue.poll();
        }
        for (int i = start; i < end; i++) {
            outList.add(tempQueue.poll());
        }
        return outList;
    }
}
