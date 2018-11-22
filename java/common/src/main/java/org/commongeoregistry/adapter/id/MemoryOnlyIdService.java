package org.commongeoregistry.adapter.id;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.commongeoregistry.adapter.HttpRegistryClient;

public class MemoryOnlyIdService implements AdapterIdServiceIF
{
  protected Set<String> cache;

  protected HttpRegistryClient client;
  
  protected Object lock;
  
  public MemoryOnlyIdService()
  {
    this.cache = new HashSet<String>(100);
    this.lock = new Object();
  }
  
  public void setClient(HttpRegistryClient client)
  {
    this.client = client;
  }
  
  @Override
  public void populate(int size)
  {
    synchronized(lock)
    {
      int amount = size - this.cache.size();
      
      Set<String> fetchedSet = this.client.getGeoObjectUids(amount);
      
      this.cache.addAll(fetchedSet);
    }
  }

  @Override
  public String next() throws EmptyIdCacheException
  {
    synchronized(lock)
    {
      if (this.cache.size() > 0)
      {
        Iterator<String> it = this.cache.iterator();
        
        String id = it.next();
        it.remove();
        
        return id;
      }
      else
      {
        throw new EmptyIdCacheException();
      }
    }
  }

}
