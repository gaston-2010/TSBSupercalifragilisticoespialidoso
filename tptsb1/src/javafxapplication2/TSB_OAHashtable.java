/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Usuario
 * @param <K>
 * @param <V>
 */
public class TSB_OAHashtable<K, V> implements Map<K, V>, Cloneable, Serializable 
{
    private final static int MAX_SIZE = Integer.MAX_VALUE;
    private Entry<K,V>[] tabla;
    private int initial_capacity;
    private int count;
    private float load_factor;
    
    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;
    
    protected transient int modCount;
    
    public TSB_OAHashtable()
    {
        this(13,0.5f);
    }
    
    public TSB_OAHashtable(int initial_capacity)
    {
        
        this(initial_capacity, 0.5f);
    }
    
    public TSB_OAHashtable(int initialcapacity, float loadfactor)
    {
        if(loadfactor <= 0) { load_factor = 0.5f; }
        if(initialcapacity <= 0) { initial_capacity = 13; }
        else
        {
            if(initialcapacity > TSB_OAHashtable.MAX_SIZE)
            {
               initial_capacity = TSB_OAHashtable.MAX_SIZE;
            }
            else
            {
               if (!esPrimo(initialcapacity)){
                    initial_capacity = siguientePrimo(initialcapacity);
               }
            }
            
            
        } 
        this.tabla = new Entry[initialcapacity];
        
        
        load_factor = loadfactor;
        count = 0;
        modCount = 0;
    
        
    }
    
    public TSB_OAHashtable(Map<? extends K,? extends V> t)
    {
        this(13, 0.5f);
        this.putAll(t);
    }
    
    public int siguientePrimo ( int n )
    {
        if ( n % 2 == 0) n++;
        for ( ; !esPrimo(n); n+=2 ) ;
        return n;
    }
    

    public boolean esPrimo(int n){
    int a = 0, i;
    for (i = 1; i < (n + 1); i++) 
        if (n % i == 0)  a++;
    if (a != 2) return false;
    else return true;
    }
    
    @Override
    public int size() {
        return this.tabla.length;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < tabla.length; i++) {
            if(tabla[i]!=null) return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < this.tabla.length; i++) {
            if(key.equals(this.tabla[i].getKey())) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < tabla.length; i++) {
            if(value.equals(tabla[i].getValue())) return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < tabla.length; i++) {
            if(key.equals(tabla[i].getKey())) return tabla[i].getValue();
        }
        return null;
    }
    
    public int h(int k)
    {
        return h(k, this.tabla.length);
    }
    
    public int h(K key)
    {
        return h(key.hashCode(), this.tabla.length);
    }
    
    public int h(K key, int t)
    {
        return h(key.hashCode(), t);
    }
    
    public int h(int k, int t)
    {
        if(k < 0) k *= -1;
        return k % t;
    }
    
    public Entry<K, V> search_for_entry(V value, K key, Entry<K, V>[] bucket)
    {
        int hash = h(key);
        boolean corte = true;
        int i = hash;
        while (corte)
        {
            Entry<K, V> entry = bucket[i];
            if (entry != null &&  entry.getEstado()==1)
            {
                if(key.equals(entry.getKey())&& value.equals(entry.getValue())) return entry;
            }
            i++;
            if (i==tabla.length)i=0;
            if (i==hash)corte=false;
           
        }
        return null;
    }
    
    public float averageLength()
    {
        return (float)count/tabla.length;
    }
    
    protected void rehash()
    {
        int old_t = this.tabla.length;

        int new_t = siguientePrimo(old_t * 2);
        TSB_OAHashtable<K, V> temp= new TSB_OAHashtable(new_t);
        
        if(new_t > TSB_OAHashtable.MAX_SIZE)
        {
            new_t = TSB_OAHashtable.MAX_SIZE;
        }
        this.modCount++;
        
        for (int i=0;i<old_t;i++)
        {
            if(tabla[i]!=null) temp.add(tabla[i]);
        }
        this.tabla = temp.tabla;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        TSB_OAHashtable<K, V> temp = (TSB_OAHashtable<K, V>) super.clone();
        temp.tabla = new Entry[this.tabla.length];
        System.arraycopy(this.tabla, 0, temp.tabla, 0, count);

        temp.modCount = 0; 

        return temp;
    }
    
    public void add(Entry<K, V> entry)
    {
        if (entry == null) return;
        int ib = h(entry.getKey());
        int i = ib;
        boolean corte = true;
        while(corte){
            if (tabla[i]==null)
            {
                tabla[i] = entry;
                corte=false;
                this.count++;
                this.modCount++;
            }
            else
            {
                if (tabla[i].getEstado()==2 ) 
                {   
                    System.arraycopy(tabla,i+1,tabla,i+1,tabla.length-i-1);
                    
                    tabla[i] = entry;
                    corte=false;
                    this.count++;
                    this.modCount++;
                }
                i++;
                if (i==tabla.length) i=0;
                if (i==ib) corte=false;
            }
        }
    }
    
    
    
    @Override
    public V put(K key, V value) {
        if(key == null || value == null) throw new NullPointerException("put(): parámetro null");
        
        
        Entry<K,V>[] bucket = this.tabla;
        
        Map.Entry<K, V> x = this.search_for_entry(value, key, bucket);
        
        if(x != null)
        {
            System.out.println("Existe ya ese elemento.");
        }
        else
        {
            
            if(this.averageLength() >= this.load_factor) this.rehash();
            Entry<K, V> entry = new Entry<>(key, value);
            add(entry);
            
        }
        return value;
        
    }
    
    public Entry<K,V>[] getTabla()
    {
        return tabla;
    }

    @Override
    public V remove(Object key) {
        int ib = h((Integer)key);
        if(ib >= tabla.length || ib < 0)
        {
            throw new IndexOutOfBoundsException("remove(): índice fuera de rango...");
        }
        
        int t = tabla.length;
        
        Entry<K,V> old = tabla[ib]; 
        old.setEstado(2);
        count--;
        this.modCount++; 
        
        return  old.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
       for(Map.Entry<? extends K, ? extends V> e : m.entrySet())
        {
            put(e.getKey(), e.getValue());
        } 
    }

    @Override
    public void clear() {
        for(int i=0;i<tabla.length;i++) {V old = this.remove(i);}
    }

    @Override
    public Set<K> keySet() {
        if(keySet == null)
            {
                keySet = new KeySet();
            }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if(values==null)
            {
                values=new ValueCollection();
            }
        return values;
    }

    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if(entrySet==null)
            {
                entrySet = new EntrySet();
            }
        return entrySet;
    }
    
    public Entry<K,V> getInfo(K key)
    {
          return tabla[h(key)];
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.deepHashCode(this.tabla);
        hash = 89 * hash + this.initial_capacity;
        hash = 89 * hash + this.count;
        hash = 89 * hash + Float.floatToIntBits(this.load_factor);
        hash = 89 * hash + Objects.hashCode(this.keySet);
        hash = 89 * hash + Objects.hashCode(this.entrySet);
        hash = 89 * hash + Objects.hashCode(this.values);
        hash = 89 * hash + this.modCount;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TSB_OAHashtable<?, ?> other = (TSB_OAHashtable<?, ?>) obj;
        if (this.initial_capacity != other.initial_capacity) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        if (Float.floatToIntBits(this.load_factor) != Float.floatToIntBits(other.load_factor)) {
            return false;
        }
        if (this.modCount != other.modCount) {
            return false;
        }
        if (!Arrays.deepEquals(this.tabla, other.tabla)) {
            return false;
        }
        if (!Objects.equals(this.keySet, other.keySet)) {
            return false;
        }
        if (!Objects.equals(this.entrySet, other.entrySet)) {
            return false;
        }
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TSB_OAHashtable{" + "tabla=" + toStringTabla() + ", initial_capacity=" + initial_capacity + ", count=" + count + ", load_factor=" + load_factor + ", keySet=" + keySet + ", entrySet=" + entrySet + ", values=" + values + ", modCount=" + modCount + '}';
    }
    public String toStringTabla()
    {
        StringBuilder buff = new StringBuilder();
        buff.append('{');
        boolean primero = true;
        for (int i=0; i<tabla.length; i++)
        {
            if(tabla[i]!=null && tabla[i].getEstado() == 1 && primero){
                buff.append(tabla[i]);
                primero = false;
            }
            else
            {
                if(tabla[i]!= null && tabla[i].getEstado() == 1) buff.append(", " + tabla[i]);
            }
        }
        buff.append('}');
        return buff.toString();
    }
    private class Entry<K, V> implements Map.Entry<K, V>
    {
        private K key;
        private V value;
        //El atributo estado es 1=cerrado y 2=tumba
        private int estado;
        
        public Entry(K key, V value)
        {
            if(key==null || value==null)
            {
                throw new IllegalArgumentException("Entry(): parametros nulls");
            }
            this.key = key;
            this.value = value;
            this.estado = 1;
        }

        public int getEstado() {
            return this.estado;
        }

        public void setEstado(int estado) {
            this.estado = estado;
        }
        
        
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }
        
        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 19 * hash + Objects.hashCode(this.key);
            hash = 19 * hash + Objects.hashCode(this.value);
            hash = 19 * hash + this.estado;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Entry<?, ?> other = (Entry<?, ?>) obj;
            if (this.estado != other.estado) {
                return false;
            }
            if (!Objects.equals(this.key, other.key)) {
                return false;
            }
            if (!Objects.equals(this.value, other.value)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Entry{" + "key=" + key + ", value=" + value + ", estado=" + estado + '}';
        }

        
        
    }
    
    private class KeySet extends AbstractSet<K>
    {
          
        @Override
        public Iterator<K> iterator() {
            return new KeySetIterator();
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private class KeySetIterator implements Iterator <K>
        {
            
            private int current_entry;
            private boolean next_ok;
            
            public KeySetIterator()
            {
                current_entry = -1;
                next_ok = false;
            }
            
            @Override
            public boolean hasNext() {
                Entry<K, V> t[] = TSB_OAHashtable.this.tabla;
                if(TSB_OAHashtable.this.isEmpty()) { return false; }
                if(current_entry >= TSB_OAHashtable.this.size() -1) return false;
                
                int next_entry = current_entry + 1;
                while(next_entry < t.length && t[next_entry]==null)
                {
                    next_entry++;
                }
                if(next_entry >= t.length) { return false; }
                return true;
            }

            @Override
            public K next() {
                if(!hasNext())
                {
                    System.out.println("No hay elemento siguiente");
                    return null;
                }
                
                Entry<K, V> t[] = TSB_OAHashtable.this.tabla;
                
                if(current_entry>-1)
                {
                    Entry<K, V> entry = t[current_entry];
                
                    int last_entry = current_entry;
                }
                current_entry++;
                while(t[current_entry] == null || t[current_entry].getEstado()==2)
                {
                    current_entry++;
                }
                K key = t[current_entry].getKey();
                next_ok = true;
                return key;
            }

            
        }
    }
    
    private class EntrySet extends AbstractSet<Map.Entry<K,V>>
    {
        
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntrySetIterator();
        }


        @Override
        public int size() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private class  EntrySetIterator implements Iterator<Map.Entry<K,V>>
        {
            
            private int current_entry;
            private boolean next_ok;
            
            public EntrySetIterator()
            {
                current_entry = -1;
                next_ok = false;
            }
            
            @Override
            public boolean hasNext() {
                Entry<K, V> t[] = TSB_OAHashtable.this.tabla;
                if(TSB_OAHashtable.this.isEmpty()) { return false; }
                if(current_entry >= TSB_OAHashtable.this.size() -1) return false;
                
                int next_entry = current_entry + 1;
                while(next_entry < t.length && t[next_entry]==null)
                {
                    next_entry++;
                }
                if(next_entry >= t.length) { return false; }
                return true;
            }

            @Override
            public Entry<K, V> next() {
                
                if(!hasNext())
                {
                    System.out.println("No hay elemento siguiente");
                    return null;
                }
                
                Entry<K, V> t[] = TSB_OAHashtable.this.tabla;
                
                if(current_entry>-1)
                {
                    Entry<K, V> entry = t[current_entry];

                    int last_entry = current_entry;
                }
                current_entry++;
                while(t[current_entry] == null || t[current_entry].getEstado()==2)
                {
                    current_entry++;
                }
                Entry<K, V> entry = t[current_entry];
                next_ok = true;
                return entry;
            }

            
            
            
        }
    }
    
    private class ValueCollection extends AbstractCollection<V>
    {
        @Override
        public Iterator<V> iterator() {
            return new ValueCollectionIterator();
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        private class ValueCollectionIterator implements Iterator<V>
        {   
            private int current_entry;
            private boolean next_ok;
            
            public ValueCollectionIterator()
            {
                current_entry = -1;
                next_ok = false;
            }
            
            @Override
            public boolean hasNext() {
                Entry<K, V> t[] = TSB_OAHashtable.this.tabla;
                if(TSB_OAHashtable.this.isEmpty()) { return false; }
                if(current_entry >= TSB_OAHashtable.this.size() -1) return false;
                
                int next_entry = current_entry + 1;
                while(next_entry < t.length && t[next_entry]==null)
                {
                    next_entry++;
                }
                if(next_entry >= t.length) { return false; }
                return true;
            }

            @Override
            public V next() {
                if(!hasNext())
                {
                    System.out.println("No hay elemento siguiente");
                    return null;
                }
                
                Entry<K, V> t[] = TSB_OAHashtable.this.tabla;
                if(current_entry>-1)
                {
                    Entry<K, V> entry = t[current_entry];
                
                    int last_entry = current_entry;
                }
                current_entry++;
                while(t[current_entry] == null || t[current_entry].getEstado()==2)
                {
                    current_entry++;
                }
                V value = t[current_entry].getValue();
                next_ok = true;
                return value;
            }

            @Override
            public void remove() {
                Iterator.super.remove(); //To change body of generated methods, choose Tools | Templates.
            }
            
            
        }
    }
    
}
