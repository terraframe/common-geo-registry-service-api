/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpCredentialConnector extends AbstractHttpConnector
{
  private String username;

  private String password;

  public void setCredentials(String username, String password)
  {
    this.username = username;
    this.password = password;
  }

  synchronized public void initialize()
  {
    class DefaultTrustManager implements X509TrustManager
    {

      @Override
      public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
      {
      }

      @Override
      public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
      {
      }

      @Override
      public X509Certificate[] getAcceptedIssuers()
      {
        return null;
      }
    }

    try
    {
      SSLContext ctx = SSLContext.getInstance("TLS");

      ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());

      SSLContext.setDefault(ctx);
    }
    catch (KeyManagementException | NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }

    /*
     * Set the default authenticator for the VM
     */
    Authenticator.setDefault(new Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(username, password.toCharArray());
      }
    });
  }
}
