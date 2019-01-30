package org.commongeoregistry.adapter.metadata;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Term;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeTermType extends AttributeType
{
  /**
   * 
   */
  private static final long serialVersionUID                = 6431580798592645011L;
  
  public static final String JSON_ROOT_TERM				    = "rootTerm";

  public static String TYPE                                 = "term";
  
  private Term rootTerm                                     = null;
  
  private Map<String, Term> termMap                                 = null;
  
  public AttributeTermType(String _name, String _localizedLabel, String _localizedDescription, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE, _isDefault);
    
    this.termMap = new ConcurrentHashMap<String, Term>();
  }
  
  public Term getRootTerm()
  {
    return this.rootTerm;
  }
  
  public void setRootTerm(Term rootTerm)
  {
	this.termMap.clear();
	
    this.rootTerm = rootTerm;
    
    this.buildTermMap(rootTerm);
  }
  
  private void buildTermMap(Term term)
  {
    this.termMap.put(term.getCode(), term);
    
    for (Term childTerm : term.getChildren())
    {
      this.buildTermMap(childTerm);
    }
  }

  public List<Term> getTerms()
  {
    return this.rootTerm.getChildren();
  }
  
  public Optional<Term> getTermByCode(String termCode)
  {
	Term term = this.termMap.get(termCode);
	  
    return Optional.of(term);
  }
  
  @Override
  public JsonObject toJSON(CustomSerializer serializer)
  {
	JsonObject json  = super.toJSON(serializer);
    
	if (this.rootTerm != null)
	{
	  json.add(JSON_ROOT_TERM, this.getRootTerm().toJSON());
	}

    return json;
  }
  
  /**
   * Populates any additional attributes from JSON that were not populated in
   * {@link GeoObjectType#fromJSON(String, org.commongeoregistry.adapter.RegistryAdapter)} 
   * 
   * @param attrObj
   * @return {@link AttributeType}
   */
  @Override
  public void fromJSON(JsonObject attrObj)
  {
	super.fromJSON(attrObj);
	  
	JsonElement termElement = attrObj.get(AttributeTermType.JSON_ROOT_TERM);
	
	if (termElement != null)
	{
	  Term rootTerm = Term.fromJSON(termElement.getAsJsonObject());
	  this.setRootTerm(rootTerm);
	}
  }
}
