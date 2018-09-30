package org.commongeoregistry.adapter;

import org.commongeoregistry.adapter.RegistryServerInterface;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class RegistryServerInterfaceTest
{
  
  private static RegistryServerInterface registryServerInterface;
  
  public RegistryServerInterfaceTest()
  {
    super();
  }

  @BeforeClass
  public static void classSetUp()
  {
    registryServerInterface = new RegistryServerInterface();
    
    
    
    
    System.out.println("ClassSetUp");
  }
  
  
  
  @Test
  public static void testCache()
  {
    registryServerInterface.getMetadataCache().clear();
    
    System.out.println("testCache");
    
   Assert.assertTrue(true);
  }
  
  @Test
  public void testPass()
  {
   Assert.assertTrue(true);
  }
  
//  @Test
//  public void testFail()
//  {
//   Assert.assertTrue(false);
//  }
  
}
