package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.commongeoregistry.adapter.HttpRegistryClient;
import org.commongeoregistry.adapter.action.AbstractAction;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.http.AbstractHttpConnector;
import org.commongeoregistry.adapter.http.Connector;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseProcessor;


public class AndroidRegistryClient extends HttpRegistryClient
{
  /**
   * 
   */
  private static final long serialVersionUID = 2367836756416546643L;

  public static final String EXECUTE_ACTIONS  = "executeActions";
  
  private LocalObjectCache localObjectCache;

  /**
   *
   * @param connector URL to the common geo-registry
   */
  public AndroidRegistryClient(Connector connector, Context context)
  {
    super(connector);

    this.localObjectCache = new LocalObjectCache(context, this);
  }

  /**
   *
   * @param connector URL to the common geo-registry
   * @param localObjectCache LocalObjectCache to use with the client
   */
  public AndroidRegistryClient(Connector connector, LocalObjectCache localObjectCache)
  {
    super(connector);

    this.localObjectCache = localObjectCache;
  }

  /**
   * Returns a reference to the object that is managing the local persisted
   * cache on the Android device.
   * 
   * @return a reference to the object that is managing the local persisted
   * cache on the Android device.
   */
  public LocalObjectCache getLocalCache()
  {
    return this.localObjectCache;
  }
  
  /**
   * All modified objects that have been persisted will be pushed to the
   * 
   * common geo-registry.
   */
  public void pushObjectsToRegistry()
  {
    AbstractAction[] actions = this.localObjectCache.getActionHistory();

    String sActions = AbstractAction.serializeActions(actions).toString();

    HttpResponse resp = this.connector.httpPost(EXECUTE_ACTIONS, sActions);
    ResponseProcessor.validateStatusCode(resp);
  }
}
