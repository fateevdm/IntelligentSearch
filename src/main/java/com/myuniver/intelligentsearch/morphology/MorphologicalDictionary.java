package com.myuniver.intelligentsearch.morphology;

import com.myuniver.intelligentsearch.structure.Dictionary;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * User: Dima
 * Date: 03.02.13
 * Time: 1:51
 */
public class MorphologicalDictionary<V> implements Dictionary<String, V> {

    private int N;       // size
    private Node root;   // root of TST

    @Override
    public String getByValue(V value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean containsValue(V value) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Dictionary<String, V> clear() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Dictionary<String, V> putAll(Dictionary<? extends String, ? extends V> dict) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<V> values() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * return number of key-value pairs
     */
    public int size() {
        return N;
    }

    /**
     * Is string key in the symbol table?
     */
    @Override
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    @Override
    public V get(String key) {
        if (key == null || key.length() == 0) {
            throw new RuntimeException("illegal key");
        }
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (V) x.val;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (key == null || key.length() == 0) throw new RuntimeException("illegal key");
        if (x == null) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }

    /**
     * Insert string s into the symbol table.
     */
    @Override
    public Dictionary<String, V> put(String s, V val) {
        if (!containsKey(s)) N++;
        root = put(root, s, val, 0);
        return this;
    }

    private Node put(Node x, String s, V val, int d) {
        char c = s.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) x.left = put(x.left, s, val, d);
        else if (c > x.c) x.right = put(x.right, s, val, d);
        else if (d < s.length() - 1) x.mid = put(x.mid, s, val, d + 1);
        else x.val = val;
        return x;
    }

    /**
     * Find and return longest prefix of s in Dictionary
     */
    public String longestPrefixOf(String s) {
        if (s == null || s.length() == 0) return null;
        int length = 0;
        Node x = root;
        int i = 0;
        while (x != null && i < s.length()) {
            char c = s.charAt(i);
            if (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return s.substring(0, i);
    }

    // all keys in symbol table
    public Iterable<String> keys() {
        Queue<String> queue = new ArrayDeque<>();
        collect(root, "", queue);
        return queue;
    }

    /**
     * all keys starting with given prefix
     */
    public Iterable<String> prefixMatch(String prefix) {
        Queue<String> queue = new ArrayDeque<>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.add(prefix);
        collect(x.mid, prefix, queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, String prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left, prefix, queue);
        if (x.val != null) queue.add(prefix + x.c);
        collect(x.mid, prefix + x.c, queue);
        collect(x.right, prefix, queue);
    }

    /**
     * return all keys matching given wilcard pattern
     *
     * @param pat
     * @return
     */
    public Iterable<String> wildcardMatch(String pat) {
        Queue<String> queue = new ArrayDeque<>();
        collect(root, "", 0, pat, queue);
        return queue;
    }

    private void collect(Node x, String prefix, int i, String pat, Queue<String> q) {
        if (x == null) return;
        char c = pat.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pat, q);
        if (c == '.' || c == x.c) {
            if (i == pat.length() - 1 && x.val != null) q.add(prefix + x.c);
            if (i < pat.length() - 1) collect(x.mid, prefix + x.c, i + 1, pat, q);
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pat, q);
    }

    @Override
    public V delete(String key) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    private class Node {
        private char c;                 // character
        private Node left, mid, right;  // left, middle, and right subtries
        private V val;              // value associated with string

        @Override
        public String toString() {
            return "Node{" +
                    "c=" + c +
                    ", left=" + left +
                    ", mid=" + mid +
                    ", right=" + right +
                    ", val=" + val +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PrototypeMorphologicalDictionary{" +
                "N=" + N +
                ", root=" + root +
                '}';
    }

}
