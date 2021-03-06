/**
 * Copyright (C) 2018-2019 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotels.hcommon.ssh.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.Session;
import com.pastdev.jsch.SessionFactory;

class DelegatingSessionFactory implements SessionFactory {
  private static final Logger log = LoggerFactory.getLogger(DelegatingSessionFactory.class);

  /* VisibleForTesting */
  final SessionFactory delegate;
  /* VisibleForTesting */
  final int sshTimeout;

  DelegatingSessionFactory(SessionFactory delegate, int sshTimeout) {
    this.delegate = delegate;
    this.sshTimeout = sshTimeout;
  }

  @Override
  public String getHostname() {
    return delegate.getHostname();
  }

  @Override
  public int getPort() {
    return delegate.getPort();
  }

  @Override
  public Proxy getProxy() {
    return delegate.getProxy();
  }

  @Override
  public String getUsername() {
    return delegate.getUsername();
  }

  @Override
  public Session newSession() throws JSchException {
    Session session = delegate.newSession();
    log.info("Setting SSH session timeout to {}", sshTimeout);
    session.setTimeout(sshTimeout);
    return session;
  }

  @Override
  public SessionFactoryBuilder newSessionFactoryBuilder() {
    SessionFactoryBuilder builder = delegate.newSessionFactoryBuilder();
    return new DelegatingSessionFactoryBuilder(builder, sshTimeout);
  }

}
