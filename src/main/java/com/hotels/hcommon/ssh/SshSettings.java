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
package com.hotels.hcommon.ssh;

import static com.hotels.hcommon.ssh.validation.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.hotels.hcommon.ssh.validation.constraint.TunnelRoute;
import com.hotels.hcommon.ssh.validation.validator.TunnelRouteValidator;

public class SshSettings {

  public static final String DEFAULT_LOCALHOST = "localhost";
  public static final int DEFAULT_SSH_PORT = 22;
  public static final int DEFAULT_SESSION_TIMEOUT = 0; // never time out
  public static final boolean DEFAULT_STRICT_HOST_KEY_CHECKING = true;

  public static class Builder {
    private int sshPort = DEFAULT_SSH_PORT;
    private String route;
    private String privateKeys;
    private String knownHosts;
    private String localhost = DEFAULT_LOCALHOST;
    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;
    private boolean strictHostKeyChecking = DEFAULT_STRICT_HOST_KEY_CHECKING;

    private Builder() {}

    public Builder withSshPort(@Min(1) @Max(65535) int sshPort) {
      this.sshPort = sshPort;
      return this;
    }

    public Builder withRoute(@TunnelRoute String route) {
      this.route = route;
      return this;
    }

    public Builder withPrivateKeys(@NotNull String privateKeys) {
      this.privateKeys = privateKeys;
      return this;
    }

    public Builder withKnownHosts(String knownHosts) {
      this.knownHosts = knownHosts;
      return this;
    }

    public Builder withLocalhost(String localhost) {
      this.localhost = localhost;
      return this;
    }

    public Builder withSessionTimeout(@Min(0) int sessionTimeout) {
      this.sessionTimeout = sessionTimeout;
      return this;
    }

    public Builder withStrictHostKeyChecking(boolean strictHostKeyChecking) {
      this.strictHostKeyChecking = strictHostKeyChecking;
      return this;
    }

    public SshSettings build() {
      checkArgument(1 <= sshPort && sshPort <= 65535, "Invalid SSH port number: " + sshPort);
      checkArgument(new TunnelRouteValidator().isValid(route, null), "Invalid SSH tunnel route: '" + route + "'");
      checkArgument(privateKeys != null && !privateKeys.trim().isEmpty(),
          "Invalid SSH private keys: '" + privateKeys + "'");
      checkArgument(sessionTimeout >= 0, "Invalid SSH session timeout: " + sessionTimeout);
      return new SshSettings(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  private final int sshPort;
  private final String route;
  private final List<String> privateKeys;
  private final String knownHosts;
  private final String localhost;
  private final int sessionTimeout;
  private final boolean strictHostKeyChecking;

  private SshSettings(Builder builder) {
    sshPort = builder.sshPort;
    route = builder.route;
    privateKeys = Collections.unmodifiableList(Arrays.asList(builder.privateKeys.split(",")));
    knownHosts = builder.knownHosts;
    localhost = builder.localhost;
    sessionTimeout = builder.sessionTimeout;
    strictHostKeyChecking = builder.strictHostKeyChecking;
  }

  public int getSshPort() {
    return sshPort;
  }

  public String getRoute() {
    return route;
  }

  public List<String> getPrivateKeys() {
    return privateKeys;
  }

  public String getKnownHosts() {
    return knownHosts;
  }

  public String getLocalhost() {
    return localhost;
  }

  public int getSessionTimeout() {
    return sessionTimeout;
  }

  public boolean isStrictHostKeyChecking() {
    return strictHostKeyChecking;
  }

}
