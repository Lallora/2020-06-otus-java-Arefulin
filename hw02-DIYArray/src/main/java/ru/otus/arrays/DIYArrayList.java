package ru.otus.arrays;

import java.util.*;

public class DIYArrayList<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENT = {};

    private int sizeArray;
    private transient Object[] mainArray;


    public DIYArrayList() {
        this.mainArray = new Object[DEFAULT_CAPACITY];
        this.sizeArray = 0;
    }

    public DIYArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.mainArray = new Object[initialCapacity];
            this.sizeArray = 0;
        } else if (initialCapacity == 0) {
            this.mainArray = new Object[DEFAULT_CAPACITY];
            this.sizeArray = 0;
        } else {
            throw new IllegalArgumentException("\n" +
                    "\tThe argument for DIYArrayList\n" +
                    "\tmust be a positive integer.\n" +
                    "\tBut entered " + initialCapacity);
        }
    }

    public int capacity() {
        return mainArray.length;
    }


    @Override
    public int size() {
        return sizeArray;
    }

    @Override
    public boolean isEmpty() {
        return sizeArray == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.mainArray, this.sizeArray);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E t) {
        add(sizeArray, t);
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index > mainArray.length || index < 0)
            throw new IndexOutOfBoundsException("Incorrect index = " + index);
        if (index == mainArray.length) {
            int newLength = this.sizeArray + this.sizeArray / 2;
            this.mainArray = Arrays.copyOf(this.mainArray, newLength);
        }
        if (index < mainArray.length) {
            mainArray[index] = element;
            sizeArray++;
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) this.mainArray[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldValue = (E) mainArray[index];
        mainArray[index] = element;
        return oldValue;
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (this.sizeArray == 0) return "[]";
        return outputStringFromIterator(listIterator());
//        return Arrays.toString(this.mainArray);
    }

    private static String outputStringFromIterator(ListIterator Itr) {
        StringBuilder str = new StringBuilder("[");
        while (Itr.hasNext()) {
            str.append(Itr.next()).append(", ");
        }
        str = new StringBuilder(str.substring(0,  str.length() - 2) + "]");
        return str.toString();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            if (cursor == 0)
                throw new NoSuchElementException("No previous element");
            cursor--;
            lastReturned = cursor;
            return get(cursor);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            if (cursor == 0)
                throw new NoSuchElementException("No previous index");
            return cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E t) {
            if (lastReturned < 0)
                throw new NoSuchElementException();
            DIYArrayList.this.set(lastReturned, t);
        }

        @Override
        public void add(E t) {
            DIYArrayList.this.add(cursor, t);
            cursor++;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        int cursor;
        int lastReturned = -1;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public E next() {
            int i = cursor;
            if (i >= sizeArray)
                throw new NoSuchElementException();
            lastReturned = i;
            cursor++;
            return get(i);
        }
    }

    private void checkIndex(int index) {
        if (index >= sizeArray || index < 0)
            throw new IndexOutOfBoundsException("Incorrect index = " + index);
    }
}
