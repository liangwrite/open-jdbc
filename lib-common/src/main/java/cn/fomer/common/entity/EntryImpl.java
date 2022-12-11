/**
 * 
 */
package cn.fomer.common.entity;

import java.util.Map.Entry;

/**
 * 2021-03-24
 * 
 */
public class EntryImpl<K, V> implements Entry<K, V> {
	
	K k;
	V v;
	
	
	public EntryImpl(K k, V v)
	{
		this.k= k;
		this.v= v;
	}

	@Override
	public K getKey() {
		// TODO Auto-generated method stub
		return k;
	}

	@Override
	public V getValue() {
		// TODO Auto-generated method stub
		return v;
	}

	@Override
	public V setValue(V value) {
		// TODO Auto-generated method stub
		return this.v= value;
	}

}
