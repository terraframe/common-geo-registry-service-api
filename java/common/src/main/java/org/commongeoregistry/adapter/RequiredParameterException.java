package org.commongeoregistry.adapter;

public class RequiredParameterException extends RuntimeException
{
  /**
   * 
   */
  private static final long serialVersionUID = 4747172271720334348L;

  /**
   * 
   */

  private String            parameter;

  public RequiredParameterException(String parameter)
  {
    super();

    this.parameter = parameter;
  }

  public RequiredParameterException(String message, Throwable cause, String parameter)
  {
    super(message, cause);

    this.parameter = parameter;
  }

  public RequiredParameterException(String message, String parameter)
  {
    super(message);

    this.parameter = parameter;
  }

  public RequiredParameterException(Throwable cause, String parameter)
  {
    super(cause);

    this.parameter = parameter;
  }

  public String getParameter()
  {
    return parameter;
  }
}
